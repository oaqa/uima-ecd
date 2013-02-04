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
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.metadata.TypeSystemDescription;
import org.apache.uima.util.Progress;
import org.uimafit.factory.TypeSystemDescriptionFactory;

import com.google.common.collect.Lists;

import edu.cmu.lti.oaqa.ecd.BaseExperimentBuilder;
import edu.cmu.lti.oaqa.ecd.ExperimentBuilder;
import edu.cmu.lti.oaqa.ecd.config.Stage;
import edu.cmu.lti.oaqa.ecd.config.StagedConfiguration;
import edu.cmu.lti.oaqa.ecd.config.StagedConfigurationImpl;
import edu.cmu.lti.oaqa.ecd.flow.FunneledFlow;
import edu.cmu.lti.oaqa.ecd.flow.strategy.FunnelingStrategy;
import edu.cmu.lti.oaqa.ecd.impl.DefaultFunnelingStrategy;

public final class ECDDriver {

	private final ExperimentBuilder builder;
	private final AnyObject config;
	private final List<Long> processedItems = Lists.newArrayList();

	/**
	 * Constructor.
	 * @param resource path to YAML descriptor
	 * @param uuid Unique experiment ID
	 * @throws Exception
	 */
	public ECDDriver(String resource, String uuid) throws Exception {
		// Create new (empty?) type system description
		TypeSystemDescription typeSystem = TypeSystemDescriptionFactory
				.createTypeSystemDescription();
		// Instantiate a experiment builder with a unique experiment ID
		// and a given YAML file ('resource')
		this.builder = new BaseExperimentBuilder(uuid, resource, typeSystem);
		this.config = builder.getConfiguration();
	}

	/**
	 * Run the experiment!
	 * @throws Exception
	 */
	public void run() throws Exception {
		// Create a new Stage config from the YAML config object
		StagedConfiguration stagedConfig = new StagedConfigurationImpl(config);
		FunnelingStrategy ps = getProcessingStrategy();
		for (Stage stage : stagedConfig) {
			FunneledFlow funnel = ps.newFunnelStrategy(builder
					.getExperimentUuid());
			AnyObject conf = stage.getConfiguration();
			CollectionReader reader = builder.buildCollectionReader(conf,
					stage.getId());
			AnalysisEngine pipeline = builder.buildPipeline(conf, "pipeline",
					stage.getId(), funnel);
			if (conf.getIterable("post-process") != null) {
				AnalysisEngine post = builder.buildPipeline(conf,
						"post-process", stage.getId());
				SimplePipelineRev803.runPipeline(reader, pipeline, post);
			} else {
				SimplePipelineRev803.runPipeline(reader, pipeline);
			}
			Progress progress = reader.getProgress()[0];
			long total = progress.getCompleted();
			processedItems.add(total);
		}
	}

	/**
	 * 
	 * @return
	 * @throws ResourceInitializationException
	 */
	private FunnelingStrategy getProcessingStrategy()
			throws ResourceInitializationException {
		// Create an 'empty' Funneling Strategy
		FunnelingStrategy ps = new DefaultFunnelingStrategy();
		// 
		AnyObject map = config.getAnyObject("processing-strategy");
		if (map != null) {
			ps = BaseExperimentBuilder.loadProvider(map,
					FunnelingStrategy.class);
		}
		return ps;
	}

	Iterable<Long> getProcessedItems() {
		return processedItems;
	}

	/**
	 * This is what runs when we do an experiment!
	 * @param args command line arguments
	 * 	path to YAML descriptor
	 * 	optional experiment ID
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		// Generate ID string for experiment
		String uuid = UUID.randomUUID().toString();
		// If no ID supplied, generate ID
		if (args.length > 1) {
			uuid = args[1];
		}
		System.out.println("Experiment UUID: " + uuid);
		// Instantiate driver with path to YAML descriptor (?) 
		// and experiment ID
		ECDDriver driver = new ECDDriver(args[0], uuid);
		// Run experiment!
		driver.run();
	}
}