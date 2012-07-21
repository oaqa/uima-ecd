
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
public class ExperimentUUID_Type extends Annotation_Type {
  /** @generated */
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (ExperimentUUID_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = ExperimentUUID_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new ExperimentUUID(addr, ExperimentUUID_Type.this);
  			   ExperimentUUID_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new ExperimentUUID(addr, ExperimentUUID_Type.this);
  	  }
    };
  /** @generated */
  public final static int typeIndexID = ExperimentUUID.typeIndexID;
  /** @generated 
     @modifiable */
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("edu.cmu.lti.oaqa.framework.types.ExperimentUUID");
 
  /** @generated */
  final Feature casFeat_uuid;
  /** @generated */
  final int     casFeatCode_uuid;
  /** @generated */ 
  public String getUuid(int addr) {
        if (featOkTst && casFeat_uuid == null)
      jcas.throwFeatMissing("uuid", "edu.cmu.lti.oaqa.framework.types.ExperimentUUID");
    return ll_cas.ll_getStringValue(addr, casFeatCode_uuid);
  }
  /** @generated */    
  public void setUuid(int addr, String v) {
        if (featOkTst && casFeat_uuid == null)
      jcas.throwFeatMissing("uuid", "edu.cmu.lti.oaqa.framework.types.ExperimentUUID");
    ll_cas.ll_setStringValue(addr, casFeatCode_uuid, v);}
    
  
 
  /** @generated */
  final Feature casFeat_stageId;
  /** @generated */
  final int     casFeatCode_stageId;
  /** @generated */ 
  public int getStageId(int addr) {
        if (featOkTst && casFeat_stageId == null)
      jcas.throwFeatMissing("stageId", "edu.cmu.lti.oaqa.framework.types.ExperimentUUID");
    return ll_cas.ll_getIntValue(addr, casFeatCode_stageId);
  }
  /** @generated */    
  public void setStageId(int addr, int v) {
        if (featOkTst && casFeat_stageId == null)
      jcas.throwFeatMissing("stageId", "edu.cmu.lti.oaqa.framework.types.ExperimentUUID");
    ll_cas.ll_setIntValue(addr, casFeatCode_stageId, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public ExperimentUUID_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_uuid = jcas.getRequiredFeatureDE(casType, "uuid", "uima.cas.String", featOkTst);
    casFeatCode_uuid  = (null == casFeat_uuid) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_uuid).getCode();

 
    casFeat_stageId = jcas.getRequiredFeatureDE(casType, "stageId", "uima.cas.Integer", featOkTst);
    casFeatCode_stageId  = (null == casFeat_stageId) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_stageId).getCode();

  }
}



    