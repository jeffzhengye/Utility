/**
 * 
 */
package org.dutir.document.wikipedia;

import java.util.ArrayList;

import org.dutir.util.Pair;

/**
 * 
 * @author Yezheng
 * 
 */
public class WikiPage {
	public static int Article = 0;
	public static int IndexArticle = 1;
	public static int Category = 2;
	public static int Others = -1;
	
	private  boolean setTag = false;
	private  boolean lanTag = false;
	private  boolean titleTag = false;
	
	
	private static ArrayList<String> EndList = null;
	private static ArrayList<String> StartList = null;
	static {
		EndList = new ArrayList<String>();
		EndList.add(" - 维基百科，自由的百科全书");
		EndList.add(" - Wikipedia");
		EndList.add("- Wikipedia, the free encyclopedia");
		
		StartList = new ArrayList<String>();
		StartList.add("Image:");
		StartList.add("User talk:");
		StartList.add("Category:");
		StartList.add("Template:");
		StartList.add("(miss)");
		StartList.add("Talk:");
		StartList.add("User:");
//		StartList.add("(miss)");
//		StartList.add("(miss)");
		
		
	}

	String title = null;
	String rawtext = null; // store the raw text
	String text = null;
	ArrayList<WikiSection> secList;
	int KindOfPage = Article;
	ArrayList<Pair> LanLinks ;
	public WikiPage() {

	}

	public void setKind(int kind) {
		this.KindOfPage = kind;
	}

	public int getKind() {
		return this.KindOfPage;
	}

	public ArrayList<WikiSection> getSection() {
		return secList;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
//		System.err.println("origin:" + title);
		for (int i = 0, n = StartList.size(); i < n; i++) {
			if (title.startsWith(StartList.get(i))) {
				this.KindOfPage = Others;
				this.title = title;
				return;
			}
		}
		//
		for (int i = 0, n = EndList.size(); i < n; i++) {
			String endstr = EndList.get(i);
			if (title.endsWith(endstr)) {
				this.title = title.substring(0, title.length()
						- endstr.length());
				this.KindOfPage = Article;
			}
		}
		this.setTitleTag(true);
//		System.err.println("page title:" + this.getTitle());
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public WikiPage(String title, String text) {
		this.title = title;
		this.text = text;
	}

	public class WikiSection {
		public WikiSection(String name, String text2, ArrayList<Pair> list) {
			// TODO Auto-generated constructor stub
			this.subtitle = name;
			this.stext = text2;
			this.linkList = list;
		}

		String subtitle;
		String stext;
		ArrayList<Sentence> sentences;
		ArrayList<Pair> linkList;
		public String toString(){
			StringBuffer buf = new StringBuffer();
			buf.append("\tsecTitle: " + subtitle + " \n");
			buf.append(this.stext+ " \n");
			buf.append("Links:" + this.linkList.size() +"\n");
			for(int i=0, n = linkList.size(); i < n; i++){
				buf.append("link " + i + " :" + this.linkList.get(i).first + " : " + this.linkList.get(i).second + "\n");
			}
			return buf.toString();
		}
	}

	public class Sentence {
		String text = null;
		int start = -1, end = -1;
		ArrayList<String> concept;
	}

	public void addSection(String name, String text2, ArrayList<Pair> list) {
		// TODO Auto-generated method stub
		if (this.secList == null) {
			this.secList = new ArrayList<WikiSection>();
		}
		this.secList.add(new WikiSection(name, text2, list));
	}

	public static void main(String args[]) {
		for (int i = 0; i < 10; i++) {
			System.out.println(i);
			if (i > 5) {
				return;
			}
		}
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("Title: " + this.title + "\n");
		for (int i = 0, n = this.getSection().size(); i < n; i++) {
			String sec = this.getSection().get(i).toString();
			buf.append(sec);
		}
		return buf.toString();
	}

	public void print() {
		// TODO Auto-generated method stub
		System.out.println(toString());
	}



	public ArrayList<Pair> getLanLinks() {
		return LanLinks;
	}

	public void setLanLinks(ArrayList<Pair> lanLinks) {
		LanLinks = new ArrayList<Pair>(lanLinks);
	}

	public boolean isLanTag() {
		return lanTag;
	}

	public void setLanTag(boolean lanTag) {
		this.lanTag = lanTag;
	}
	public boolean isTitleTag() {
		return titleTag;
	}

	public void setTitleTag(boolean tTag) {
		this.titleTag = tTag;
	}
	
	/**
	 * @param setTag the setTag to set
	 */
	public void setSetTag(boolean setTag) {
		this.setTag = setTag;
	}

	/**
	 * @return the setTag
	 */
	public boolean isSetTag() {
		return setTag;
	}
	
	public String getEnLanguageLink() {
		// TODO Auto-generated method stub
		if(LanLinks == null)return null;
		int pos = -1; LanLinks.indexOf("English");
		for(int i=0, n = LanLinks.size(); i < n; i++){
			if("English".equals((String)LanLinks.get(i).first)){
				pos = i;
				break;
			}
		}
		if(pos == -1){
			return null;
		}else{
			String sourceURL = (String)LanLinks.get(pos).second;
			String retURL = null;
			int epos = sourceURL.lastIndexOf("/");
			boolean tag = false;
			if(sourceURL.endsWith(".html"))
			{
				tag = true;
			}
			if(epos != -1){
				if(tag){
					retURL = BGENurl + sourceURL.substring(epos +1, sourceURL.length() - 5);
				}else{
					retURL = BGENurl + sourceURL.substring(epos +1);
				}
			}else{
				retURL = BGENurl + sourceURL;
			}
				
			return retURL.replace("%E2%88%92", "-");
		}
	}
	private static String BGENurl = "http://en.wikipedia.org/wiki/";
}
