package edu.cmu.lti.oaqa.ecd.phase;

import java.io.InputStream;
import java.util.Iterator;
import java.util.ListIterator;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.cas.CASRuntimeException;
import org.apache.uima.cas.ConstraintFactory;
import org.apache.uima.cas.FSIndexRepository;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.FSMatchConstraint;
import org.apache.uima.cas.Feature;
import org.apache.uima.cas.FeaturePath;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.FeatureValuePath;
import org.apache.uima.cas.SofaFS;
import org.apache.uima.cas.SofaID;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.TypeSystem;
import org.apache.uima.cas.admin.CASAdminException;
import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.LowLevelCAS;
import org.apache.uima.cas.impl.LowLevelIndexRepository;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JFSIndexRepository;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.cas.FloatArray;
import org.apache.uima.jcas.cas.IntegerArray;
import org.apache.uima.jcas.cas.Sofa;
import org.apache.uima.jcas.cas.StringArray;
import org.apache.uima.jcas.cas.TOP;
import org.apache.uima.jcas.cas.TOP_Type;
import org.apache.uima.jcas.tcas.Annotation;

class WrappedJCas implements JCas {

  private final JCas delegate;

  private boolean alive = true;

  public WrappedJCas(JCas aCas) {
    this.delegate = aCas;
  }

  void invalidate() {
    delegate.getCasImpl().enableReset(true);
    delegate.release();
    this.alive = false;
  }

  private void testLiveness() {
    if (!alive) {
      throw new IllegalStateException("Cas was already death");
    }
  }

  @Override
  public void release() {
    testLiveness();
    delegate.release();
  }

  @Override
  public FSIndexRepository getFSIndexRepository() {
    testLiveness(); // TODO Auto-generated method stub
    return delegate.getFSIndexRepository();
  }

  @Override
  public LowLevelIndexRepository getLowLevelIndexRepository() {
    testLiveness(); // TODO Auto-generated method stub
    return delegate.getLowLevelIndexRepository();
  }

  @Override
  public CAS getCas() {
    testLiveness(); // TODO Auto-generated method stub
    return delegate.getCas();
  }

  @Override
  public CASImpl getCasImpl() {
    testLiveness(); // TODO Auto-generated method stub
    return delegate.getCasImpl();
  }

  @Override
  public LowLevelCAS getLowLevelCas() {
    testLiveness(); // TODO Auto-generated method stub
    return delegate.getLowLevelCas();
  }

  @Override
  public TOP_Type getType(int i) {
    testLiveness(); // TODO Auto-generated method stub
    return delegate.getType(i);
  }

  @Override
  public Type getCasType(int i) {
    testLiveness(); // TODO Auto-generated method stub
    return delegate.getCasType(i);
  }

  @Override
  @Deprecated
  public TOP_Type getType(TOP instance) {
    testLiveness(); // TODO Auto-generated method stub
    return delegate.getType(instance);
  }

  @Override
  public Type getRequiredType(String s) throws CASException {
    testLiveness(); // TODO Auto-generated method stub
    return delegate.getRequiredType(s);
  }

  @Override
  public Feature getRequiredFeature(Type t, String s) throws CASException {
    testLiveness(); // TODO Auto-generated method stub
    return delegate.getRequiredFeature(t, s);
  }

  @Override
  public Feature getRequiredFeatureDE(Type t, String s, String rangeName, boolean featOkTst) {
    testLiveness(); // TODO Auto-generated method stub
    return delegate.getRequiredFeatureDE(t, s, rangeName, featOkTst);
  }

  @Override
  public void putJfsFromCaddr(int casAddr, FeatureStructure fs) {
    testLiveness(); // TODO Auto-generated method stub
    putJfsFromCaddr(casAddr, fs);
  }

  @Override
  public TOP getJfsFromCaddr(int casAddr) {
    testLiveness(); // TODO Auto-generated method stub
    return delegate.getJfsFromCaddr(casAddr);
  }

