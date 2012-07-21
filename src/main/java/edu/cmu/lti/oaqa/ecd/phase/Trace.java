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

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

public final class Trace {

  private static final HashFunction hf = Hashing.sha256();
  
  private final String trace;
  
  private final String traceId;
  
  public Trace(String trace) {
    this.trace = trace;
    this.traceId = hf.hashString(trace).toString();
  }
  
  public int hashCode() {
    return trace.hashCode();
  }
  
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof Trace)) {
      return false;
    }
    Trace t = (Trace) o; 
    return trace.equals(t.trace);
  }
  
  public String getTrace() {
    return trace;
  }
  
  public String getTraceHash() {
    return traceId;
  }
  
  public String toString() {
    return trace;
  }
  
}
