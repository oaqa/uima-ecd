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

package edu.cmu.lti.oaqa.ecd.impl;

import java.util.List;

import com.google.common.collect.Lists;

import edu.cmu.lti.oaqa.ecd.flow.FunneledFlow;
import edu.cmu.lti.oaqa.ecd.funnel.SetBasedFunnel;

/**
 * Funneling strategy consisting of empty Strings and an empty Set.
 */
public final class DefaultFunnelingStrategy extends AbstractFunnelingStrategy {

	@Override
	/**
	 * 
	 * @param experimentUuid Unique experiment ID
	 */
	public FunneledFlow newFunnelStrategy(String experimentUuid) {
		// TODO This creates a new empty ArrayList (?)
		List<String> matchingTraces = getMatchingTraces(experimentUuid);
		// TODO This is just an empty set!?
		return new SetBasedFunnel(matchingTraces);
	}

	/**
	 * Return and empty ArrayList of String's.
	 * @param experimentUuid unique experiment ID
	 * @return empty ArrayList of String's.
	 */
	private List<String> getMatchingTraces(String experimentUuid) {
		return Lists.<String> newArrayList();
	}
}
