package edu.cmu.lti.oaqa.ecd.example;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;


public class SecondPhaseAnnotatorC1 extends SecondPhaseAnnotatorBase { 
  @Override
  public void process(JCas jcas) throws AnalysisEngineProcessException {
    System.out.printf("process: %s\n", getClass().getSimpleName());
    try {
      throw new NullPointerException();
    } catch (Exception e) {
      throw new AnalysisEngineProcessException(e);
    }
  }
}
