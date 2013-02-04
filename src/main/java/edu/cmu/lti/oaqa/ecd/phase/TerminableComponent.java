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

import java.util.Set;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

import com.google.common.collect.Sets;
import com.google.common.eventbus.Subscribe;

import edu.cmu.lti.oaqa.ecd.phase.event.PhaseEventBus;
import edu.cmu.lti.oaqa.ecd.phase.event.TerminateEvent;
import edu.cmu.lti.oaqa.ecd.util.CasUtils;
import edu.cmu.lti.oaqa.framework.types.CurrentExecution;

public abstract class TerminableComponent extends JCasAnnotator_ImplBase {

	private String terminatedIdHash;

	private String executionIdHash;

	@Override
	public void initialize(UimaContext c)
			throws ResourceInitializationException {
		PhaseEventBus.registerForTerminateEvent(this);
	}

	@Subscribe
	public void terminate(TerminateEvent event) {
		String eventIdHash = event.getIdHash();
		if (eventIdHash.equals(executionIdHash)) {
			terminatedIdHash = executionIdHash;
		}
	}

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		CurrentExecution ce = CasUtils.getLast(jcas, CurrentExecution.class);
		this.executionIdHash = ce.getIdHash();
	}

	/**
	 * Checks if the component is still alive, throws an IllegalStateException
	 * otherwise. Components usually call this method on the process method to
	 * find if they should keep processing.
	 */
	protected void testAliveness() {
		if (executionIdHash.equals(terminatedIdHash)) {
			throw new IllegalStateException("Component already terminated");
		}
	}

}