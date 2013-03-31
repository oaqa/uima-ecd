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
import java.net.URL;

import mx.bigdata.anyobject.AnyObject;
import mx.bigdata.anyobject.impl.SnakeYAMLLoader;

import com.google.common.io.CharStreams;

public class ConfigurationLoader {

  public static String getResourceLocation(String resource, boolean fullpath) {
    String parsed = resource.replace(".", "/");
    return (fullpath ? "/" : "") + parsed + ".yaml";
  }

  public static AnyObject load(String resource) throws IOException {
    
    String resourceLocation = getResourceLocation(resource, true);
    InputStream in = ConfigurationLoader.class.getResourceAsStream(resourceLocation);
    if (in == null) {
      in = new URL(resource).openStream();
      if (in == null) {
        throw new FileNotFoundException(resourceLocation + " is not present on classpath");
      }
    }
    try {
      return SnakeYAMLLoader.getInstance().load(in);
    } finally {
      in.close();
    }
  }

  public static String getString(String resource) throws IOException {
    String resourceLocation = ConfigurationLoader.getResourceLocation(resource, true);
    InputStream in = ConfigurationLoader.class.getResourceAsStream(resourceLocation);
    try {
      Reader reader = new InputStreamReader(in);
      return CharStreams.toString(reader);
    } finally {
      in.close();
    }
  }
}
