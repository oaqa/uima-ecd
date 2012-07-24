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

import java.util.Arrays;
import java.util.List;

import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Ints;

import edu.cmu.lti.oaqa.ecd.util.CasUtils;
import edu.cmu.lti.oaqa.framework.types.ExperimentUUID;
import edu.cmu.lti.oaqa.framework.types.InputElement;
import edu.cmu.lti.oaqa.framework.types.ProcessingStep;

public class ProcessingStepUtils {
  
  public static String getCurrentExperimentId(JCas jcas) {
    ExperimentUUID experiment = getCurrentExperiment(jcas);
    final String uuid = experiment.getUuid();
    return uuid;
  }
  
  public static Trace getTrace(JCas jcas) {
    AnnotationIndex<Annotation> steps = jcas.getAnnotationIndex(ProcessingStep.type);
    return getTrace(steps);
  } 
  
  public static Trace getTrace(Iterable<Annotation> steps) {
    return getTraceString(steps, new ProcessingStepToString());
  } 
  
  private static Trace getTraceString(Iterable<Annotation> steps, 
          Function<Annotation,String> f) {
    Joiner joiner = Joiner.on(">").skipNulls();
    List<Annotation> list = new ProcessingStepOrdering().sortedCopy(steps);
    String trace = joiner.join(Lists.transform(list, f));
    return new Trace(trace);
  }
  
  public static ProcessingStep getMax(Iterable<Annotation> steps) {
    List<Annotation> list = new ProcessingStepOrdering().sortedCopy(steps);
    return (ProcessingStep) list.get(list.size() - 1);
  }
  
  public static String getPreviousCasId(Iterable<Annotation> steps) {
    List<Annotation> list = new ProcessingStepOrdering().sortedCopy(steps);
    if (list.size() == 0) {
      return null;
    }
    return ((ProcessingStep) list.get(list.size() - 1)).getCasId();
  }
  
  private static final class ProcessingStepToString implements Function<Annotation, String> {
    @Override
    public String apply(Annotation arg0) {
      ProcessingStep ps = (ProcessingStep) arg0;
      return String.format("%s|%s", ps.getPhaseId(), ps.getComponent());
    }
  }
  
  public static Trace getPartialTrace(String prevTrace, int phaseNo, String optionId) {
    String key = phaseNo + "|" + optionId;
    if (prevTrace.length() > 0) {
      Joiner joiner = Joiner.on(">").skipNulls();
      String trace = joiner.join(Arrays.asList(prevTrace, key));
      return new Trace(trace);
    } else {
      return new Trace(key);
    }
  }
  
  public static int getSequenceId(JCas nextCas) {
    InputElement input = (InputElement) CasUtils.getFirst(nextCas, 
            InputElement.class.getName());
    int sequenceId = input.getSequenceId();
    return sequenceId;
  }
  
  public static InputElement getInputElement(JCas nextCas) {
    return (InputElement) CasUtils.getFirst(nextCas, 
            InputElement.class.getName());
  }
  
  public static ExperimentUUID getCurrentExperiment(JCas jcas) {
    AnnotationIndex<Annotation> steps = jcas.getAnnotationIndex(ExperimentUUID.type);
    List<Annotation> list = new ExperimentUUIDOrdering().sortedCopy(steps);
    return (ExperimentUUID) list.get(list.size() - 1);
  }
  
  private static final class ExperimentUUIDOrdering extends Ordering<Annotation> {
    @Override
    public int compare(Annotation arg0, Annotation arg1) {
      return Ints.compare(((ExperimentUUID) arg0).getStageId(), 
              ((ExperimentUUID) arg1).getStageId());
    }
  } 
  
  private static final class ProcessingStepOrdering extends Ordering<Annotation> {
    @Override
    public int compare(Annotation arg0, Annotation arg1) {
      return Ints.compare(((ProcessingStep) arg0).getPhaseId(), 
              ((ProcessingStep) arg1).getPhaseId());
    }
  } 
}
