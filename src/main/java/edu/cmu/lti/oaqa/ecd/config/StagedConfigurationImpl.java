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

package edu.cmu.lti.oaqa.ecd.config;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import mx.bigdata.anyobject.AnyObject;
import mx.bigdata.anyobject.AnyTuple;
import mx.bigdata.anyobject.MapBasedAnyObject;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class StagedConfigurationImpl implements StagedConfiguration {

  private enum Scope {
    STAGE, GLOBAL
  };

  private static final String STAGES_NODE_NAME = "stages";

  private static final String SCOPE_NODE_NAME = "scope";

  private final AnyObject original;

  public StagedConfigurationImpl(AnyObject config) {
    this.original = config;
  }

  @Override
  public Iterator<Stage> iterator() {
    Iterable<AnyObject> stages = original.getIterable(STAGES_NODE_NAME);
    if (stages == null) {
      return Iterators.singletonIterator(new Stage(1, original));
    } else {
      return buildStagedConfigs(stages);
    }
  }

  Iterator<Stage> buildStagedConfigs(Iterable<AnyObject> stages) {
    List<Stage> confs = Lists.newArrayList();
    List<Object> pipeline = Lists.newArrayList();
    int count = 1;
    for (AnyObject stage : stages) {
      Map<String, Object> conf = Maps.newLinkedHashMap();
      for (AnyTuple tuple : original.getTuples()) {
        if (!tuple.getKey().equals(STAGES_NODE_NAME)) {
          conf.put(tuple.getKey(), tuple.getObject());
        }
      }
      List<Object> inner = Lists.newArrayList(pipeline);
      for (Object o : stage.getIterable("pipeline")) {
        inner.add(o);
        AnyObject component = (AnyObject) o;
        String scope = component.getString(SCOPE_NODE_NAME);
        addComponentIfInGlobalScope(o, pipeline, scope);
      }
      conf.put("pipeline", inner);
      conf.put("post-process", stage.getIterable("post-process"));
      confs.add(new Stage(count, new MapBasedAnyObject(conf)));
      count++;
    }
    return confs.iterator();
  }

  private void addComponentIfInGlobalScope(Object o, List<Object> pipeline, String scopeName) {
    if (scopeName == null) {
      pipeline.add(o);
    } else {
      Scope scope = Scope.valueOf(scopeName);
      if (scope != Scope.STAGE) {
        pipeline.add(o);
      }
    }
  }

}
