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

package edu.cmu.lti.oaqa.ecd.example;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.fit.component.CasConsumer_ImplBase;

import edu.cmu.lti.oaqa.ecd.phase.ProcessingStepUtils;
import edu.cmu.lti.oaqa.ecd.phase.Trace;
import edu.cmu.lti.oaqa.framework.types.ExperimentUUID;
import edu.cmu.lti.oaqa.framework.types.ProcessingStep;

public class ExampleConsumer extends CasConsumer_ImplBase {



  /**
   * Reads the results from the retrieval phase from the DOCUMENT and the DOCUEMNT_GS views of the
   * JCAs, and generates and evaluates them using the evaluate method from the FMeasureConsumer
   * class.
   */
  @Override
  public void process(CAS aCAS) throws AnalysisEngineProcessException {
    try {
      JCas jcas = aCAS.getJCas();
      ExperimentUUID experiment = ProcessingStepUtils.getCurrentExperiment(jcas);
      AnnotationIndex<Annotation> steps = jcas.getAnnotationIndex(ProcessingStep.type);
      String uuid = experiment.getUuid();
      Trace trace = ProcessingStepUtils.getTrace(steps);
      System.out.printf("%s: %s", uuid, trace);
    } catch (Exception e) {
      throw new AnalysisEngineProcessException(e);
    }
  }
}
