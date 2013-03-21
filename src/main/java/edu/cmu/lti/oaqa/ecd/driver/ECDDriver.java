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

import java.lang.reflect.Array;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import mx.bigdata.anyobject.AnyObject;

import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.metadata.FixedFlow;
import org.apache.uima.collection.CollectionReader;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.metadata.TypeSystemDescription;
import org.apache.uima.util.Progress;
import org.uimafit.factory.TypeSystemDescriptionFactory;

import com.google.common.collect.Lists;

import edu.cmu.lti.oaqa.ecd.BaseExperimentBuilder;
import edu.cmu.lti.oaqa.ecd.ExperimentBuilder;
import edu.cmu.lti.oaqa.ecd.ExperimentPersistenceProvider;
import edu.cmu.lti.oaqa.ecd.GroupExperimentPersistenceProvider;
import edu.cmu.lti.oaqa.ecd.collection.KnownSizeCollectionReader;
import edu.cmu.lti.oaqa.ecd.config.ConfigurationLoader;
import edu.cmu.lti.oaqa.ecd.config.Stage;
import edu.cmu.lti.oaqa.ecd.config.StagedConfiguration;
import edu.cmu.lti.oaqa.ecd.config.StagedConfigurationImpl;
import edu.cmu.lti.oaqa.ecd.flow.FunneledFlow;
import edu.cmu.lti.oaqa.ecd.flow.strategy.FunnelingStrategy;
import edu.cmu.lti.oaqa.ecd.impl.DefaultExperimentPersistenceProvider;
import edu.cmu.lti.oaqa.ecd.impl.DefaultFunnelingStrategy;
import edu.cmu.lti.oaqa.ecd.impl.DefaultGroupExperimentPersistenceProvider;

public final class ECDDriver {

  private static final int TEST_FLAG = 1;
  private static final int TRAIN_FLAG = 0;
  
  public static final String TRAIN_CLASS = "train-class";
  public static final String TEST_CLASS = "class";
  
  public static final String TRAIN_CONFIG = "train-config";

  private final ExperimentBuilder builder;

  private final AnyObject config;

  private final List<Long> processedItems = Lists.newArrayList();
  private TypeSystemDescription typeSystem;
  private String resource;
  private DefaultGroupExperimentPersistenceProvider persistence;
  
  public ECDDriver(String resource, String uuid) throws Exception {
    typeSystem = TypeSystemDescriptionFactory.createTypeSystemDescription();
    this.resource = resource;
    this.builder = new BaseExperimentBuilder(uuid, resource, typeSystem);
    this.config = builder.getConfiguration();
  }
  
  

  public void run() throws Exception {
    StagedConfiguration stagedConfig = new StagedConfigurationImpl(config);
    FunnelingStrategy ps = getProcessingStrategy();
    for (Stage stage : stagedConfig) {
      AnyObject conf = stage.getConfiguration();
      AnyObject trainConf =  conf.getAnyObject(TRAIN_CONFIG);
      if(trainConf == null){
        singleRun(conf, stage, ps);
      }else{
        splitRun(conf, stage, ps);
      }
    }
  }
  
