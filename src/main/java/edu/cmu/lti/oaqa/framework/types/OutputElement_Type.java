
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
public class OutputElement_Type extends Annotation_Type {
  /** @generated */
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (OutputElement_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = OutputElement_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new OutputElement(addr, OutputElement_Type.this);
  			   OutputElement_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new OutputElement(addr, OutputElement_Type.this);
  	  }
    };
  /** @generated */
  public final static int typeIndexID = OutputElement.typeIndexID;
  /** @generated 
     @modifiable */
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("edu.cmu.lti.oaqa.framework.types.OutputElement");
 
  /** @generated */
  final Feature casFeat_sequenceId;
  /** @generated */
  final int     casFeatCode_sequenceId;
  /** @generated */ 
  public int getSequenceId(int addr) {
        if (featOkTst && casFeat_sequenceId == null)
      jcas.throwFeatMissing("sequenceId", "edu.cmu.lti.oaqa.framework.types.OutputElement");
    return ll_cas.ll_getIntValue(addr, casFeatCode_sequenceId);
  }
  /** @generated */    
  public void setSequenceId(int addr, int v) {
        if (featOkTst && casFeat_sequenceId == null)
      jcas.throwFeatMissing("sequenceId", "edu.cmu.lti.oaqa.framework.types.OutputElement");
    ll_cas.ll_setIntValue(addr, casFeatCode_sequenceId, v);}
    
  
 
  /** @generated */
  final Feature casFeat_answer;
  /** @generated */
  final int     casFeatCode_answer;
  /** @generated */ 
  public String getAnswer(int addr) {
        if (featOkTst && casFeat_answer == null)
      jcas.throwFeatMissing("answer", "edu.cmu.lti.oaqa.framework.types.OutputElement");
    return ll_cas.ll_getStringValue(addr, casFeatCode_answer);
  }
  /** @generated */    
  public void setAnswer(int addr, String v) {
        if (featOkTst && casFeat_answer == null)
      jcas.throwFeatMissing("answer", "edu.cmu.lti.oaqa.framework.types.OutputElement");
    ll_cas.ll_setStringValue(addr, casFeatCode_answer, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public OutputElement_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_sequenceId = jcas.getRequiredFeatureDE(casType, "sequenceId", "uima.cas.Integer", featOkTst);
    casFeatCode_sequenceId  = (null == casFeat_sequenceId) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_sequenceId).getCode();

 
    casFeat_answer = jcas.getRequiredFeatureDE(casType, "answer", "uima.cas.String", featOkTst);
    casFeatCode_answer  = (null == casFeat_answer) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_answer).getCode();

  }
}



    