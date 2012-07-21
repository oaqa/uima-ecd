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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.UUID;

import mx.bigdata.anyobject.AnyObject;

import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.resource.metadata.TypeSystemDescription;
import org.junit.Test;
import org.uimafit.factory.TypeSystemDescriptionFactory;

import edu.cmu.lti.oaqa.ecd.AbstractExperimentBuilder;
import edu.cmu.lti.oaqa.ecd.BaseExperimentBuilder;
import edu.cmu.lti.oaqa.ecd.config.ConfigurationLoader;
import edu.cmu.lti.oaqa.ecd.config.Stage;
import edu.cmu.lti.oaqa.ecd.config.StagedConfigurationImpl;

public class BasePhaseTest {

  
  @Test
  public void testPhaseAllocation() throws Exception {
    String resource = "test.base-phase-test";
    int[] sizes = new int[] {4, 4, 6, 6, 7, 8, 13};
    testPipeline(resource, sizes);
  }  
  
  @Test
  public void crosOptsAllocation() throws Exception {
    String resource = "test.cross-opts-test";
    int[] sizes = new int[] {25, 37, 85, 31, 61};
    testPipeline(resource, sizes);
  }
  
  @Test
  public void testStagedPhaseAllocation() throws Exception {
    String resource = "test.partial-ecd-example";
    int[] sizes = new int[] {3, 4, 3};
    testPipeline(resource, sizes);
  }
  
  private void testPipeline(String resource, int[] sizes) throws Exception {
    AnyObject conf = ConfigurationLoader.load(resource);    
    String uuid = UUID.randomUUID().toString();
    TypeSystemDescription typeSystem = TypeSystemDescriptionFactory.createTypeSystemDescription();
    AbstractExperimentBuilder builder = new BaseExperimentBuilder(uuid, typeSystem);
    StagedConfigurationImpl staged = new StagedConfigurationImpl(conf);
    Stage stage = staged.iterator().next();
    AnyObject stageConf = stage.getConfiguration();
    Iterable<AnyObject> phases = stageConf.getIterable("pipeline");
    int i = 0;
    for (AnyObject phaseDescription : phases) {
      AnalysisEngineDescription aed = builder.buildComponent(stage.getId(), i + 1,
          phaseDescription);
      if (aed.getImplementationName().equalsIgnoreCase(
          AbstractPhase.class.getName())) {
        AbstractPhase bp = new BasePhase();
        String optDescr = (String) aed.getAnalysisEngineMetaData()
                .getConfigurationParameterSettings().getParameterValue("options");
        List<AnalysisEngineDescription> options = bp.loadOptions(optDescr);
        assertThat(options.size(), is(equalTo(sizes[i++])));
      }
    }
  }

//  public UimaContext createUimaContext(AnalysisEngineDescription aed)
//      throws ResourceInitializationException {
//    UimaContextAdmin context = UIMAFramework.newUimaContext(
//        UIMAFramework.getLogger(), UIMAFramework.newDefaultResourceManager(),
//        UIMAFramework.newConfigurationManager());
//    ConfigurationManager cfgMgr = context.getConfigurationManager();
//    cfgMgr.setSession(context.getSession());
//    NameValuePair[] parameters = aed.getAnalysisEngineMetaData()
//        .getConfigurationParameterSettings().getParameterSettings();
//    for (NameValuePair p : parameters) {
//      cfgMgr.setConfigParameterValue(
//          context.getQualifiedContextName() + p.getName(), p.getValue());
//    }
//    return context;
//  }

}
