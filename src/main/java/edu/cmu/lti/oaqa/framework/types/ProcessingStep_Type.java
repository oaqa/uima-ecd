
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
public class ProcessingStep_Type extends Annotation_Type {
  /** @generated */
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (ProcessingStep_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = ProcessingStep_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new ProcessingStep(addr, ProcessingStep_Type.this);
  			   ProcessingStep_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new ProcessingStep(addr, ProcessingStep_Type.this);
  	  }
    };
  /** @generated */
  public final static int typeIndexID = ProcessingStep.typeIndexID;
  /** @generated 
     @modifiable */
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("edu.cmu.lti.oaqa.framework.types.ProcessingStep");
 
  /** @generated */
  final Feature casFeat_phaseId;
  /** @generated */
  final int     casFeatCode_phaseId;
  /** @generated */ 
  public int getPhaseId(int addr) {
        if (featOkTst && casFeat_phaseId == null)
      jcas.throwFeatMissing("phaseId", "edu.cmu.lti.oaqa.framework.types.ProcessingStep");
    return ll_cas.ll_getIntValue(addr, casFeatCode_phaseId);
  }
  /** @generated */    
  public void setPhaseId(int addr, int v) {
        if (featOkTst && casFeat_phaseId == null)
      jcas.throwFeatMissing("phaseId", "edu.cmu.lti.oaqa.framework.types.ProcessingStep");
    ll_cas.ll_setIntValue(addr, casFeatCode_phaseId, v);}
    
  
 
  /** @generated */
  final Feature casFeat_component;
  /** @generated */
  final int     casFeatCode_component;
  /** @generated */ 
  public String getComponent(int addr) {
        if (featOkTst && casFeat_component == null)
      jcas.throwFeatMissing("component", "edu.cmu.lti.oaqa.framework.types.ProcessingStep");
    return ll_cas.ll_getStringValue(addr, casFeatCode_component);
  }
  /** @generated */    
  public void setComponent(int addr, String v) {
        if (featOkTst && casFeat_component == null)
      jcas.throwFeatMissing("component", "edu.cmu.lti.oaqa.framework.types.ProcessingStep");
    ll_cas.ll_setStringValue(addr, casFeatCode_component, v);}
    
  
 
  /** @generated */
  final Feature casFeat_casId;
  /** @generated */
  final int     casFeatCode_casId;
  /** @generated */ 
  public String getCasId(int addr) {
        if (featOkTst && casFeat_casId == null)
      jcas.throwFeatMissing("casId", "edu.cmu.lti.oaqa.framework.types.ProcessingStep");
    return ll_cas.ll_getStringValue(addr, casFeatCode_casId);
  }
  /** @generated */    
  public void setCasId(int addr, String v) {
        if (featOkTst && casFeat_casId == null)
      jcas.throwFeatMissing("casId", "edu.cmu.lti.oaqa.framework.types.ProcessingStep");
    ll_cas.ll_setStringValue(addr, casFeatCode_casId, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	* @generated */
  public ProcessingStep_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_phaseId = jcas.getRequiredFeatureDE(casType, "phaseId", "uima.cas.Integer", featOkTst);
    casFeatCode_phaseId  = (null == casFeat_phaseId) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_phaseId).getCode();

 
    casFeat_component = jcas.getRequiredFeatureDE(casType, "component", "uima.cas.String", featOkTst);
    casFeatCode_component  = (null == casFeat_component) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_component).getCode();

 
    casFeat_casId = jcas.getRequiredFeatureDE(casType, "casId", "uima.cas.String", featOkTst);
    casFeatCode_casId  = (null == casFeat_casId) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_casId).getCode();

  }
}



    