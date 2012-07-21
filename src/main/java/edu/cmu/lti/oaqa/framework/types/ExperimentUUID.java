

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
public class ExperimentUUID extends Annotation {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(ExperimentUUID.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected ExperimentUUID() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public ExperimentUUID(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public ExperimentUUID(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public ExperimentUUID(JCas jcas, int begin, int end) {
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
  //* Feature: uuid

  /** getter for uuid - gets 
   * @generated */
  public String getUuid() {
    if (ExperimentUUID_Type.featOkTst && ((ExperimentUUID_Type)jcasType).casFeat_uuid == null)
      jcasType.jcas.throwFeatMissing("uuid", "edu.cmu.lti.oaqa.framework.types.ExperimentUUID");
    return jcasType.ll_cas.ll_getStringValue(addr, ((ExperimentUUID_Type)jcasType).casFeatCode_uuid);}
    
  /** setter for uuid - sets  
   * @generated */
  public void setUuid(String v) {
    if (ExperimentUUID_Type.featOkTst && ((ExperimentUUID_Type)jcasType).casFeat_uuid == null)
      jcasType.jcas.throwFeatMissing("uuid", "edu.cmu.lti.oaqa.framework.types.ExperimentUUID");
    jcasType.ll_cas.ll_setStringValue(addr, ((ExperimentUUID_Type)jcasType).casFeatCode_uuid, v);}    
   
    
  //*--------------*
  //* Feature: stageId

  /** getter for stageId - gets 
   * @generated */
  public int getStageId() {
    if (ExperimentUUID_Type.featOkTst && ((ExperimentUUID_Type)jcasType).casFeat_stageId == null)
      jcasType.jcas.throwFeatMissing("stageId", "edu.cmu.lti.oaqa.framework.types.ExperimentUUID");
    return jcasType.ll_cas.ll_getIntValue(addr, ((ExperimentUUID_Type)jcasType).casFeatCode_stageId);}
    
  /** setter for stageId - sets  
   * @generated */
  public void setStageId(int v) {
    if (ExperimentUUID_Type.featOkTst && ((ExperimentUUID_Type)jcasType).casFeat_stageId == null)
      jcasType.jcas.throwFeatMissing("stageId", "edu.cmu.lti.oaqa.framework.types.ExperimentUUID");
    jcasType.ll_cas.ll_setIntValue(addr, ((ExperimentUUID_Type)jcasType).casFeatCode_stageId, v);}    
  }

    