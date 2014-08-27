/*
 Copyright 2009-2010  Regents of the University of Colorado.
 All rights reserved.

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

package edu.cmu.lti.oaqa.ecd.driver;

import static java.util.Arrays.asList;
import static org.apache.uima.fit.factory.AnalysisEngineFactory.createAggregate;
import static org.apache.uima.fit.factory.AnalysisEngineFactory.createAggregateDescription;
import static org.apache.uima.fit.factory.CollectionReaderFactory.createCollectionReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.analysis_engine.CasIterator;
import org.apache.uima.analysis_engine.JCasIterator;
import org.apache.uima.cas.CAS;
import org.apache.uima.collection.CollectionReader;
import org.apache.uima.collection.CollectionReaderDescription;
import org.apache.uima.collection.base_cpm.BaseCollectionReader;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.Resource;
import org.apache.uima.resource.metadata.ResourceMetaData;
import org.apache.uima.util.CasCreationUtils;

// We added this file here to bring uimaFit 1.3.1 bug improved pipeline 
// while preserving compatibility with UIMA 2.3.1
// Elmer

/**
 * @author Steven Bethard, Philip Ogren
 * @author Richard Eckart de Castilho
 *
 */
public final class SimplePipelineRev803 {
  private SimplePipelineRev803() {
    // This class is not meant to be instantiated
  }

  /**
   * Run the CollectionReader and AnalysisEngines as a pipeline. After processing all CASes
   * provided by the reader, the method calls {@link AnalysisEngine#collectionProcessComplete()
   * collectionProcessComplete()} on the engines and {@link Resource#destroy() destroy()} on all
   * engines.
   *
   * @param reader
   *            The CollectionReader that loads the documents into the CAS.
   * @param descs
   *            Primitive AnalysisEngineDescriptions that process the CAS, in order. If you have a
   *            mix of primitive and aggregate engines, then please create the AnalysisEngines
   *            yourself and call the other runPipeline method.
   * @throws UIMAException
   * @throws IOException
   */
  public static void runPipeline(final CollectionReader reader,
      final AnalysisEngineDescription... descs) throws UIMAException, IOException {
    // Create AAE
    final AnalysisEngineDescription aaeDesc = createAggregateDescription(descs);

    // Instantiate AAE
    final AnalysisEngine aae = createAggregate(aaeDesc);

    // Create CAS from merged metadata
    final CAS cas = CasCreationUtils.createCas(asList(reader.getMetaData(), aae.getMetaData()));
    
    try {
      // Process
      while (reader.hasNext()) {
        reader.getNext(cas);
        aae.process(cas);
        cas.reset();
      }
      // Signal end of processing
      aae.collectionProcessComplete();
    }
    finally {
      // Destroy
      aae.destroy();
      destroy(reader);
    }
  }

  /**
   * Run the CollectionReader and AnalysisEngines as a pipeline. After processing all CASes
   * provided by the reader, the method calls {@link AnalysisEngine#collectionProcessComplete()
   * collectionProcessComplete()} on the engines, {@link CollectionReader#close() close()} on the
   * reader and {@link Resource#destroy() destroy()} on the reader and all engines.
   *
   * @param readerDesc
   *            The CollectionReader that loads the documents into the CAS.
   * @param descs
   *            Primitive AnalysisEngineDescriptions that process the CAS, in order. If you have a
   *            mix of primitive and aggregate engines, then please create the AnalysisEngines
   *            yourself and call the other runPipeline method.
   * @throws UIMAException
   * @throws IOException
   */
  public static void runPipeline(final CollectionReaderDescription readerDesc,
      final AnalysisEngineDescription... descs) throws UIMAException, IOException {
    // Create the components
    final CollectionReader reader = createCollectionReader(readerDesc);

    try {
      // Run the pipeline
      runPipeline(reader, descs);
    }
    finally {
      close(reader);
      destroy(reader);
    }
  }

  /**
   * Provides a simple way to run a pipeline for a given collection reader and sequence of
   * analysis engines. After processing all CASes provided by the reader, the method calls
   * {@link AnalysisEngine#collectionProcessComplete() collectionProcessComplete()} on the
   * engines.
   *
   * @param reader
   *            a collection reader
   * @param engines
   *            a sequence of analysis engines
   * @throws UIMAException
   * @throws IOException
   */
  public static void runPipeline(final CollectionReader reader, final AnalysisEngine... engines)
      throws UIMAException, IOException {
    final List<ResourceMetaData> metaData = new ArrayList<ResourceMetaData>();
    metaData.add(reader.getMetaData());
    for (AnalysisEngine engine : engines) {
      metaData.add(engine.getMetaData());
    }
    
    final CAS cas = CasCreationUtils.createCas(metaData);
    try {
      while (reader.hasNext()) {
        reader.getNext(cas);
        runPipeline(cas, engines);
        cas.reset();
      }
    } finally {
      collectionProcessComplete(engines);
      destroy(reader);
    }
  }

