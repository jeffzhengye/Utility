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
public class Des implements Serializable{

	public String MH;
	public String UI = "";

	public String RECTYPE = "D";

	public ArrayList<Concept> conceptList;

	public ArrayList<String> TreeNumber;


	String CacheDes = null;

	public static void println(String text)
	{
		System.out.println(text);
	}
	/**
	 * 
	 */
	public Des() {
		// TODO Auto-generated constructor stub
		this.init();
	}

	private void init() {
		this.conceptList = new ArrayList<Concept>(8);
		TreeNumber = new ArrayList<String>(4);
	}

	public void addConcept(Concept c) {
		this.conceptList.add(c);

	}

	public void addTreeNumber(String treenumber) {
		this.TreeNumber.add(treenumber);
	}

	public void addPreferConcept(Concept c) {
		this.conceptList.add(0, c);
	}

	public void setMainHeading(String mh) {
		this.MH = mh;
	}

	/**
	 * 把该Des里所有内容转换成string， 要求：还必须能用通过改string 构造一个Des 类(即实现函数stringToDes)
	 */
	public String toString() {
		if (this.CacheDes != null) {
			return this.CacheDes;
		} else {
			StringBuffer buf = new StringBuffer();
			buf.append(this.UI + "@");
			buf.append(this.MH + "@");
			buf.append(this.RECTYPE + "@");

			int tsize = this.TreeNumber.size();
			buf.append(Integer.toString(tsize) + "@");// 该数字用于正确分割TreeNumber
			for (int i = 0; i < tsize; i++) {
				buf.append(this.TreeNumber.get(i) + "@");
//				println(buf.toString());
			}

			int size = this.conceptList.size() - 1;
			for (int i = 0; i < size; i++) {
				buf.append(this.conceptList.get(i).toString() + "@");
			}
			buf.append(this.conceptList.get(size).toString());
//			println(buf.toString());
			this.CacheDes = buf.toString();
			return this.CacheDes;
		}
	}

	public static Des String2Des(String DesString) {
		
		String parts[] = DesString.split("@");
		Des des = new Des();
		des.UI = parts[0];
		des.MH = parts[1];
		des.RECTYPE = parts[2];
		int treeNumeCount = Integer.parseInt(parts[3]);
		for(int i=0; i < treeNumeCount; i++){
			des.TreeNumber.add(parts[4+i]);
		}
		for(int i = treeNumeCount+ 4; i < parts.length; i++)
		{
			des.addConcept(Concept.String2Concept(parts[i]));
		}
		return des;
	}
	
	public String[] getTerms()
	{
		return getTerms(false);
	}
	
	public String[] getTerms(Boolean filter)
	{
		int size = this.conceptList.size();
		ArrayList<String> strterm = new ArrayList<String>();
		for (int i = 0; i < size; i++) {
			Concept cspt = this.conceptList.get(i);
			//此处加入过滤信息
			String str[] = new String[cspt.term.size()];
			for(int j=0;j < str.length; j++)
			{
				str[j] = cspt.term.get(j).termValue;
				strterm.add(str[j]);
			}
		}
		String tstr[] = new String[strterm.size()];
		strterm.toArray(tstr);
		return tstr;
	}
	
	public String getTreeNumber()
	{
		StringBuffer buf = new StringBuffer();
		int len = this.TreeNumber.size();
		for(int i=0; i < len; i++){
			buf.append(this.TreeNumber.get(i) + " ");
		}
		return buf.toString().trim();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public String getMainHeading() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setUI(String string) {
		this.UI = string;
		
	}
}
