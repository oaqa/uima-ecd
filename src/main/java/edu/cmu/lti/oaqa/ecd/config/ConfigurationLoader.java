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

package edu.cmu.lti.oaqa.ecd.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import mx.bigdata.anyobject.AnyObject;
import mx.bigdata.anyobject.impl.SnakeYAMLLoader;

import com.google.common.io.CharStreams;

public class ConfigurationLoader {

	/**
	 * Converts YAML 'package' path to fully qualified path.
	 * @param resource path to YAML descriptor
	 * @param fullpath flag to return the entire file path
	 * @return full file path
	 */
	public static String getResourceLocation(String resource, boolean fullpath) {
		String parsed = resource.replace(".", "/");
		return (fullpath ? "/" : "") + parsed + ".yaml";
	}

	/**
	 * Load a YAML file and return object representation of the file
	 * in SnakeYaml
	 * @param resource path to YAML descriptor
	 * @return YAML file object
	 * @throws IOException
	 */
	public static AnyObject load(String resource) throws IOException {
		String resourceLocation = getResourceLocation(resource, true);
		// Reads YAML file as InputStream
		InputStream in = ConfigurationLoader.class
				.getResourceAsStream(resourceLocation);
		if (in == null) {
			throw new FileNotFoundException(resourceLocation
					+ " is not present on classpath");
		}
		// Parse YAML file, return AnyObject
		try {
			return SnakeYAMLLoader.getInstance().load(in);
		} finally {
			in.close();
		}
	}

	/**
	 * Dump the YAML file to String.
	 * @param resource path to the YAML file 
	 * @return Contents of the YAML file as a String
	 * @throws IOException
	 */
	public static String getString(String resource) throws IOException {
		// 
		String resourceLocation = ConfigurationLoader.getResourceLocation(
				resource, true);
		InputStream in = ConfigurationLoader.class
				.getResourceAsStream(resourceLocation);
		try {
			Reader reader = new InputStreamReader(in);
			return CharStreams.toString(reader);
		} finally {
			in.close();
		}
	}
}
