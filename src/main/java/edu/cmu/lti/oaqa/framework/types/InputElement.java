

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
public class InputElement extends Annotation {
  /** @generated
   * @ordered 
   */
  public final static int typeIndexID = JCasRegistry.register(InputElement.class);
  /** @generated
   * @ordered 
   */
  public final static int type = typeIndexID;
  /** @generated  */
  public              int getTypeIndexID() {return typeIndexID;}
 
  /** Never called.  Disable default constructor
   * @generated */
  protected InputElement() {}
    
  /** Internal - constructor used by generator 
   * @generated */
  public InputElement(int addr, TOP_Type type) {
    super(addr, type);
    readObject();
  }
  
  /** @generated */
  public InputElement(JCas jcas) {
    super(jcas);
    readObject();   
  } 

  /** @generated */  
  public InputElement(JCas jcas, int begin, int end) {
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
    if (InputElement_Type.featOkTst && ((InputElement_Type)jcasType).casFeat_sequenceId == null)
      jcasType.jcas.throwFeatMissing("sequenceId", "edu.cmu.lti.oaqa.framework.types.InputElement");
    return jcasType.ll_cas.ll_getIntValue(addr, ((InputElement_Type)jcasType).casFeatCode_sequenceId);}
    
  /** setter for sequenceId - sets  
   * @generated */
  public void setSequenceId(int v) {
    if (InputElement_Type.featOkTst && ((InputElement_Type)jcasType).casFeat_sequenceId == null)
      jcasType.jcas.throwFeatMissing("sequenceId", "edu.cmu.lti.oaqa.framework.types.InputElement");
    jcasType.ll_cas.ll_setIntValue(addr, ((InputElement_Type)jcasType).casFeatCode_sequenceId, v);}    
   
    
  //*--------------*
  //* Feature: question

  /** getter for question - gets 
   * @generated */
  public String getQuestion() {
    if (InputElement_Type.featOkTst && ((InputElement_Type)jcasType).casFeat_question == null)
      jcasType.jcas.throwFeatMissing("question", "edu.cmu.lti.oaqa.framework.types.InputElement");
    return jcasType.ll_cas.ll_getStringValue(addr, ((InputElement_Type)jcasType).casFeatCode_question);}
    
  /** setter for question - sets  
   * @generated */
  public void setQuestion(String v) {
    if (InputElement_Type.featOkTst && ((InputElement_Type)jcasType).casFeat_question == null)
      jcasType.jcas.throwFeatMissing("question", "edu.cmu.lti.oaqa.framework.types.InputElement");
    jcasType.ll_cas.ll_setStringValue(addr, ((InputElement_Type)jcasType).casFeatCode_question, v);}    
   
    
  //*--------------*
  //* Feature: answerPattern

  /** getter for answerPattern - gets 
   * @generated */
  public String getAnswerPattern() {
    if (InputElement_Type.featOkTst && ((InputElement_Type)jcasType).casFeat_answerPattern == null)
      jcasType.jcas.throwFeatMissing("answerPattern", "edu.cmu.lti.oaqa.framework.types.InputElement");
    return jcasType.ll_cas.ll_getStringValue(addr, ((InputElement_Type)jcasType).casFeatCode_answerPattern);}
    
  /** setter for answerPattern - sets  
   * @generated */
  public void setAnswerPattern(String v) {
    if (InputElement_Type.featOkTst && ((InputElement_Type)jcasType).casFeat_answerPattern == null)
      jcasType.jcas.throwFeatMissing("answerPattern", "edu.cmu.lti.oaqa.framework.types.InputElement");
    jcasType.ll_cas.ll_setStringValue(addr, ((InputElement_Type)jcasType).casFeatCode_answerPattern, v);}    
   
    
  //*--------------*
  //* Feature: dataset

  /** getter for dataset - gets 
   * @generated */
  public String getDataset() {
    if (InputElement_Type.featOkTst && ((InputElement_Type)jcasType).casFeat_dataset == null)
      jcasType.jcas.throwFeatMissing("dataset", "edu.cmu.lti.oaqa.framework.types.InputElement");
    return jcasType.ll_cas.ll_getStringValue(addr, ((InputElement_Type)jcasType).casFeatCode_dataset);}
    
  /** setter for dataset - sets  
   * @generated */
  public void setDataset(String v) {
    if (InputElement_Type.featOkTst && ((InputElement_Type)jcasType).casFeat_dataset == null)
      jcasType.jcas.throwFeatMissing("dataset", "edu.cmu.lti.oaqa.framework.types.InputElement");
    jcasType.ll_cas.ll_setStringValue(addr, ((InputElement_Type)jcasType).casFeatCode_dataset, v);}    
   
    
  //*--------------*
  //* Feature: quuid

  /** getter for quuid - gets 
   * @generated */
  public String getQuuid() {
    if (InputElement_Type.featOkTst && ((InputElement_Type)jcasType).casFeat_quuid == null)
      jcasType.jcas.throwFeatMissing("quuid", "edu.cmu.lti.oaqa.framework.types.InputElement");
    return jcasType.ll_cas.ll_getStringValue(addr, ((InputElement_Type)jcasType).casFeatCode_quuid);}
    
  /** setter for quuid - sets  
   * @generated */
  public void setQuuid(String v) {
    if (InputElement_Type.featOkTst && ((InputElement_Type)jcasType).casFeat_quuid == null)
      jcasType.jcas.throwFeatMissing("quuid", "edu.cmu.lti.oaqa.framework.types.InputElement");
    jcasType.ll_cas.ll_setStringValue(addr, ((InputElement_Type)jcasType).casFeatCode_quuid, v);}    
  }

    