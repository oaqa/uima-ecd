package edu.cmu.lti.oaqa.ecd.driver;

import java.util.List;

import com.google.common.collect.Lists;

import edu.cmu.lti.oaqa.ecd.flow.FunneledFlow;
import edu.cmu.lti.oaqa.ecd.funnel.SetBasedFunnelStrategy;

final class DefaultProcessingStrategy implements ProcessingStrategy {
  
  @Override
  public FunneledFlow newFunnelStrategy(String experimentUuid) {
    List<String> matchingTraces = getMatchingTraces(experimentUuid);
    return new SetBasedFunnelStrategy(matchingTraces);
  }
  
  private List<String> getMatchingTraces(String experiment) {
    return Lists.<String>newArrayList();
  }
}
