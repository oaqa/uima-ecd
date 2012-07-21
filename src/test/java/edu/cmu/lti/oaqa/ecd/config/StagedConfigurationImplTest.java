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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import mx.bigdata.anyobject.AnyObject;
import mx.bigdata.anyobject.AnyTuple;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class StagedConfigurationImplTest {
  
  @Test
  public void stagedPipelineIsValid() throws IOException {
    AnyObject conf = ConfigurationLoader.load("test.staged-ecd-example"); 
    StagedConfigurationImpl staged = new StagedConfigurationImpl(conf);
    Iterator<Stage> it = staged.iterator();
    List<Stage> list = Lists.newArrayList(it);
    assertThat(list.size(), is(equalTo(2)));
  }
  
  @Test
  public void stagedConfigsAreCorrect() throws IOException {
    AnyObject conf = ConfigurationLoader.load("test.staged-ecd-example"); 
    StagedConfigurationImpl staged = new StagedConfigurationImpl(conf);
    int[] sizes = new int[] {4,6};
    int i = 0;
    for (Stage stage : staged) {
      validateConfiguration(stage.getConfiguration(), sizes[i++]);
    }
  }
  
  @Test
  public void nonStagedPipelineIsValid() throws IOException {
    AnyObject conf = ConfigurationLoader.load("test.nonstaged-ecd-example"); 
    StagedConfigurationImpl staged = new StagedConfigurationImpl(conf);
    Iterator<Stage> it = staged.iterator();
    List<Stage> list = Lists.newArrayList(it);
    assertThat(list.size(), is(equalTo(1))); 
  }

  @Test
  public void nonStagedConfigIsCorrect() throws IOException {
    AnyObject conf = ConfigurationLoader.load("test.nonstaged-ecd-example"); 
    StagedConfigurationImpl staged = new StagedConfigurationImpl(conf);
    int[] sizes = new int[] {5};
    int i = 0;
    for (Stage stage : staged) {
      validateConfiguration(stage.getConfiguration(), sizes[i++]);
    }
  }
  
  private void validateConfiguration(AnyObject config, int pipelineSize) {
    Set<String> keys = Sets.newHashSet();
    for (AnyTuple tuple : config.getTuples()) {
      keys.add(tuple.getKey());
    }
    assertThat(keys.size(), is(equalTo(5)));
    assertThat(keys, hasItems("experiment", "configuration", "collection-reader", "pipeline", "post-process"));
    List<Object> pipeline = Lists.newArrayList(config.getIterable("pipeline"));
    assertThat(pipeline.size(), is(equalTo(pipelineSize)));
  }
  
}
