/**
 * 
 */
package org.dutir.document.mesh;

import java.io.Serializable;
import java.util.*;

/**
 * @author yezheng
 *
 */


public class Concept implements Serializable{

	public String UI;
	
	public ArrayList<String> ST; //Semantic Type

	public ArrayList<Term> term;
	
	String ConceptCache = null;
	
	public String[] gerTerms(){
		String str[] = new String[this.term.size()];
		for(int i=0;i < str.length; i++)
		{
			str[i] = this.term.get(i).termValue;
		}
		return str;
	}
	
	/**
	 * 
	 */
	public Concept() {
		// TODO Auto-generated constructor stub
		term = new ArrayList<Term>(8);
		ST = new ArrayList<String>(4);
	}

	public ArrayList<Term> getTermList() {
		return this.term;
	}

	public Term[] getTerms() {
		Term terms[] = new Term[this.term.size()];
		this.term.toArray(terms);
		return terms;
	}

	public void setUI(String ui) {
		this.UI = ui;
	}

	public void addST(String st) {
		this.ST.add(st);
	}

	public void addTerm(String termValue) {
		this.term.add(new Term(termValue));
	}
	
	public void addTerm(Term term) {
		this.term.add(term);
	}
	
	public void addPreferTerm(String termValue) {
		this.term.add(0, new Term(termValue));
	}

	public String getConceptName() {
		return this.term.get(0).termValue;
	}

	public String toString() {
		if(this.ConceptCache != null){
			return this.ConceptCache;
		}else{
			StringBuffer buf = new StringBuffer();
			buf.append(this.UI + "|");
			int size = this.ST.size();
			for(int i=0; i < size; i++){ 
				if( i != size -1){
					buf.append(ST.get(i) +":");// ST间的分隔符用 ：
				}else{
					buf.append(ST.get(i) +"|");
				}
			}
			
			
			size = this.term.size() - 1;//term至少一个
			for (int i = 0; i < size; i++) {
				buf.append(term.get(i) + "|");
			}
			buf.append(term.get(size));
			this.ConceptCache = buf.toString();
			return ConceptCache;
		}
	}

	public static Concept String2Concept(String strConcept) {
		String parts[] = strConcept.split("\\|");
		Concept c = new Concept();
		c.setUI(parts[0]);
		String sts[] = parts[1].split(":");
		for(int i=0; i < sts.length; i++){
			c.addST(sts[i]);
		}
		
		for (int i = 2; i < parts.length; i++) {
			String cnen[] = parts[i].split(":");
			c.addTerm(new Term(cnen[0], cnen[1]));
		}
		return c;
	}

	public static void main(String args[]) {
		String xx ="UI|st:st1|jj|yezh|yezh";
		String parts[] = xx.split("\\|");
		for(int i=0;i <parts.length; i++)
		{
			System.out.println(parts[i]);
		}
		Concept c = new Concept();
		c.setUI("UI");
		c.addST("st");
		c.addST("st1");
		c.addTerm("yezh");
		c.addTerm("yezh");
		c.addPreferTerm("jj");
		System.out.println(c.term.get(0).termValue);
		System.out.println(c.term.get(1).termValue);
		System.out.println(c.term.get(2).termValue);
		
		String cs = c.toString();
		Concept c1 = Concept.String2Concept(cs);
		System.out.println(cs);
		System.out.println(c1);
	}

}
