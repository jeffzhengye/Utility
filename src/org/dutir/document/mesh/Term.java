package org.dutir.document.mesh;

import java.io.Serializable;


/**
 * The String term in MeSH, which is the smallest unit in MeSH.
 * @author yezheng
 *
 */
public class Term implements Serializable{

	public String termValue ;
	public String cntermValue ;

	String entermValue;

	public Term(String termValue) {
		this.termValue = termValue;
	}
	
	public Term(String termValue, String cntermValue) {
		this.termValue = termValue;
		this.cntermValue = cntermValue;
	}
	
	public void setTran(String tran){
		this.cntermValue = tran;
	}
	
	public static Term String2Term(String value) {
		return new Term(value);
	}
	
	public String toString()
	{
		return this.termValue + ":" + this.cntermValue;
	}
}