
/* First created by JCasGen Sat Jul 21 15:40:55 EDT 2012 */
package edu.cmu.lti.oaqa.framework.types;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.FSGenerator;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.impl.FeatureImpl;
import org.apache.uima.cas.Feature;
import org.apache.uima.jcas.tcas.Annotation_Type;

/** 
 * Updated by JCasGen Sat Jul 21 15:40:55 EDT 2012
 * @generated */
public class InputElement_Type extends Annotation_Type {
  /** @generated */
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (InputElement_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = InputElement_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new InputElement(addr, InputElement_Type.this);
  			   InputElement_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new InputElement(addr, InputElement_Type.this);
  	  }
    };
  /** @generated */
  public final static int typeIndexID = InputElement.typeIndexID;
  /** @generated 
     @modifiable */
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("edu.cmu.lti.oaqa.framework.types.InputElement");
 
  /** @generated */
  final Feature casFeat_sequenceId;
  /** @generated */
  final int     casFeatCode_sequenceId;
  /** @generated */ 
  public int getSequenceId(int addr) {
        if (featOkTst && casFeat_sequenceId == null)
      jcas.throwFeatMissing("sequenceId", "edu.cmu.lti.oaqa.framework.types.InputElement");
    return ll_cas.ll_getIntValue(addr, casFeatCode_sequenceId);
  }
  /** @generated */    
  public void setSequenceId(int addr, int v) {
        if (featOkTst && casFeat_sequenceId == null)
      jcas.throwFeatMissing("sequenceId", "edu.cmu.lti.oaqa.framework.types.InputElement");
    ll_cas.ll_setIntValue(addr, casFeatCode_sequenceId, v);}
    
  
 
  /** @generated */
  final Feature casFeat_question;
  /** @generated */
  final int     casFeatCode_question;
  /** @generated */ 
  public String getQuestion(int addr) {
        if (featOkTst && casFeat_question == null)
      jcas.throwFeatMissing("question", "edu.cmu.lti.oaqa.framework.types.InputElement");
    return ll_cas.ll_getStringValue(addr, casFeatCode_question);
  }
  /** @generated */    
  public void setQuestion(int addr, String v) {
        if (featOkTst && casFeat_question == null)
      jcas.throwFeatMissing("question", "edu.cmu.lti.oaqa.framework.types.InputElement");
    ll_cas.ll_setStringValue(addr, casFeatCode_question, v);}
    
  
 
  /** @generated */
  final Feature casFeat_answerPattern;
  /** @generated */
  final int     casFeatCode_answerPattern;
  /** @generated */ 
  public String getAnswerPattern(int addr) {
        if (featOkTst && casFeat_answerPattern == null)
      jcas.throwFeatMissing("answerPattern", "edu.cmu.lti.oaqa.framework.types.InputElement");
    return ll_cas.ll_getStringValue(addr, casFeatCode_answerPattern);
  }
  /** @generated */    
  public void setAnswerPattern(int addr, String v) {
        if (featOkTst && casFeat_answerPattern == null)
      jcas.throwFeatMissing("answerPattern", "edu.cmu.lti.oaqa.framework.types.InputElement");
    ll_cas.ll_setStringValue(addr, casFeatCode_answerPattern, v);}
    
  
 
  /** @generated */
  final Feature casFeat_dataset;
  /** @generated */
  final int     casFeatCode_dataset;
  /** @generated */ 
  public String getDataset(int addr) {
        if (featOkTst && casFeat_dataset == null)
      jcas.throwFeatMissing("dataset", "edu.cmu.lti.oaqa.framework.types.InputElement");
    return ll_cas.ll_getStringValue(addr, casFeatCode_dataset);
  }
  /** @generated */    
  public void setDataset(int addr, String v) {
        if (featOkTst && casFeat_dataset == null)
      jcas.throwFeatMissing("dataset", "edu.cmu.lti.oaqa.framework.types.InputElement");
    ll_cas.ll_setStringValue(addr, casFeatCode_dataset, v);}
    
  
 
  /** @generated */
  final Feature casFeat_quuid;
  /** @generated */
  final int     casFeatCode_quuid;
  /** @generated */ 
  public String getQuuid(int addr) {
        if (featOkTst && casFeat_quuid == null)
      jcas.throwFeatMissing("quuid", "edu.cmu.lti.oaqa.framework.types.InputElement");
    return ll_cas.ll_getStringValue(addr, casFeatCode_quuid);
  }
  /** @generated */    
  public void setQuuid(int addr, String v) {
        if (featOkTst && casFeat_quuid == null)
      jcas.throwFeatMissing("quuid", "edu.cmu.lti.oaqa.framework.types.InputElement");
    ll_cas.ll_setStringValue(addr, casFeatCode_quuid, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public InputElement_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_sequenceId = jcas.getRequiredFeatureDE(casType, "sequenceId", "uima.cas.Integer", featOkTst);
    casFeatCode_sequenceId  = (null == casFeat_sequenceId) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_sequenceId).getCode();

 
    casFeat_question = jcas.getRequiredFeatureDE(casType, "question", "uima.cas.String", featOkTst);
    casFeatCode_question  = (null == casFeat_question) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_question).getCode();

 
    casFeat_answerPattern = jcas.getRequiredFeatureDE(casType, "answerPattern", "uima.cas.String", featOkTst);
    casFeatCode_answerPattern  = (null == casFeat_answerPattern) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_answerPattern).getCode();

 
    casFeat_dataset = jcas.getRequiredFeatureDE(casType, "dataset", "uima.cas.String", featOkTst);
    casFeatCode_dataset  = (null == casFeat_dataset) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_dataset).getCode();

 
    casFeat_quuid = jcas.getRequiredFeatureDE(casType, "quuid", "uima.cas.String", featOkTst);
    casFeatCode_quuid  = (null == casFeat_quuid) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_quuid).getCode();

  }
}



    