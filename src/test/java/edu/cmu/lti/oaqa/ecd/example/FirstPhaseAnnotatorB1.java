package edu.cmu.lti.oaqa.ecd.example;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;

public class FirstPhaseAnnotatorB1 extends JCasAnnotator_ImplBase {

  @Override
  public void process(JCas jcas) throws AnalysisEngineProcessException {
    System.out.printf("process: %s\n", getClass().getSimpleName());
  }
}
