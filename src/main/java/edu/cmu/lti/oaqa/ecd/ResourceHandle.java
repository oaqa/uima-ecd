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

public class ResourceHandle {
  
  public enum HandleType {
    INHERIT, CLASS, PIPELINE, TRAINCLASS;
    
    public static HandleType getInstance(String value) {
      return HandleType.valueOf(value.toUpperCase().replaceAll("-",""));
    }
  }
  
  private final HandleType type;

  final String resource;

  private ResourceHandle(HandleType type, String resource) {
    this.type = type;
    this.resource = resource;
  }

  public static ResourceHandle newInheritHandle(String resource) {
    return new ResourceHandle(HandleType.INHERIT, resource);
  }

  public static ResourceHandle newHandle(String type, String resource) {
    try {
      return new ResourceHandle(HandleType.valueOf(type.toUpperCase()), resource);
    } catch (Exception e) {
      throw new IllegalArgumentException(
              "Illegal experiment descriptor, must contain one node of type <class> or <inherit>");
    }
  }

  public static ResourceHandle newHandle(HandleType type, String resource) {
    return new ResourceHandle(type, resource);
  }

  HandleType getType() {
    return type;
  }
  
  @Override
  public String toString() {
    return String.format("%s[%s]", type.toString().toLowerCase(), resource);
  }
}