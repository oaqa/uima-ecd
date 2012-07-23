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

package edu.cmu.lti.oaqa.ecd.driver;

import java.util.List;
import java.util.UUID;

import mx.bigdata.anyobject.AnyObject;

import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.collection.CollectionReader;
import org.apache.uima.resource.metadata.TypeSystemDescription;
import org.apache.uima.util.Progress;
import org.uimafit.factory.TypeSystemDescriptionFactory;

import com.google.common.collect.Lists;

import edu.cmu.lti.oaqa.ecd.BaseExperimentBuilder;
import edu.cmu.lti.oaqa.ecd.ExperimentBuilder;
import edu.cmu.lti.oaqa.ecd.SimplePipelineRev803;
import edu.cmu.lti.oaqa.ecd.config.FunneledFlow;
import edu.cmu.lti.oaqa.ecd.config.ProcessingStrategy;
import edu.cmu.lti.oaqa.ecd.config.Stage;
import edu.cmu.lti.oaqa.ecd.config.StagedConfiguration;
import edu.cmu.lti.oaqa.ecd.config.StagedConfigurationImpl;

public final class ECDDriver {

  private final ExperimentBuilder builder;

  private final AnyObject config;

  private final List<Long> processedItems = Lists.newArrayList();
  
  public ECDDriver(String resource, String uuid) throws Exception {
    TypeSystemDescription typeSystem = TypeSystemDescriptionFactory.createTypeSystemDescription();
    this.builder = new BaseExperimentBuilder(uuid, resource, typeSystem);
    this.config = builder.getConfiguration();
  }


  void run() throws Exception {
    StagedConfiguration stagedConfig = new StagedConfigurationImpl(config);
    ProcessingStrategy ps = new DefaultProcessingStrategy();
    for (Stage stage : stagedConfig) {
      FunneledFlow funnel = ps.newFunnelStrategy(builder.getExperimentUuid());
      AnyObject conf = stage.getConfiguration();
      CollectionReader reader = builder.buildCollectionReader(conf, stage.getId());
      AnalysisEngine pipeline = builder.buildPipeline(conf, "pipeline", stage.getId(), funnel);
      if (conf.getIterable("post-process") != null) {
        AnalysisEngine post = builder.buildPipeline(conf, "post-process", stage.getId());
        SimplePipelineRev803.runPipeline(reader, pipeline, post);
      } else {
        SimplePipelineRev803.runPipeline(reader, pipeline);
      }
      Progress progress = reader.getProgress()[0];
      long total = progress.getCompleted();
      processedItems.add(total);
    }
  }
  
  Iterable<Long> getProcessedItems() {
    return processedItems;
  }

  public static void main(String[] args) throws Exception {
    String uuid = UUID.randomUUID().toString();
    System.out.println("Experiment UUID: " + uuid);
    ECDDriver driver = new ECDDriver(args[0], uuid);
    driver.run();
  }
}