  /**
   * Run a sequence of {@link AnalysisEngine analysis engines} over a {@link JCas}. The result of
   * the analysis can be read from the JCas.
   *
   * @param aCas
   *            the CAS to process
   * @param aDescs
   *            a sequence of analysis engines to run on the jCas
   * @throws UIMAException
   * @throws IOException
   */
  public static void runPipeline(final CAS aCas, final AnalysisEngineDescription... aDescs)
      throws UIMAException, IOException {
    // Create aggregate AE
    final AnalysisEngineDescription aaeDesc = createAggregateDescription(aDescs);

    // Instantiate
    final AnalysisEngine aae = createAggregate(aaeDesc);
    try {
      // Process
      aae.process(aCas);
      
      // Signal end of processing
      aae.collectionProcessComplete();
    }
    finally {
      // Destroy
      aae.destroy();
    }
  }

  /**
   * Run a sequence of {@link AnalysisEngine analysis engines} over a {@link JCas}. The result of
   * the analysis can be read from the JCas.
   *
   * @param jCas
   *            the jCas to process
   * @param descs
   *            a sequence of analysis engines to run on the jCas
   * @throws UIMAException
   * @throws IOException
   */
  public static void runPipeline(final JCas jCas, final AnalysisEngineDescription... descs)
      throws UIMAException, IOException {
    runPipeline(jCas.getCas(), descs);
  }

  /**
   * Run a sequence of {@link AnalysisEngine analysis engines} over a {@link JCas}. This method
   * does not {@link AnalysisEngine#destroy() destroy} the engines or send them other events like
   * {@link AnalysisEngine#collectionProcessComplete()}. This is left to the caller.
   *
   * @param jcas
   *            the jCas to process
   * @param engines
   *            a sequence of analysis engines to run on the jCas
   * @throws UIMAException
   * @throws IOException
   */
  public static void runPipeline(final JCas jcas, final AnalysisEngine... engines)
      throws UIMAException, IOException {
    if (engines.length == 0) {
      return;
    }
    JCasIterator jcasIter = engines[0].processAndOutputNewCASes(jcas);
    AnalysisEngine[] enginesRemains = Arrays.copyOfRange(engines, 1, engines.length);
    while (jcasIter.hasNext()) {
      JCas nextJcas = jcasIter.next();
      runPipeline(nextJcas, enginesRemains);
      nextJcas.release();
    }
  }

  /**
   * Run a sequence of {@link AnalysisEngine analysis engines} over a {@link CAS}. This method
   * does not {@link AnalysisEngine#destroy() destroy} the engines or send them other events like
   * {@link AnalysisEngine#collectionProcessComplete()}. This is left to the caller.
   *
   * @param cas
   *            the CAS to process
   * @param engines
   *            a sequence of analysis engines to run on the jCas
   * @throws UIMAException
   * @throws IOException
   */
  public static void runPipeline(final CAS cas, final AnalysisEngine... engines)
      throws UIMAException, IOException {
    if (engines.length == 0) {
      return;
    }
    CasIterator casIter = engines[0].processAndOutputNewCASes(cas);
    AnalysisEngine[] enginesRemains = Arrays.copyOfRange(engines, 1, engines.length);
    while (casIter.hasNext()) {
      CAS nextCas = casIter.next();
      runPipeline(nextCas, enginesRemains);
      nextCas.release();
    }
  }

  /**
   * Notify a set of {@link AnalysisEngine analysis engines} that the collection process is complete.
   */
  public static void collectionProcessComplete(final AnalysisEngine... engines)
      throws AnalysisEngineProcessException {
    for (AnalysisEngine e : engines) {
      e.collectionProcessComplete();
    }
  }

  /**
   * Destroy a set of {@link Resource resources}.
   */
  public static void destroy(final Resource... resources)
  {
    for (Resource r : resources) {
      if (r != null) {
        r.destroy();
      }
    }
  }
  
  private static void close(final BaseCollectionReader aReader)
  {
    if (aReader == null) {
      return;
    }
    
    try {
      aReader.close();
    }
    catch (IOException e) {
      // Ignore.
    }
  }
}
