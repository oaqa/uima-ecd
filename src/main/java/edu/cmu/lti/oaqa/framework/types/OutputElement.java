

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
public class OutputElement extends Annotation {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(OutputElement.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected OutputElement() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public OutputElement(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public OutputElement(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public OutputElement(JCas jcas, int begin, int end) {
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
  //* Feature: sequenceId

  /** getter for sequenceId - gets 
   * @generated */
  public int getSequenceId() {
    if (OutputElement_Type.featOkTst && ((OutputElement_Type)jcasType).casFeat_sequenceId == null)
      jcasType.jcas.throwFeatMissing("sequenceId", "edu.cmu.lti.oaqa.framework.types.OutputElement");
    return jcasType.ll_cas.ll_getIntValue(addr, ((OutputElement_Type)jcasType).casFeatCode_sequenceId);}
    
  /** setter for sequenceId - sets  
   * @generated */
  public void setSequenceId(int v) {
    if (OutputElement_Type.featOkTst && ((OutputElement_Type)jcasType).casFeat_sequenceId == null)
      jcasType.jcas.throwFeatMissing("sequenceId", "edu.cmu.lti.oaqa.framework.types.OutputElement");
    jcasType.ll_cas.ll_setIntValue(addr, ((OutputElement_Type)jcasType).casFeatCode_sequenceId, v);}    
   
    
  //*--------------*
  //* Feature: answer

  /** getter for answer - gets 
   * @generated */
  public String getAnswer() {
    if (OutputElement_Type.featOkTst && ((OutputElement_Type)jcasType).casFeat_answer == null)
      jcasType.jcas.throwFeatMissing("answer", "edu.cmu.lti.oaqa.framework.types.OutputElement");
    return jcasType.ll_cas.ll_getStringValue(addr, ((OutputElement_Type)jcasType).casFeatCode_answer);}
    
  /** setter for answer - sets  
   * @generated */
  public void setAnswer(String v) {
    if (OutputElement_Type.featOkTst && ((OutputElement_Type)jcasType).casFeat_answer == null)
      jcasType.jcas.throwFeatMissing("answer", "edu.cmu.lti.oaqa.framework.types.OutputElement");
    jcasType.ll_cas.ll_setStringValue(addr, ((OutputElement_Type)jcasType).casFeatCode_answer, v);}    
  }

    