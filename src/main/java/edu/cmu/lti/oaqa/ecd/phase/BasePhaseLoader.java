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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mx.bigdata.anyobject.AnyObject;
import mx.bigdata.anyobject.AnyTuple;

import org.apache.uima.analysis_component.AnalysisComponent;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.resource.ResourceInitializationException;
import org.uimafit.descriptor.OperationalProperties;
import org.uimafit.factory.AggregateBuilder;
import org.uimafit.factory.AnalysisEngineFactory;
import org.yaml.snakeyaml.Yaml;

import com.google.common.base.Joiner;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import edu.cmu.lti.oaqa.ecd.BaseExperimentBuilder;
import edu.cmu.lti.oaqa.ecd.ResourceHandle;
import edu.cmu.lti.oaqa.ecd.ResourceHandle.HandleType;

// The superclass also adds this annotation, we just want to be explicit about doing it
@OperationalProperties(outputsNewCases = true)
public final class BasePhaseLoader {
  
  private String classTag;
  
  public BasePhaseLoader() {
    this("class");
  }
  
  public BasePhaseLoader(String classTag) {
    super();
    this.classTag = classTag;
  }

  private static final String CROSS_PARAMS_KEY = "cross-opts";
  
  public AnalysisEngine[] loadOptions(AnalysisEngineDescription[] aeds) {
    List<AnalysisEngine> aes = new ArrayList<AnalysisEngine>();
    for (AnalysisEngineDescription aeDesc : aeds) {
      try {
        aes.add(AnalysisEngineFactory.createAggregate(aeDesc));
      } catch (ResourceInitializationException e) {
        e.printStackTrace();
      }
    }
    return aes.toArray(new AnalysisEngine[0]);
  }

  public AnalysisEngineDescription[] loadOptionDescriptions(String options) {
    List<AnalysisEngineDescription> aeds = loadOptions(options);
    return aeds.toArray(new AnalysisEngineDescription[0]);
  }

  List<AnalysisEngineDescription> loadOptions(String options) {
    List<AnalysisEngineDescription> aeds = new ArrayList<AnalysisEngineDescription>();
    Yaml yaml = new Yaml();
    @SuppressWarnings("unchecked")
    List<Map<String, Object>> ao = (List<Map<String, Object>>) yaml.load(options);
    for (Map<String, Object> optionDescription : ao) {
      loadOption(optionDescription, aeds);
    }
    return aeds;
  }

  @SuppressWarnings("unchecked")
  void loadOption(Map<String, Object> description, List<AnalysisEngineDescription> aes) {
    try {
      Map.Entry<String, Object> first = getFirst(description);
      HandleType type = HandleType.getInstance(first.getKey());
      if (type == HandleType.PIPELINE) {
        List<Map<String, Object>> pipeline = (List<Map<String, Object>>) first.getValue();
        List<AnalysisEngineDescription> options = createInnerPipeline(pipeline);
        aes.addAll(options);
      } else {
        String resource = (String) first.getValue();
        List<AnalysisEngineDescription> options = doLoadOptions(ResourceHandle.newHandle(type,
                resource));
        aes.addAll(options);
      }
    } catch (Exception e) {
      System.err.printf("Unable to load option %s caused by:\n", description);
      Throwable cause = e.getCause();
      if (cause != null) {
        cause.printStackTrace();
      } else {
        e.printStackTrace();
      }
    }
  }

