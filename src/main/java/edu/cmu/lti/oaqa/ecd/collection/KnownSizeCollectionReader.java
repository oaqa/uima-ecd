package edu.cmu.lti.oaqa.ecd.collection;

import org.apache.uima.collection.CollectionReader;

public interface KnownSizeCollectionReader extends CollectionReader {
  public int size();
}
