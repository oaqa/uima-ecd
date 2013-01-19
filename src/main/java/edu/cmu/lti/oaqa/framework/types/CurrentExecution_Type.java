
/* First created by JCasGen Tue Dec 18 22:48:45 EST 2012 */
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

import org.apache.uima.jcas.cas.TOP_Type;

/** 
 * Updated by JCasGen Fri Jan 18 19:04:09 EST 2013
 * @generated */
public class CurrentExecution_Type extends Annotation_Type {
  /** @generated */
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (CurrentExecution_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = CurrentExecution_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new CurrentExecution(addr, CurrentExecution_Type.this);
  			   CurrentExecution_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new CurrentExecution(addr, CurrentExecution_Type.this);
  	  }
    };
  /** @generated */
  public final static int typeIndexID = CurrentExecution.typeIndexID;
  /** @generated 
     @modifiable */
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("edu.cmu.lti.oaqa.framework.types.CurrentExecution");
 
  /** @generated */
  final Feature casFeat_idHash;
  /** @generated */
  final int     casFeatCode_idHash;
  /** @generated */ 
  public String getIdHash(int addr) {
        if (featOkTst && casFeat_idHash == null)
      jcas.throwFeatMissing("idHash", "edu.cmu.lti.oaqa.framework.types.CurrentExecution");
    return ll_cas.ll_getStringValue(addr, casFeatCode_idHash);
  }
  /** @generated */    
  public void setIdHash(int addr, String v) {
        if (featOkTst && casFeat_idHash == null)
      jcas.throwFeatMissing("idHash", "edu.cmu.lti.oaqa.framework.types.CurrentExecution");
    ll_cas.ll_setStringValue(addr, casFeatCode_idHash, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public CurrentExecution_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_idHash = jcas.getRequiredFeatureDE(casType, "idHash", "uima.cas.String", featOkTst);
    casFeatCode_idHash  = (null == casFeat_idHash) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_idHash).getCode();

  }
}



    