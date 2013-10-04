/*
 *  Copyright 2013 Carnegie Mellon University
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
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.collection.CollectionReader;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.metadata.TypeSystemDescription;
import org.apache.uima.util.Progress;
import org.apache.uima.fit.factory.TypeSystemDescriptionFactory;

import com.google.common.collect.Lists;

import edu.cmu.lti.oaqa.ecd.BaseExperimentBuilder;
import edu.cmu.lti.oaqa.ecd.ExperimentBuilder;
import edu.cmu.lti.oaqa.ecd.config.Stage;
import edu.cmu.lti.oaqa.ecd.config.StagedConfiguration;
import edu.cmu.lti.oaqa.ecd.config.StagedConfigurationImpl;
import edu.cmu.lti.oaqa.ecd.flow.FunneledFlow;
import edu.cmu.lti.oaqa.ecd.flow.strategy.FunnelingStrategy;
import edu.cmu.lti.oaqa.ecd.impl.DefaultFunnelingStrategy;

import org.apache.spark.api.java.*;

import edu.cmu.lti.suim.JavaSparkUima;
import edu.cmu.lti.suim.JavaSparkUima.PipelineFunction;
import edu.cmu.lti.suim.SCAS;

public final class SparkDriver {

  private final ExperimentBuilder builder;

  private final AnyObject config;

  private final List<Long> processedItems = Lists.newArrayList();
  
  public SparkDriver(String resource, String uuid) throws Exception {
    TypeSystemDescription typeSystem = TypeSystemDescriptionFactory.createTypeSystemDescription();
    this.builder = new BaseExperimentBuilder(uuid, resource, typeSystem);
    this.config = builder.getConfiguration();
  }

  public void run(String host) throws Exception {
    String cp = System.getenv("SPARK_CLASSPATH");
    JavaSparkContext sc = new JavaSparkContext(host, "SparkECDDriver",
                                               System.getenv("SPARK_HOME"), 
                                               cp != null ? cp.split(":") : new String[0]);
    StagedConfiguration stagedConfig = new StagedConfigurationImpl(config);
    FunnelingStrategy ps = getProcessingStrategy();
    for (Stage stage : stagedConfig) {
      FunneledFlow funnel = ps.newFunnelStrategy(builder.getExperimentUuid());
      AnyObject conf = stage.getConfiguration();
      CollectionReader reader = builder.buildCollectionReader(conf, stage.getId());
      JavaRDD<SCAS> rdd = JavaSparkUima.makeRDD(reader, sc);
      final AnalysisEngineDescription pipeline = 
        builder.buildPipelineDescription(conf, "pipeline", stage.getId(), funnel);
      if (conf.getIterable("post-process") != null) {
        final AnalysisEngineDescription post = 
          builder.buildPipelineDescription(conf, "post-process", stage.getId(), null);
        JavaRDD<SCAS> result = rdd.map(new PipelineFunction(pipeline, post));
      } else {
        JavaRDD<SCAS> result = rdd.map(new PipelineFunction(pipeline));
      }
      Progress progress = reader.getProgress()[0];
      long total = progress.getCompleted();
      processedItems.add(total);
    }
    sc.stop();
  }
  
  private FunnelingStrategy getProcessingStrategy() throws ResourceInitializationException {
    FunnelingStrategy ps = new DefaultFunnelingStrategy();
    AnyObject map = config.getAnyObject("processing-strategy");
    if (map != null) {
      ps = BaseExperimentBuilder.loadProvider(map, FunnelingStrategy.class);
    }
    return ps;
  }
  
  Iterable<Long> getProcessedItems() {
    return processedItems;
  }

  public static void main(String[] args) throws Exception {
    String uuid = UUID.randomUUID().toString();    
    if (args.length > 2) {
      uuid = args[2];
    }
    System.out.println("Experiment UUID: " + uuid);
    SparkDriver driver = new SparkDriver(args[0], uuid);
    driver.run(args[1]);
  }
}
