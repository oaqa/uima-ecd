package edu.cmu.lti.oaqa.ecd.base;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;

public class NoOpAnnotator extends JCasAnnotator_ImplBase {
	
  @Override
  public void process(JCas jcas) throws AnalysisEngineProcessException {
 
  }
}
