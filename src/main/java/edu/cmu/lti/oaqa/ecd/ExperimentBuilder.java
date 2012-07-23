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

import mx.bigdata.anyobject.AnyObject;

import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.metadata.FixedFlow;
import org.apache.uima.collection.CollectionReader;
import org.apache.uima.resource.Resource;

public interface ExperimentBuilder {

  String getExperimentUuid();

  CollectionReader buildCollectionReader(AnyObject configuration, int id) throws Exception;

  AnalysisEngine buildPipeline(AnyObject configuration, String string, int id) throws Exception;

  AnalysisEngine buildPipeline(AnyObject configuration, String string, int id, FixedFlow funnel) throws Exception;

  AnalysisEngine buildPipeline(AnyObject config, String string, int i, FixedFlow object, boolean b) throws Exception;

  AnalysisEngine createNoOpEngine() throws Exception;

  <T extends Resource> T initializeResource(AnyObject config, String node, Class<T> type) throws Exception;

  AnyObject getConfiguration();
}
