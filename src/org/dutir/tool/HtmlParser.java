package org.dutir.tool;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.html.dom.HTMLDocumentImpl;
import org.cyberneko.html.parsers.DOMFragmentParser;
import org.cyberneko.html.parsers.DOMParser;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class HtmlParser {

	private static DOMFragmentParser parser = new DOMFragmentParser();
	private static DOMParser dparser = new DOMParser();
	
	public static String getText(byte[] content)
	{
		try {
			DocumentFragment node = new HTMLDocumentImpl().createDocumentFragment();
			parser.parse(new InputSource(new ByteArrayInputStream(content)), node);
			StringBuffer buf = new StringBuffer();
			getText(buf, node);
			return buf.toString();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	public static String getFullText(byte[] content)
	{
		try {
			dparser.parse(new InputSource(new ByteArrayInputStream(content)));
			Node node = dparser.getDocument();
			StringBuffer buf = new StringBuffer();
			getText(buf, node);
			return buf.toString();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static String getText(String fragText)
	{
		return getText(fragText.getBytes());
	}
	public static void getText(StringBuffer sb, Node node) {
		if (node.getNodeType() == Node.TEXT_NODE) {
			sb.append(node.getNodeValue());// 取得结点值，即开始与结束标签之间的信息
		}
		//		System.out.println(node.getNodeName());
		NodeList children = node.getChildNodes();
		if (children != null) {
			int len = children.getLength();
			for (int i = 0; i < len; i++) {
				Node nd = children.item(i);
					getText(sb, nd);// 递归遍历DOM树
			}
		}
	}
	
	public static boolean getTitle(StringBuffer sb, Node node) {
		if (node.getNodeType() == Node.ELEMENT_NODE) {
			if ("title".equalsIgnoreCase(node.getNodeName())) {
				getText(sb, node);
				return true;
			}
		}
		NodeList children = node.getChildNodes();
		if (children != null) {
			int len = children.getLength();
			for (int i = 0; i < len; i++) {
				if (getTitle(sb, children.item(i))) {
					return true;
				}
			}
		}
		return false;
	}
	/**
	 * 直接得到node中的某个特定标签NodeName的节点
	 * 重点：该递归程序
	 * @param node
	 * @param NodeName
	 * @return
	 */
	private static Node getANode(Node node, String NodeName)
	{
		if (node.getNodeName().equalsIgnoreCase(NodeName)) {
			return node;
		} else {
			NodeList nl = node.getChildNodes();
			for (int i = 0; i < nl.getLength(); i++) {
				Node nd = null;
				if ((nd = getANode(nl.item(i), NodeName)) != null) {
					return nd;
				}
			}
		}
		return null;
	}
	/**
	 * 分析Trec语料中的title片段，
	 * @author yezheng
	 * @throws SAXException
	 * @throws IOException
	 */
	public static void parseTitleFrag(byte[] content) throws SAXException,
			IOException {
		DocumentFragment node = new HTMLDocumentImpl().createDocumentFragment();
		//解析HTML内容
		parser.parse(new InputSource(new ByteArrayInputStream(content)), node);

		Node h2Node = null;
		h2Node = getANode(node, "H2");
		if (h2Node != null) {
			//	    	  System.out.println(h2Node.getNodeName());
			//取得全部文本内容
			StringBuffer sb = new StringBuffer();
			getText(sb, h2Node);
			String text = sb.toString();
			System.out.println("title: " + text);
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String text = "<b>Napoleon I</b> of France - Wikipedia, the free encyclopedia";
		System.out.println(getText(text));
	}

}
