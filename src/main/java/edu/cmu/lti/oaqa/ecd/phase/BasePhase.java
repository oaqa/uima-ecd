/*
 *  Copyright 2012 Carnegie Mellon University
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package edu.cmu.lti.oaqa.ecd.phase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.impl.Serialization;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.fit.component.JCasMultiplier_ImplBase;
import org.apache.uima.fit.descriptor.OperationalProperties;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.CasCopier;
import org.xml.sax.SAXException;

import com.google.common.base.Throwables;

import edu.cmu.lti.oaqa.ecd.BaseExperimentBuilder;
import edu.cmu.lti.oaqa.ecd.phase.event.PhaseEventBus;
import edu.cmu.lti.oaqa.ecd.phase.event.TerminateEvent;
import edu.cmu.lti.oaqa.ecd.util.CasUtils;
import edu.cmu.lti.oaqa.framework.types.ExperimentUUID;
import edu.cmu.lti.oaqa.framework.types.InputElement;
import edu.cmu.lti.oaqa.framework.types.ProcessingStep;

// The superclass also adds this annotation, we just want to be explicit about doing it
@OperationalProperties(outputsNewCases = true)
public final class BasePhase extends JCasMultiplier_ImplBase {

  private final ExecutorService executor = Executors.newSingleThreadExecutor();

  public static final String QA_INTERNAL_PHASEID = "__.qa.internal.phaseid.__";

  private static final String TIMEOUT_KEY = "option-timeout";

  private static final String LAZY_LOAD_KEY = "lazy-load-options";

  private static final int DEFAULT_OPTION_TIMEOUT = 5;

  private AnalysisEngine[] options;

  private AnalysisEngineDescription[] optionDescriptions;

  private int nextAnnotator;

  private JCas cas;

  private Integer phaseNo;

  private Integer optionTimeout;

  private boolean lazyLoadOptions;

  private String phaseName;

  private PhasePersistenceProvider persistence;

  @Override
  public void initialize(UimaContext ctx) throws ResourceInitializationException {
    super.initialize(ctx);
    String pp = (String) ctx.getConfigParameterValue("persistence-provider");
    if (pp == null) {
      throw new ResourceInitializationException(new IllegalArgumentException(
              "Must provide a parameter of type <persistence-provider>"));
    }
    this.persistence = BaseExperimentBuilder.loadProvider(pp, PhasePersistenceProvider.class);
    this.phaseName = (String) ctx.getConfigParameterValue("name");
    this.phaseNo = (Integer) ctx.getConfigParameterValue(QA_INTERNAL_PHASEID);
    this.optionTimeout = (Integer) ctx.getConfigParameterValue(TIMEOUT_KEY);
    if (optionTimeout == null) {
      this.optionTimeout = DEFAULT_OPTION_TIMEOUT;
    }
    Boolean lazyLoadOpts = (Boolean) ctx.getConfigParameterValue(LAZY_LOAD_KEY);
    if (lazyLoadOpts == null) {
      this.lazyLoadOptions = false;
    } else {
      this.lazyLoadOptions = lazyLoadOpts.booleanValue();
    }
    System.out.printf("Phase: %s (lazy-load=%s)\n", toString(), lazyLoadOptions);
    String experimentId = (String) ctx
            .getConfigParameterValue(BaseExperimentBuilder.EXPERIMENT_UUID_PROPERTY);
    String optDescr = (String) ctx.getConfigParameterValue("options");
    BasePhaseLoader loader = new BasePhaseLoader();
    this.optionDescriptions = loader.loadOptionDescriptions(optDescr);
    if (!lazyLoadOptions) {
      this.options = loader.loadOptions(optionDescriptions);
    }
    if (size() == 0) {
      throw new ResourceInitializationException(new IllegalArgumentException("Phase: " + toString()
              + " provided no options"));
    }
    for (AnalysisEngineDescription ae : optionDescriptions) {
      System.out.println("\t- " + ae.getAnalysisEngineMetaData().getName());
    }
    System.out.println(" Total # of options configured: " + size());

    int stageId = (Integer) ctx.getConfigParameterValue(BaseExperimentBuilder.STAGE_ID_PROPERTY);
    persistence.insertExperimentMeta(experimentId, phaseNo, stageId, size());
    nextAnnotator = 0;
  }

  @Override
  public void process(JCas aJCas) throws AnalysisEngineProcessException {
    cas = aJCas;
    nextAnnotator = 0;
  }

  @Override
  public boolean hasNext() throws AnalysisEngineProcessException {
    return (nextAnnotator < optionDescriptions.length);
  }

  public Integer getPhaseNo() {
    return phaseNo;
  }

  // We will call the process() method for each of the annotator on this phase,
  // returning a new CAS for each of them
  // See
  // http://uima.apache.org/d/uimaj-2.4.0/apidocs/org/apache/uima/analysis_component/AnalysisComponent.html
  @Override
  public JCas next() throws AnalysisEngineProcessException {
    JCas nextCas = getEmptyJCas();
    try {
      greedyCopy(cas, nextCas);
      AnalysisEngine ae = getAnalysisEngine();
      try {
        AnnotationIndex<Annotation> prevSteps = nextCas.getAnnotationIndex(ProcessingStep.type);
        String prevCasId = ProcessingStepUtils.getPreviousCasId(prevSteps);
        Trace prevTrace = ProcessingStepUtils.getTrace(prevSteps);
        String optionId = ae.getAnalysisEngineMetaData().getName();
        String sequenceId = ProcessingStepUtils.getSequenceId(nextCas);
        Trace trace = ProcessingStepUtils.getPartialTrace(prevTrace.getTrace(), getPhaseNo(),
                optionId);
        if (!loadCasFromStorage(nextCas, trace, sequenceId)) {
          process(ae, nextCas, prevCasId, prevTrace, optionId, sequenceId, trace);
        }
        // TODO:Should this be in a final clause?
        // Otherwise how would we move on to the next annotator in case of exception?
        nextAnnotator++;
        return nextCas;
      } catch (Exception e) {
        throw new AnalysisEngineProcessException(e);
      } finally {
        if (options != null) {
          ae.destroy();
        }
      }
    } catch (Exception e) {
      // Release if greedyCopy fails
      nextCas.release();
      throw new AnalysisEngineProcessException(e);
    }
  }

  private AnalysisEngine getAnalysisEngine() throws ResourceInitializationException {
    if (options != null) {
      return options[nextAnnotator];
    } else {
      AnalysisEngine ae = AnalysisEngineFactory.createAggregate(optionDescriptions[nextAnnotator]);
      return ae;
    }
  }

  @Override
  public void collectionProcessComplete() throws AnalysisEngineProcessException {
    if (options != null) {
      for (AnalysisEngine ae : options) {
        ae.collectionProcessComplete();
      }
    }
    executor.shutdown();
  }

  private void process(final AnalysisEngine ae, JCas nextCas, String prevCasId, Trace prevTrace,
          String optionId, String sequenceId, Trace trace) throws IOException, SAXException,
          Exception {
    long a = System.currentTimeMillis();
    final String uuid = ProcessingStepUtils.getCurrentExperimentId(nextCas);
    final String key = ProcessingStepUtils.getExecutionIdHash(uuid, trace, sequenceId);
    try {
      insertExecutionTrace(nextCas, optionId, a, prevCasId, trace, key);
      System.out.printf("[%s] Executing option: %s on trace %s\n", sequenceId, optionId, prevTrace);
      // Wrap the JCas to ignore downstream method invocation in case component timeout.
      final DeferredTerminationJCasWrapper wrapped = new DeferredTerminationJCasWrapper(nextCas);
      // We are using a single thread executor for each phase, this means that
      // at any point no more than a single task should be executed until
      // the previous task finishes or dies
      Future<?> future = executor.submit(new Runnable() {
        @Override
        public void run() {
          try {
            // The execution id hash is added just before the future is executed
            wrapped.addExecutionIdHash(key);
            // Here is where the actual option is processed
            ae.process(wrapped);
          } catch (Exception e) {
            // Propagate the exception so it gets printed.
            // An async mechanism should be used to get the exception
            // back into the calling thread
            Throwables.propagate(e);
          }
        }
      });
      try {
        future.get(optionTimeout, TimeUnit.MINUTES);
        long b = System.currentTimeMillis();
        addProcessingStep(nextCas, optionId, key);
        storeCas(nextCas, b, key);
        System.out.printf("[%s]  Execution time for option %s: %ss\n", sequenceId, optionId,
                (b - a) / 1000);
      } catch (TimeoutException e) {
        // If the AE is taking too long and it times out it could be either:
        // 1) It's reading from a resource or
        // 2) it's stuck on a iterative algorithm.

        // First send a termination event so the AE is aware that it's been terminated.
        // Classes that inherit from TerminableComponent have access to this information
        // and should check if the component has been terminated.
        PhaseEventBus.sendTerminateEvent(new TerminateEvent(key));
        // The cancel(true) method will call notify, any waiting IO operations will be interrupted,
        // with an InterruptedIOException and the component be rendered invalid.
        future.cancel(true);
        // We invalidate the JCas wrapper, so any subsequent calls to it will throw an Exception
        wrapped.invalidate();
        long b = System.currentTimeMillis();
        storeException(b, e, key, ExecutionStatus.TIMEOUT);
        System.out.printf("[%s]  Execution timed out for option: %s after %ss\n", sequenceId,
                optionId, (b - a) / 1000);
        // Finally re-throw the exception to allow the flow controller do its job
        throw e;
      }
    } catch (Exception e) {
      long b = System.currentTimeMillis();
      try {
        storeException(b, e, key, ExecutionStatus.FAILURE);
        System.out.printf("[%s]  Execution failed for option: %s after %ss\n", sequenceId,
                optionId, (b - a) / 1000);
      } finally {
        nextCas.release();
      }
      throw e; // Re-throw exception to allow the Flow controller do its job
    }
  }

  private void greedyCopy(JCas existingCas, JCas emptyCas) {
    // nothing 'greedy' about the default implementation, which copies everything.
    CasCopier.copyCas(existingCas.getCas(), emptyCas.getCas(), true);
  }

  private void addProcessingStep(JCas jcas, String optionId, String key) {
    ProcessingStep s = new ProcessingStep(jcas);
    s.setComponent(optionId);
    s.setPhaseId(getPhaseNo());
    s.setCasId(key);
    s.addToIndexes();
  }

  private void insertExecutionTrace(JCas jcas, final String optionId, final long startTime,
          final String prevCas, final Trace trace, final String key) throws IOException {
    final String uuid = ProcessingStepUtils.getCurrentExperimentId(jcas);
    InputElement input = (InputElement) CasUtils.getFirst(jcas, InputElement.class.getName());
    final String dataset = input.getDataset();
    final String sequenceId = input.getSequenceId();
    final String question = input.getQuestion();
    persistence.insertExecutionTrace(optionId, question, sequenceId, dataset, getPhaseNo(), uuid, startTime,
            getHostName(), trace.getTrace(), key);
  }

  private void storeCas(JCas jcas, final long endTime, final String key) throws IOException,
          SAXException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    Serialization.serializeWithCompression(jcas.getCas(), baos);
    final byte[] bytes = baos.toByteArray();
    persistence.storeCas(bytes, ExecutionStatus.SUCCESS, endTime, key);
  }

  public static void deserializeCAS(CAS cas, InputStream in) {
    Serialization.deserializeCAS(cas, in);
  }

  private void storeException(final long endTime, Exception e, final String key,
          ExecutionStatus status) throws IOException, SAXException {
    Throwable rootCause = Throwables.getRootCause(e);
    String rootCauseString = Throwables.getStackTraceAsString(rootCause);
    final byte[] bytes = rootCauseString.getBytes("UTF8");
    persistence.storeException(bytes, status, endTime, key);
  }

  private boolean loadCasFromStorage(JCas jcas, Trace trace, String sequenceId) throws SQLException {
    ExperimentUUID experiment = ProcessingStepUtils.getCurrentExperiment(jcas);
    String experimentId = experiment.getUuid();
    int stageId = experiment.getStageId();
    final String hash = ProcessingStepUtils.getExecutionIdHash(experimentId, trace, sequenceId);
    CasDeserializer deserializer = persistence.deserialize(jcas, hash);
    if (deserializer.processedCas()) {
      ExperimentUUID expUuid = new ExperimentUUID(jcas);
      expUuid.setUuid(experimentId);
      expUuid.setStageId(stageId);
      expUuid.addToIndexes();
      System.err.printf("Loaded cas for %s @ %s\n", sequenceId, trace.getTrace());
    }
    return deserializer.processedCas();
  }

  private String getHostName() throws IOException {
    InetAddress addr = InetAddress.getLocalHost();
    return addr.getHostName();
  }

  @Override
  public String toString() {
    return String.format("%s|%s>", getPhaseNo(), phaseName);
  }

  int size() {
    return optionDescriptions.length;
  }
}