  @Override
  public void checkArrayBounds(int fsRef, int pos) {
    testLiveness(); // TODO Auto-generated method stub
    checkArrayBounds(fsRef, pos);
  }

  @Override
  public void throwFeatMissing(String feat, String type) {
    testLiveness(); // TODO Auto-generated method stub
    throwFeatMissing(feat, type);
  }

  @Override
  @Deprecated
  public Sofa getSofa(SofaID sofaID) {
    testLiveness(); // TODO Auto-generated method stub
    return delegate.getSofa();
  }

  @Override
  public Sofa getSofa() {
    testLiveness(); // TODO Auto-generated method stub
    return delegate.getSofa();
  }

  @Override
  public JCas createView(String sofaID) throws CASException {
    testLiveness(); // TODO Auto-generated method stub
    return delegate.createView(sofaID);
  }

  @Override
  public JCas getJCas(Sofa sofa) throws CASException {
    testLiveness(); // TODO Auto-generated method stub
    return delegate.getJCas(sofa);
  }

  @Override
  public JFSIndexRepository getJFSIndexRepository() {
    testLiveness(); // TODO Auto-generated method stub
    return delegate.getJFSIndexRepository();
  }

  @Override
  public TOP getDocumentAnnotationFs() {
    testLiveness(); // TODO Auto-generated method stub
    return delegate.getDocumentAnnotationFs();
  }

  @Override
  public StringArray getStringArray0L() {
    testLiveness(); // TODO Auto-generated method stub
    return delegate.getStringArray0L();
  }

  @Override
  public IntegerArray getIntegerArray0L() {
    testLiveness(); // TODO Auto-generated method stub
    return delegate.getIntegerArray0L();
  }

  @Override
  public FloatArray getFloatArray0L() {
    testLiveness(); // TODO Auto-generated method stub
    return delegate.getFloatArray0L();
  }

  @Override
  public FSArray getFSArray0L() {
    testLiveness(); // TODO Auto-generated method stub
    return delegate.getFSArray0L();
  }

  @Override
  @Deprecated
  public void processInit() {
    testLiveness(); // TODO Auto-generated method stub
    processInit();
  }

  @Override
  public JCas getView(String localViewName) throws CASException {
    testLiveness(); // TODO Auto-generated method stub
    return delegate.getView(localViewName);
  }

  @Override
  public JCas getView(SofaFS aSofa) throws CASException {
    testLiveness(); // TODO Auto-generated method stub
    return delegate.getView(aSofa);
  }

  @Override
  public TypeSystem getTypeSystem() throws CASRuntimeException {
    testLiveness(); // TODO Auto-generated method stub
    return delegate.getTypeSystem();
  }

  @Override
  @Deprecated
  public SofaFS createSofa(SofaID sofaID, String mimeType) {
    testLiveness(); // TODO Auto-generated method stub
    return delegate.createSofa(sofaID, mimeType);
  }

  @Override
  public FSIterator<SofaFS> getSofaIterator() {
    testLiveness(); // TODO Auto-generated method stub
    return delegate.getSofaIterator();
  }

  @Override
  public <T extends FeatureStructure> FSIterator<T> createFilteredIterator(FSIterator<T> it,
          FSMatchConstraint cons) {
    testLiveness(); // TODO Auto-generated method stub
    return delegate.createFilteredIterator(it, cons);
  }

  @Override
  public ConstraintFactory getConstraintFactory() {
    testLiveness(); // TODO Auto-generated method stub
    return delegate.getConstraintFactory();
  }

  @Override
  public FeaturePath createFeaturePath() {
    testLiveness(); // TODO Auto-generated method stub
    return delegate.createFeaturePath();
  }

  @Override
  public FSIndexRepository getIndexRepository() {
    testLiveness(); // TODO Auto-generated method stub
    return delegate.getIndexRepository();
  }

  @Override
  public <T extends FeatureStructure> ListIterator<T> fs2listIterator(FSIterator<T> it) {
    testLiveness(); // TODO Auto-generated method stub
    return delegate.fs2listIterator(it);
  }

