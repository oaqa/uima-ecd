package edu.cmu.lti.oaqa.ecd.impl;

import java.util.Date;

import edu.cmu.lti.oaqa.ecd.log.LogEntry;
import edu.cmu.lti.oaqa.ecd.phase.Trace;

public class DefaultLogPersistenceProvider extends AbstractLogPersistenceProvider {

  @Override
  public void log(String uuid, Trace trace, LogEntry type, String message) {
    System.out.printf("[logger] %s,%s,%s,%s,%s\n", new Date(), uuid, trace, type, message);
  }

}
