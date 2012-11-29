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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

public class FirstPhaseAnnotatorC1 extends JCasAnnotator_ImplBase {

  @Override
  public void initialize(UimaContext context) throws ResourceInitializationException {
    Object o = (Object) context.getConfigParameterValue("float-value");
    assertThat(o, is(instanceOf(Float.class)));
    Object o2 = (Object) context.getConfigParameterValue("parameter-c");
    assertThat(o2, is(instanceOf(Float.class)));
  }
  
  @Override
  public void process(JCas jcas) throws AnalysisEngineProcessException {
    System.out.printf("process: %s\n", getClass().getSimpleName());
  }
}