  @Override
  public void reset() throws CASAdminException {
    testLiveness(); // TODO Auto-generated method stub
    delegate.reset();
  }

  @Override
  public String getViewName() {
    testLiveness(); // TODO Auto-generated method stub
    return delegate.getViewName();
  }

  @Override
  public int size() {
    testLiveness(); // TODO Auto-generated method stub
    return delegate.size();
  }

  @Override
  public FeatureValuePath createFeatureValuePath(String featureValuePath)
          throws CASRuntimeException {
    testLiveness(); // TODO Auto-generated method stub
    return delegate.createFeatureValuePath(featureValuePath);
  }

  @Override
  public void setDocumentText(String text) throws CASRuntimeException {
    testLiveness(); // TODO Auto-generated method stub
    delegate.setDocumentText(text);
  }

  @Override
  public void setSofaDataString(String text, String mimetype) throws CASRuntimeException {
    testLiveness(); // TODO Auto-generated method stub
    delegate.setSofaDataString(text, mimetype);
  }

  @Override
  public String getDocumentText() {
    testLiveness(); // TODO Auto-generated method stub
    return delegate.getDocumentText();
  }

  @Override
  public String getSofaDataString() {
    testLiveness(); // TODO Auto-generated method stub
    return delegate.getSofaDataString();
  }

  @Override
  public void setDocumentLanguage(String languageCode) throws CASRuntimeException {
    testLiveness(); // TODO Auto-generated method stub
    delegate.setDocumentLanguage(languageCode);
  }

  @Override
  public String getDocumentLanguage() {
    testLiveness(); // TODO Auto-generated method stub
    return delegate.getDocumentLanguage();
  }

  @Override
  public void setSofaDataArray(FeatureStructure array, String mime) throws CASRuntimeException {
    testLiveness(); // TODO Auto-generated method stub
    delegate.setSofaDataArray(array, mime);
  }

  @Override
  public FeatureStructure getSofaDataArray() {
    testLiveness(); // TODO Auto-generated method stub
    return delegate.getSofaDataArray();
  }

  @Override
  public void setSofaDataURI(String uri, String mime) throws CASRuntimeException {
    testLiveness(); // TODO Auto-generated method stub
    delegate.setSofaDataURI(uri, mime);
  }

  @Override
  public String getSofaDataURI() {
    testLiveness(); // TODO Auto-generated method stub
    return delegate.getSofaDataURI();
  }

  @Override
  public InputStream getSofaDataStream() {
    testLiveness(); // TODO Auto-generated method stub
    return delegate.getSofaDataStream();
  }

  @Override
  public String getSofaMimeType() {
    testLiveness(); // TODO Auto-generated method stub
    return delegate.getSofaMimeType();
  }

  @Override
  public void addFsToIndexes(FeatureStructure fs) {
    testLiveness(); // TODO Auto-generated method stub
    delegate.addFsToIndexes(fs);
  }

  @Override
  public void removeFsFromIndexes(FeatureStructure fs) {
    testLiveness(); // TODO Auto-generated method stub
    delegate.removeFsFromIndexes(fs);
  }

  @Override
  public AnnotationIndex<Annotation> getAnnotationIndex() {
    testLiveness(); // TODO Auto-generated method stub
    return delegate.getAnnotationIndex();
  }

  @Override
  public AnnotationIndex<Annotation> getAnnotationIndex(Type type) throws CASRuntimeException {
    testLiveness(); // TODO Auto-generated method stub
    return delegate.getAnnotationIndex(type);
  }

  @Override
  public AnnotationIndex<Annotation> getAnnotationIndex(int type) throws CASRuntimeException {
    testLiveness(); // TODO Auto-generated method stub
    return delegate.getAnnotationIndex(type);
  }

  @Override
  public Iterator<JCas> getViewIterator() throws CASException {
    testLiveness();
    return delegate.getViewIterator();
  }

  @Override
  public Iterator<JCas> getViewIterator(String localViewNamePrefix) throws CASException {
    testLiveness();
    return delegate.getViewIterator(localViewNamePrefix);
  }

}