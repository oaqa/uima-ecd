package edu.cmu.lti.oaqa.ecd.example;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;

import edu.cmu.lti.oaqa.ecd.CasUtils;
import edu.cmu.lti.oaqa.framework.types.InputElement;
import edu.cmu.lti.oaqa.framework.types.OutputElement;

public class SecondPhaseAnnotatorBase extends JCasAnnotator_ImplBase {
	
	public String getName() { return "SecondPhaseAnnotator"; }

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
	  System.out.printf("process: %s\n", getClass().getSimpleName());
		InputElement input = (InputElement) CasUtils.getFirst(jcas , 
		    "edu.cmu.lti.oaqa.framework.types.InputElement" );
		if ( input == null ) {
		  throw new AnalysisEngineProcessException();
		}
		OutputElement answer = new OutputElement(jcas);
		answer.setAnswer("Vesuvius");
		answer.setSequenceId(input.getSequenceId());
		answer.addToIndexes();
	}

}
