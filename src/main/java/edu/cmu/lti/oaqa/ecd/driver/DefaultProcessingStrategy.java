package edu.cmu.lti.oaqa.ecd.driver;

import java.util.List;
import java.util.Set;

import org.apache.uima.analysis_engine.metadata.impl.FixedFlow_impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import edu.cmu.lti.oaqa.ecd.flow.FunneledFlow;
import edu.cmu.lti.oaqa.ecd.phase.Trace;

final class DefaultProcessingStrategy implements ProcessingStrategy {
  
  @Override
  public FunneledFlow newFunnelStrategy(String experimentUuid) {
    List<String> matchingTraces = getMatchingTraces(experimentUuid);
    return new BaseFunnelStrategy(matchingTraces);
  }
  
  private List<String> getMatchingTraces(String experiment) {
    return Lists.<String>newArrayList();
  }
  
  private final static class BaseFunnelStrategy extends FixedFlow_impl implements FunneledFlow {

    private static final long serialVersionUID = -8276851415955188563L;
    
    private final Set<String> set;
    
    public BaseFunnelStrategy(Iterable<String> traces) {
      this.set = Sets.newHashSet(traces);
    }
    
    @Override
    public boolean funnel(Trace trace) {
      return set.contains(trace.getTraceHash());
    }
  }

}
