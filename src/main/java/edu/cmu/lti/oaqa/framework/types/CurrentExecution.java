
/* First created by JCasGen Tue Dec 18 22:48:45 EST 2012 */
package edu.cmu.lti.oaqa.framework.types;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.jcas.cas.TOP_Type;

import org.apache.uima.jcas.tcas.Annotation;

import org.apache.uima.jcas.cas.TOP;

/**
 * Updated by JCasGen Fri Jan 18 19:04:09 EST 2013 XML source:
 * /Users/elmer/Documents
 * /workspace/oaqa/uima-ecd/src/main/resources/edu/cmu/lti/
 * oaqa/frameworkTypesDescriptor.xml
 * 
 * @generated
 */
public class CurrentExecution extends Annotation {
	/**
	 * @generated
	 * @ordered
	 */
	public final static int typeIndexID = JCasRegistry
			.register(CurrentExecution.class);
	/**
	 * @generated
	 * @ordered
	 */
	public final static int type = typeIndexID;

	/** @generated */
	public int getTypeIndexID() {
		return typeIndexID;
	}

	/**
	 * Never called. Disable default constructor
	 * 
	 * @generated
	 */
	protected CurrentExecution() {/* intentionally empty block */
	}

	/**
	 * Internal - constructor used by generator
	 * 
	 * @generated
	 */
	public CurrentExecution(int addr, TOP_Type type) {
		super(addr, type);
		readObject();
	}

	/** @generated */
	public CurrentExecution(JCas jcas) {
		super(jcas);
		readObject();
	}

	/** @generated */
	public CurrentExecution(JCas jcas, int begin, int end) {
		super(jcas);
		setBegin(begin);
		setEnd(end);
		readObject();
	}

	/**
	 * <!-- begin-user-doc --> Write your own initialization here <!--
	 * end-user-doc -->
	 * 
	 * @generated modifiable
	 */
	private void readObject() {
	}

	// *--------------*
	// * Feature: idHash

	/**
	 * getter for idHash - gets
	 * 
	 * @generated
	 */
	public String getIdHash() {
		if (CurrentExecution_Type.featOkTst
				&& ((CurrentExecution_Type) jcasType).casFeat_idHash == null)
			jcasType.jcas.throwFeatMissing("idHash",
					"edu.cmu.lti.oaqa.framework.types.CurrentExecution");
		return jcasType.ll_cas.ll_getStringValue(addr,
				((CurrentExecution_Type) jcasType).casFeatCode_idHash);
	}

	/**
	 * setter for idHash - sets
	 * 
	 * @generated
	 */
	public void setIdHash(String v) {
		if (CurrentExecution_Type.featOkTst
				&& ((CurrentExecution_Type) jcasType).casFeat_idHash == null)
			jcasType.jcas.throwFeatMissing("idHash",
					"edu.cmu.lti.oaqa.framework.types.CurrentExecution");
		jcasType.ll_cas.ll_setStringValue(addr,
				((CurrentExecution_Type) jcasType).casFeatCode_idHash, v);
	}
}
