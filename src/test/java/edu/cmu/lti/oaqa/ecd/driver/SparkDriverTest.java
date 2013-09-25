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


import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.UUID;

import org.junit.Test;

public class SparkDriverTest {
 
  @Test
  public void testNonStagedPipeline() throws Exception {
    testPipeline("test.partial-nonstaged-ecd-example", new long[] {2});
  }

  @Test
  public void testStagedPipeline() throws Exception {
    testPipeline("test.partial-ecd-example", new long[] {2, 2});
  }
  
  void testPipeline(String resource, long[] sizes) throws Exception {
    String uuid = UUID.randomUUID().toString();
    System.out.println("Experiment UUID: " + uuid);
    SparkDriver driver = new SparkDriver(resource, uuid);
    driver.run("local[2]");
    Iterable<Long> elements = driver.getProcessedItems();
    int i = 0;
    for (Long total : elements) {
      assertThat(total, is(equalTo(sizes[i++])));
    }
  }

}
