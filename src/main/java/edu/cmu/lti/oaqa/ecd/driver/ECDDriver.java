package edu.cmu.lti.oaqa.ecd.driver;

import java.io.IOException;
import java.util.UUID;

import mx.bigdata.anyobject.AnyObject;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.collection.CollectionReader;
import org.apache.uima.resource.metadata.TypeSystemDescription;
import org.uimafit.factory.TypeSystemDescriptionFactory;

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

  public ECDDriver(String resource, String uuid) throws Exception {
    TypeSystemDescription typeSystem = TypeSystemDescriptionFactory.createTypeSystemDescription();
    this.builder = new BaseExperimentBuilder(uuid, resource, typeSystem);
    this.config = builder.getConfiguration();
  }

  public void run() throws Exception {
    runLocal();
  }

  private void runLocal() throws Exception, UIMAException, IOException {
    StagedConfiguration stagedConfig = new StagedConfigurationImpl(config);
    ProcessingStrategy ps = new DefaultProcessingStrategy();
    for (Stage stage : stagedConfig) {
      FunneledFlow funnel = ps.newFunnelStrategy(builder.getExperimentUuid());
      AnyObject conf = stage.getConfiguration();
      CollectionReader reader = builder.buildCollectionReader(conf, stage.getId());
      AnalysisEngine pipeline = builder.buildPipeline(conf, "pipeline", stage.getId(), funnel);
      AnalysisEngine post = builder.buildPipeline(conf, "post-process", stage.getId());
      SimplePipelineRev803.runPipeline(reader, pipeline, post);
    }
  }

  public static void main(String[] args) throws Exception {
    String uuid = UUID.randomUUID().toString();
    System.out.println("Experiment UUID: " + uuid);
    ECDDriver driver = new ECDDriver(args[0], uuid);
    driver.run();
  }
}