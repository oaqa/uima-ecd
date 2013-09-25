package edu.cmu.lti.oaqa.ecd.util;


import java.util.Iterator;

import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.TypeSystem;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.TOP;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.fit.util.JCasUtil;


public final class CasUtils {

  private CasUtils() {}

  public static <T extends TOP> T getLast(JCas jcas, Class<T> type) {
    Iterator<T> iterator = JCasUtil.iterator(jcas, type);
    T last = null;
    while (iterator.hasNext()) {
      last = iterator.next();
    }
    return last;
  }
  
  public static Annotation getFirst(JCas jcas, String typeName) {
    TypeSystem ts = jcas.getTypeSystem();
    Type type = ts.getType(typeName);
    AnnotationIndex<Annotation> index = jcas.getAnnotationIndex(type);
    FSIterator<Annotation> iterator = index.iterator();
    if (iterator.hasNext()) {
      return (Annotation) iterator.next();
    }
    return null;
  }

  public static Annotation getFirst(JCas jcas, Type type) {
    AnnotationIndex<Annotation> index = jcas.getAnnotationIndex(type);
    FSIterator<Annotation> iterator = index.iterator();
    if (iterator.hasNext())
      return (Annotation) iterator.next();
    return null;
  }

  public static Type getType(JCas jcas, String typeName) {
    TypeSystem ts = jcas.getTypeSystem();
    return ts.getType(typeName);
  }
}
