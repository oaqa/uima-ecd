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

package edu.cmu.lti.oaqa.ecd;

import java.util.UUID;

import mx.bigdata.anyobject.AnyObject;

import org.apache.uima.resource.metadata.TypeSystemDescription;
import org.junit.Test;
import org.uimafit.factory.TypeSystemDescriptionFactory;

import edu.cmu.lti.oaqa.ecd.config.ConfigurationLoader;
import edu.cmu.lti.oaqa.ecd.config.Stage;
import edu.cmu.lti.oaqa.ecd.config.StagedConfigurationImpl;

public class ExperimentBuilderTest {
 
  private static ExperimentBuilder setupExperimentBuilder(AnyObject conf, String resource) throws Exception {
    String uuid = UUID.randomUUID().toString();
    TypeSystemDescription typeSystem = TypeSystemDescriptionFactory.createTypeSystemDescription();
    return new BaseExperimentBuilder(uuid, typeSystem);
  }

  @Test
  public void testSetupNonStagedPipeline() throws Exception {
    testPipelineSetup("test.nonstaged-ecd-example");
  }

  @Test
  public void testSetupStagedPipeline() throws Exception {
    testPipelineSetup("test.partial-ecd-example");
  }
  
  void testPipelineSetup(String resource) throws Exception {
    AnyObject conf = ConfigurationLoader.load(resource); 
    ExperimentBuilder builder = setupExperimentBuilder(conf, resource);
    StagedConfigurationImpl staged = new StagedConfigurationImpl(conf);
    Stage stage = staged.iterator().next();
    builder.buildPipeline(stage.getConfiguration(), "pipeline",
            stage.getId(), null);
  }

}
