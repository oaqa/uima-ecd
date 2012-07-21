

/* First created by JCasGen Sat Jul 21 15:40:55 EDT 2012 */
package edu.cmu.lti.oaqa.framework.types;

import org.apache.uima.jcas.JCas; 
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;


/** 
 * Updated by JCasGen Sat Jul 21 15:40:55 EDT 2012
 * XML source: /Users/elmer/Documents/workspace/oaqa/uima-ecd/src/main/resources/edu/cmu/lti/oaqa/frameworkTypesDescriptor.xml
 * @generated */
public class ProcessingStep extends Annotation {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(ProcessingStep.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected ProcessingStep() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public ProcessingStep(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public ProcessingStep(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public ProcessingStep(JCas jcas, int begin, int end) {
    super(jcas);
    setBegin(begin);
    setEnd(end);
    readObject();
  }   

  /** <!-- begin-user-doc -->
    * Write your own initialization here
    * <!-- end-user-doc -->
  @generated modifiable */
  private void readObject() {}
     
 
    
  //*--------------*
  //* Feature: phaseId

  /** getter for phaseId - gets 
   * @generated */
  public int getPhaseId() {
    if (ProcessingStep_Type.featOkTst && ((ProcessingStep_Type)jcasType).casFeat_phaseId == null)
      jcasType.jcas.throwFeatMissing("phaseId", "edu.cmu.lti.oaqa.framework.types.ProcessingStep");
    return jcasType.ll_cas.ll_getIntValue(addr, ((ProcessingStep_Type)jcasType).casFeatCode_phaseId);}
    
  /** setter for phaseId - sets  
   * @generated */
  public void setPhaseId(int v) {
    if (ProcessingStep_Type.featOkTst && ((ProcessingStep_Type)jcasType).casFeat_phaseId == null)
      jcasType.jcas.throwFeatMissing("phaseId", "edu.cmu.lti.oaqa.framework.types.ProcessingStep");
    jcasType.ll_cas.ll_setIntValue(addr, ((ProcessingStep_Type)jcasType).casFeatCode_phaseId, v);}    
   
    
  //*--------------*
  //* Feature: component

  /** getter for component - gets 
   * @generated */
  public String getComponent() {
    if (ProcessingStep_Type.featOkTst && ((ProcessingStep_Type)jcasType).casFeat_component == null)
      jcasType.jcas.throwFeatMissing("component", "edu.cmu.lti.oaqa.framework.types.ProcessingStep");
    return jcasType.ll_cas.ll_getStringValue(addr, ((ProcessingStep_Type)jcasType).casFeatCode_component);}
    
  /** setter for component - sets  
   * @generated */
  public void setComponent(String v) {
    if (ProcessingStep_Type.featOkTst && ((ProcessingStep_Type)jcasType).casFeat_component == null)
      jcasType.jcas.throwFeatMissing("component", "edu.cmu.lti.oaqa.framework.types.ProcessingStep");
    jcasType.ll_cas.ll_setStringValue(addr, ((ProcessingStep_Type)jcasType).casFeatCode_component, v);}    
   
    
  //*--------------*
  //* Feature: casId

  /** getter for casId - gets 
   * @generated */
  public String getCasId() {
    if (ProcessingStep_Type.featOkTst && ((ProcessingStep_Type)jcasType).casFeat_casId == null)
      jcasType.jcas.throwFeatMissing("casId", "edu.cmu.lti.oaqa.framework.types.ProcessingStep");
    return jcasType.ll_cas.ll_getStringValue(addr, ((ProcessingStep_Type)jcasType).casFeatCode_casId);}
    
  /** setter for casId - sets  
   * @generated */
  public void setCasId(String v) {
    if (ProcessingStep_Type.featOkTst && ((ProcessingStep_Type)jcasType).casFeat_casId == null)
      jcasType.jcas.throwFeatMissing("casId", "edu.cmu.lti.oaqa.framework.types.ProcessingStep");
    jcasType.ll_cas.ll_setStringValue(addr, ((ProcessingStep_Type)jcasType).casFeatCode_casId, v);}    
  }

    