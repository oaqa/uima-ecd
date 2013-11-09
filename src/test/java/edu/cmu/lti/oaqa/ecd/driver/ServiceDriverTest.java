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

import static org.junit.Assert.assertNotNull;

import org.apache.uima.adapter.jms.activemq.SpringContainerDeployer;
import org.junit.Ignore;

public class ServiceDriverTest {

  @Ignore
  public void testRemotePipeline() throws Exception {
    SpringContainerDeployer serviceDeployer = startService(new String[] { "test.service.pipeline-service-example" });
    assertNotNull(serviceDeployer);
    DriverTest.testPipeline("test.service.pipeline-service-client-test-example", new long[] { 2 });
    serviceDeployer.undeploy(SpringContainerDeployer.STOP_NOW);
  }

  SpringContainerDeployer startService(String[] resources) throws Exception {
    ECDServiceDriver service = new ECDServiceDriver();
    String contextFiles[] = service.initialize(resources);
    if (contextFiles == null) {
      return null;
    }
    SpringContainerDeployer serviceDeployer = service.deploy(contextFiles);
    if (serviceDeployer == null) {
      return null;
    }
    return serviceDeployer;
  }
}
