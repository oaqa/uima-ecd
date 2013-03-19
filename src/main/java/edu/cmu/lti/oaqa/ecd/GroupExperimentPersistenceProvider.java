package edu.cmu.lti.oaqa.ecd;

import org.apache.uima.resource.Resource;

public interface GroupExperimentPersistenceProvider extends Resource {
  void insertGroupExperiment(String id, String fold, String foldId, String name, String author,
          String configuration, String resource, String testInstanceIdx) throws Exception;
}
