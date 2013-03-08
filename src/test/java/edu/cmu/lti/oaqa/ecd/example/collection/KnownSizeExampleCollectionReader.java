package edu.cmu.lti.oaqa.ecd.example.collection;

import java.io.IOException;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CAS;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.collection.CollectionReader_ImplBase;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Progress;
import org.apache.uima.util.ProgressImpl;

import edu.cmu.lti.oaqa.ecd.BaseExperimentBuilder;
import edu.cmu.lti.oaqa.ecd.collection.KnownSizeCollectionReader;
import edu.cmu.lti.oaqa.framework.types.ExperimentUUID;
import edu.cmu.lti.oaqa.framework.types.InputElement;

public class KnownSizeExampleCollectionReader extends CollectionReader_ImplBase implements KnownSizeCollectionReader {

  private int count;

  private AnalysisEngine[] decorators;

  private String experimentUuid;
  
  private int stageId;

  private int total;
  
  private int size;
  
  @Override
  public void initialize() throws ResourceInitializationException {
    count = 0;
    this.experimentUuid = (String) getConfigParameterValue(BaseExperimentBuilder.EXPERIMENT_UUID_PROPERTY);
    this.stageId = (Integer) getConfigParameterValue(BaseExperimentBuilder.STAGE_ID_PROPERTY);
    this.total = (Integer) getConfigParameterValue("total");
    size = total;
    String decoratorsNames = (String) getConfigParameterValue("decorators");
    if (decoratorsNames != null) {
      this.decorators = BaseExperimentBuilder.createAnnotators(decoratorsNames);
    }
  }
  
  protected String getUUID() {
    return experimentUuid;
  }
  
  protected int getStageId() {
    return stageId;
  }

  @Override
  public boolean hasNext() throws IOException, CollectionException {
    total--;
    return total >= 0;
  }

  @Override
  public void getNext(CAS aCAS) throws IOException, CollectionException {
    try {
      count++;
      JCas jcas = aCAS.getJCas();
      jcas.setDocumentText("Text to analyze #"+count);
      ExperimentUUID expUuid = new ExperimentUUID(jcas);
      expUuid.setUuid(getUUID());
      expUuid.setStageId(getStageId());
      expUuid.addToIndexes();
      InputElement next = new InputElement(jcas);
      next.setSequenceId(String.valueOf(total + 1));
      next.addToIndexes();
      decorate(jcas);
      //updateExperimentMeta(getUUID(), count);
    } catch (UIMAException e) {
      throw new CollectionException(e);
    }
  }

  protected void decorate(JCas jcas) throws AnalysisEngineProcessException {
    if (decorators != null) {
      for (AnalysisEngine appender : decorators) {
        appender.process(jcas);
      }
    }
  }

  @Override
  public Progress[] getProgress() {
    return new Progress[] { new ProgressImpl(count, -1, Progress.ENTITIES) };
  }

  @Override
  public void close() throws IOException {
  }

  @Override
  public int size() {
    return size;
  }
}