  private void singleRun(AnyObject conf,  Stage stage, FunnelingStrategy ps) throws Exception{
    FunneledFlow funnel = ps.newFunnelStrategy(builder.getExperimentUuid());
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
  
  private void splitRun(AnyObject conf,  Stage stage, FunnelingStrategy ps) throws Exception{
  
    AnyObject trainConf =  conf.getAnyObject(TRAIN_CONFIG);
    Double testRatio = trainConf.getDouble("test-ratio", 0.5);
    Boolean shuffle = trainConf.getBoolean("shuffle", false);
    Integer fold = trainConf.getInteger("crossvalidation-fold", 1);
    
    System.out.println(MessageFormat.format("TRAIN_CONFIG:trainRatio:{0} shuffle:{1} fold:{2}", testRatio, shuffle, fold));
    
    KnownSizeCollectionReader reader = (KnownSizeCollectionReader) builder.buildCollectionReader(conf, stage.getId());
    int totalSize = reader.size();
    int[] posMapping = createPositionMapping(totalSize, shuffle);
    int testSize;
    if(fold == 1){
      testSize = (int) (testRatio*totalSize);
    }else{
      testSize = totalSize/fold;
    }
    
    persistence = newGroupPersistenceProvider(config);
    
    for (int i = 0; i < fold; i++) {
      System.out.println("Fold "+i);
      int offset = (int) (totalSize*i/fold);
      int[] pipelineAssignments = createPipelineAssignments(posMapping, offset, testSize);    
      singleRun(String.valueOf(fold), pipelineAssignments, conf, stage, ps, TRAIN_CLASS, TRAIN_FLAG);
      singleRun(String.valueOf(fold), pipelineAssignments, conf, stage, ps, TEST_CLASS, TEST_FLAG);
    }
  }

  private void singleRun(String fold, int[] pipelineAssignments, AnyObject conf, Stage stage, FunnelingStrategy ps, String classTag, int assignmentID) throws Exception{
    BaseExperimentBuilder foldExpBuilder = new BaseExperimentBuilder(UUID.randomUUID().toString(), resource, typeSystem);
    insertGroupExperiment(fold, foldExpBuilder.getExperimentUuid(), pipelineAssignments);
    FunneledFlow funnel = ps.newFunnelStrategy(foldExpBuilder.getExperimentUuid());
    KnownSizeCollectionReader reader = (KnownSizeCollectionReader) foldExpBuilder.buildCollectionReader(conf, stage.getId());
    AnalysisEngine[] pipeline = createPipeline(foldExpBuilder, conf, stage, funnel, classTag);
    System.out.println(classTag+":");
    long total = SimplePipelineRev803.runPipelineWithinDuty(reader, pipelineAssignments, assignmentID, pipeline);
    processedItems.add(total);
  }
  
  
  private DefaultGroupExperimentPersistenceProvider newGroupPersistenceProvider(AnyObject config)
          throws ResourceInitializationException {
    AnyObject pprovider = config.getAnyObject("group-persistence-provider");
    if (pprovider == null) {
      return new DefaultGroupExperimentPersistenceProvider();
    }
    try {
      return (DefaultGroupExperimentPersistenceProvider) builder.initializeResource(config,
              "group-persistence-provider", GroupExperimentPersistenceProvider.class);
    } catch (Exception e) {
      throw new ResourceInitializationException(
              ResourceInitializationException.ERROR_INITIALIZING_FROM_DESCRIPTOR, new Object[] {
                  "group-persistence-provider", config }, e);
    }
  }
  
  private void insertGroupExperiment(String fold, String foldUuid, int[] pipelineAssignments) throws Exception {
    AnyObject experiment = config.getAnyObject("configuration");
    String name = experiment.getString("name");
    String author = experiment.getString("author");
    
    StringBuffer testIdxs = new StringBuffer();
    for (int i = 0; i < pipelineAssignments.length; i++) {
      if(pipelineAssignments[i] == TEST_FLAG){
        testIdxs.append(i).append(",");
      }
    }
    persistence.insertGroupExperiment(builder.getExperimentUuid(), fold, foldUuid, name, author,
            ConfigurationLoader.getString(resource), resource, testIdxs.toString());
  }
  
  private AnalysisEngine[] createPipeline(BaseExperimentBuilder foldExpBuilder, AnyObject conf, Stage stage, FixedFlow funnel, String classTag) throws Exception{
    AnalysisEngine pipeline = foldExpBuilder.buildPipeline(conf, "pipeline", stage.getId(), funnel, false, classTag);
    if (conf.getIterable("post-process") != null) {
      AnalysisEngine post = foldExpBuilder.buildPipeline(conf, "post-process", stage.getId(), null, false, classTag);
      return new AnalysisEngine[]{pipeline, post};
    } else {
      return new AnalysisEngine[]{pipeline};
    }
  }
  
  private int[] createPipelineAssignments(int[] posMapping, int testStart, int testSize) {
    int[] assignments = new int[posMapping.length];
    for (int i = 0; i < posMapping.length; i++) {
      if(i>=testStart && i<testStart+testSize){
        assignments[posMapping[i]] = TEST_FLAG;
      }
    }
    return assignments;
  }

  private int[] createPositionMapping(int totalSize, boolean shuffle) {
    Random rgen = new Random(0); // XXX constant random seed
    int[] posMapping = new int[totalSize];

    for (int i = 0; i < totalSize; i++) {
      posMapping[i] = i;
    }

    if (shuffle) {
      for (int i = 0; i < totalSize; i++) {
        int randomPosition = rgen.nextInt(totalSize);
        int temp = posMapping[i];
        posMapping[i] = posMapping[randomPosition];
        posMapping[randomPosition] = temp;
      }
    }
    return posMapping;
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
    if (args.length > 1) {
      uuid = args[1];
    }
    System.out.println("Experiment UUID: " + uuid);
    ECDDriver driver = new ECDDriver(args[0], uuid);
    driver.run();
  }
}