  @SuppressWarnings("unchecked")
  private List<AnalysisEngineDescription> createInnerPipeline(List<Map<String, Object>> pipeline)
          throws ResourceInitializationException, IOException {
    List<AnalysisEngineDescription> options = Lists.newArrayList();
    List<Set<AnalysisEngineDescription>> sets = Lists.newArrayList();
    for (Map<String, Object> map : pipeline) {
      try {
        Map.Entry<String, Object> me = getFirst(map);
        String type = me.getKey();
        Object o = me.getValue();
        if (o instanceof String) {
          String component = ((String) o).trim();
          ResourceHandle handle = ResourceHandle.newHandle(type, component);
          Set<AnalysisEngineDescription> local = Sets.newLinkedHashSet(doLoadOptions(handle));
          sets.add(local);
        } else if (o instanceof Iterable) {
          Iterable<Object> components = (Iterable<Object>) o;
          Set<AnalysisEngineDescription> local = Sets.newLinkedHashSet();
          for (Object o2 : components) {
            if (o2 instanceof Map) {
              Map<String, Object> component = (Map<String, Object>) o2;
              List<AnalysisEngineDescription> aes = new ArrayList<AnalysisEngineDescription>();
              loadOption(component, aes);
              local.addAll(aes);
            } else {
              throw new IllegalArgumentException(
                      "Illegal experiment descriptor, all options must be specified as a pair 'key: value'");
            }
          }
          sets.add(local);
        } else {
          throw new IllegalArgumentException(
                  "Illegal experiment descriptor, must contain either an iterable or a string");
        }
      } catch (Exception e) {
        System.err.printf("Unable to load option %s caused by:\n", map);
        Throwable cause = e.getCause();
        if (cause != null) {
          cause.printStackTrace();
        } else {
          e.printStackTrace();
        }
      }
    }
    // AED equality is based on equality of the MetaDataObject attributes
    Set<List<AnalysisEngineDescription>> product = Sets.cartesianProduct(sets);
    for (List<AnalysisEngineDescription> local : product) {
      AggregateBuilder builder = new AggregateBuilder();
      List<String> names = Lists.newArrayList();
      for (AnalysisEngineDescription aeDesc : local) {
        builder.add(aeDesc);
        names.add(aeDesc.getAnalysisEngineMetaData().getName());
      }
      String aeName = Joiner.on(";").join(names);
      AnalysisEngineDescription aee = builder.createAggregateDescription();
      aee.getAnalysisEngineMetaData().setName(String.format("pipeline:(%s)", aeName));
      options.add(aee);
    }
    return options;
  }

  private List<AnalysisEngineDescription> doLoadOptions(ResourceHandle handle) throws Exception {
    List<AnalysisEngineDescription> aes = Lists.newArrayList();
    Map<String, Object> tuples = Maps.newLinkedHashMap();
    Class<? extends AnalysisComponent> comp = BaseExperimentBuilder.loadFromClassOrInherit(handle,
            AnalysisComponent.class, tuples, classTag);
    AnyObject crossParams = (AnyObject) tuples.remove(CROSS_PARAMS_KEY);
    if (crossParams == null) {
      AnalysisEngineDescription aeDesc = BaseExperimentBuilder.createAnalysisEngineDescription(
              tuples, comp);
      aes.add(aeDesc);
    } else {
      List<String> paramNames = getParameterNames(crossParams);
      Set<List<Object>> product = doCartesianProduct(crossParams);
      for (List<Object> configuration : product) {
        Map<String, Object> inner = Maps.newLinkedHashMap(tuples);
        setInnerParams(paramNames, configuration, inner);
        AnalysisEngineDescription aeDesc = BaseExperimentBuilder.createAnalysisEngineDescription(
                inner, comp);
        aes.add(aeDesc);
      }
    }
    return aes;
  }

  private List<String> getParameterNames(AnyObject crossParams) {
    List<String> names = Lists.newArrayList(); // parameter names
    for (AnyTuple tuple : crossParams.getTuples()) {
      String key = tuple.getKey();
      names.add(key);
    }
    return names;
  }

  private Set<List<Object>> doCartesianProduct(AnyObject crossParams) {
    List<Set<Object>> sets = Lists.newArrayList(); // input parameters
    List<String> names = Lists.newArrayList(); // parameter names
    for (AnyTuple tuple : crossParams.getTuples()) {
      Set<Object> params = Sets.newHashSet();
      String key = tuple.getKey();
      names.add(key);
      @SuppressWarnings("unchecked")
      Iterable<Object> values = (Iterable<Object>) tuple.getObject();
      for (Object value : values) {
        params.add(value);
      }
      sets.add(params);
    }
    Set<List<Object>> product = Sets.cartesianProduct(sets);
    return product;
  }

  private void setInnerParams(List<String> paramNames, List<Object> configuration,
          Map<String, Object> inner) {
    for (int i = 0; i < paramNames.size(); i++) {
      String key = paramNames.get(i);
      Object value = configuration.get(i);
      inner.put(key, value);
    }
  }

  private Map.Entry<String, Object> getFirst(Map<String, Object> map) {
    return Iterators.get(map.entrySet().iterator(), 0);
  }
}
