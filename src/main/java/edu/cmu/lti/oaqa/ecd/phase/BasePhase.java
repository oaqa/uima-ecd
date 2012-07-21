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

import java.io.IOException;
import java.sql.SQLException;

import org.apache.uima.jcas.JCas;
import org.xml.sax.SAXException;

public class BasePhase extends AbstractPhase {

  @Override
  protected void insertExecutionTrace(String optionId, int sequenceId, String dataset,
          Integer phaseNo2, String uuid, long startTime, String hostName, String trace, String key)
          throws IOException {
    // TODO Auto-generated method stub

  }

  @Override
  protected void storeCas(byte[] bytes, ExecutionStatus success, long endTime, String key)
          throws IOException {
    // TODO Auto-generated method stub

  }

  @Override
  protected void storeException(byte[] bytes, ExecutionStatus failure, long endTime, String key)
          throws IOException, SAXException {
    // TODO Auto-generated method stub

  }

  @Override
  protected CasDeserializer deserialize(JCas jcas, String hash) throws SQLException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected void insertExperimentMeta(String experimentId, int phaseNo, int stageId, int size) {
    // TODO Auto-generated method stub

  }

}
