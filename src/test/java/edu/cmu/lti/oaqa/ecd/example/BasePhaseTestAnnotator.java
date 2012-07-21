package edu.cmu.lti.oaqa.ecd.example;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

public class BasePhaseTestAnnotator extends JCasAnnotator_ImplBase {
  @Override
  public void initialize(UimaContext context) throws ResourceInitializationException {
    String name = (String) context.getConfigParameterValue("name");
    if (name == null) {
      throw new ResourceInitializationException(
              new RuntimeException("Expected name != null"));
    }
  }
  @Override
  public void process(JCas jcas) throws AnalysisEngineProcessException {
    System.out.printf("process: %s\n", getClass().getSimpleName());
  }
}
