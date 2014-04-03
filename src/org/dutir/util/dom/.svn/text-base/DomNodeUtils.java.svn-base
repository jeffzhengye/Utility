/**
 *  Created on 2006-6-28 23:42:54
 */
package org.dutir.util.dom;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.html.dom.HTMLDocumentImpl;
import org.cyberneko.html.parsers.DOMFragmentParser;
import org.cyberneko.html.parsers.DOMParser;
import org.dutir.parser.DomNodeHandle;
import org.dutir.util.LogFormatter;
import org.dutir.util.Pair;
import org.dutir.util.net.HtmlFormSelect;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


/**
 * 
 * @author YeZheng
 */
public class DomNodeUtils {
	private static final Logger log = LogFormatter.getLogger(DomNodeUtils.class);
	private static DOMFragmentParser parser = new DOMFragmentParser();
	private static DOMParser dparser = new DOMParser();
	
	
	/**
	 * parser a HTMLFragment into w3c DOM Node
	 * @param bcontents
	 * @param charset
	 * @return
	 */
	public static Node parserFragment(byte[] bcontents, String charset){
		try {
			return parserFragment(new StringReader(new String(bcontents, charset)));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static Node parserFragment(Reader reader){
		try {
			DocumentFragment node = new HTMLDocumentImpl().createDocumentFragment();
			parser.parse(new InputSource(reader), node);
			StringBuffer buf = new StringBuffer();
			getText(buf, node, 0);
			return node;
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * skip the "#text" node
	 * @param node
	 * @return
	 */
	public static Node getNextSibling(Node node){
		Node nd = node.getNextSibling();
		if(nd != null &&nd.getNodeName().equals("#text")){
			return getNextSibling(nd);
		}
		return nd;
	}
	
	/**
	 * A framework used to process diversity of nodes  
	 * @param node
	 */
	public static void recTravel(Node node, DomNodeHandle handle){
//		System.out.println(node.getNodeName());
		if(!handle.handle(node)){//if the node is not what we are interested, retrieve its chinldNodes 
			NodeList nlist = node.getChildNodes();
			int len = nlist.getLength();
			for(int i=0; i < len; i++){
				Node nd = nlist.item(i);
				recTravel(nd, handle);
			}
		}
	}
	
	static private boolean scriptdelTag = true;
	public static void setscriptdelTag(boolean tag){
		scriptdelTag = tag;
	}
	
	/**
	 * Get all the text, include text in childNode
	 * @param sb
	 * @param node
	 */
	public static void getText(StringBuffer sb, Node node, int iterator) {
		if(node ==null) return;
		String nodeName = null;
		try {
			nodeName = node.getNodeName();
		} catch (Exception e) {
			return ;
		}
		
		//delete the SCRIPT text 
		if(scriptdelTag && nodeName != null && nodeName.equalsIgnoreCase("SCRIPT")){
			return;
		}
		
		if (node.getNodeType() == Node.TEXT_NODE) {
			sb.append(node.getNodeValue());// ȡ�ý��ֵ������ʼ������ǩ֮�����Ϣ
		}
		//		System.out.println(node.getNodeName());
		NodeList children = node.getChildNodes();
		if (children != null) {
			if(iterator < 400 )
			{
				int len = children.getLength();
				for (int i = 0; i < len; i++) {
					Node nd = children.item(i);
					getText(sb, nd, iterator +1);// �ݹ����DOM��
				}
			}else{
				log.warning("DomNodeUtils: the depth of this two large (iterator>400)");
			}

		}
	}
	
	public static void getText(StringBuffer sb, Node node, String tag) {
		getText(sb, node, tag, 0);
	}
	
	public static void getText(StringBuffer sb, Node node, String tag, int iterator) {
		if(node ==null) return;
		String nodeName = null;
		try {
			nodeName = node.getNodeName();
		} catch (Exception e) {
			return ;
		}
		
		//delete the SCRIPT text 
		if(scriptdelTag && nodeName != null && nodeName.equalsIgnoreCase("SCRIPT")){
			return;
		}
		
		if (nodeName.equalsIgnoreCase(tag)) {
			sb.append(getText(node));// ȡ�ý��ֵ������ʼ������ǩ֮�����Ϣ
			return;
		}
		//		System.out.println(node.getNodeName());
		NodeList children = node.getChildNodes();
		if (children != null) {
			if(iterator < Integer.MAX_VALUE )
			{
				int len = children.getLength();
				for (int i = 0; i < len; i++) {
					Node nd = children.item(i);
					getText(sb, nd, tag, iterator +1);// �ݹ����DOM��
				}
			}else{
				log.warning("DomNodeUtils: the depth of this two large (iterator>" +Integer.MAX_VALUE +")");
			}

		}
	}
	
	
	public static String getText(Node node){
		StringBuffer buf = new StringBuffer();
		getText(buf, node, 0);
		return buf.toString();
	}
	
	/**
	 * get text only if the node is a text-like Node, 
	 * see the source, if you want to get a detailed point
	 * @param node
	 * @return
	 */
	public static String getTextContent(Node node) {
		String ret = null;
//		System.out.println("node type:" + node.getNodeType());
		switch (node.getNodeType()) {
		case Node.ELEMENT_NODE:
		case Node.ATTRIBUTE_NODE:
		case Node.ENTITY_NODE:
		case Node.ENTITY_REFERENCE_NODE:
		case Node.DOCUMENT_FRAGMENT_NODE:
			Node child = node.getFirstChild();
			if (child != null) {
				ret = child.getNodeValue();
			}
			break;
		case Node.TEXT_NODE:
		case Node.CDATA_SECTION_NODE:
		case Node.COMMENT_NODE:
		case Node.PROCESSING_INSTRUCTION_NODE:
			ret = node.getNodeValue();
			break;
		case Node.DOCUMENT_NODE:
		case Node.DOCUMENT_TYPE_NODE:
		case Node.NOTATION_NODE:
			ret = null;
			break;
		}
		return ret;
	}

	public static String getTitle(org.w3c.dom.Document domdoc) {
		NodeList nodes = domdoc.getElementsByTagName("title");
		if(nodes== null || nodes.getLength() <1){
			return null;
		}
		return getTextContent(nodes.item(0));
	}
	
	/**
	 * 
	 * @param node
	 * @return
	 * @deprecated Use {@link #getXmlAsString(Node)} instead.
	 */
	public static String toString(Node node) {
		StringBuffer sb = new StringBuffer();
		if (node instanceof Text) {
			if (node.getNodeValue() != null) {
				byte[] bytes = node.getNodeValue().getBytes();
				List<Byte> newBytes = new ArrayList<Byte>();
				for (int i = 0; i < bytes.length; i++) {
					if (bytes[i] == 63) {
						byte[] bs = "&nbsp;".getBytes();
						for (byte b : bs) {
							newBytes.add(b);
						}
					} else {
						newBytes.add(bytes[i]);
					}
				}
				byte[] valueBytes = new byte[newBytes.size()];
				for (int i = 0; i < newBytes.size(); i++) {
					valueBytes[i] = newBytes.get(i).byteValue();
				}
				sb.append(new String(valueBytes));
			}
		} else {
			sb.append("<").append(node.getNodeName());
			NamedNodeMap attrs = node.getAttributes();
			for (int j = 0; j < attrs.getLength(); j++) {
				sb.append(" ").append(attrs.item(j).getNodeName())
						.append("=\"").append(attrs.item(j).getNodeValue())
						.append("\"");
			}
			sb.append(">");
			if (node.hasChildNodes()) {
				NodeList children = node.getChildNodes();
				for (int i = 0; i < children.getLength(); i++) {
					String str = toString(children.item(i));
					if (str != null)
						sb.append(str);
				}
			} else {
				sb.append(node.getNodeValue());
			}
			sb.append("</").append(node.getNodeName()).append(">");
		}
		return sb.toString();
	}

	public static Node getChildNodeByName(Node node, String tag){
		String name = node.getNodeName();
		if(tag.equalsIgnoreCase(name)){
			return node;
		}else{
			NodeList nlist = node.getChildNodes();
			for(int i=0, n = nlist.getLength(); i < n; i++){
				Node nl = nlist.item(i);
				getChildNodeByName(nl, tag);
			}
		}
		return null;
	}
	
	public static String getText(Node node, String tag){
		StringBuffer buf = new StringBuffer();
		getText(buf, node, tag);
		return buf.toString();
	}
	
	public static ArrayList<Pair> getLinks(Node node){
		ArrayList<Pair> retlist = new ArrayList<Pair>();
		getLinks(node, retlist);
		return retlist;
	}
	
	public static void getLinks(Node node, ArrayList<Pair> list) {
		
		if (node != null && node.getNodeName().equalsIgnoreCase("a")) {
			
			NamedNodeMap map = node.getAttributes();
			if(map != null ){
				Node nd = map.getNamedItem("href");
				
				if(nd !=null){
					String link = nd.getNodeValue();
					String anchor = DomNodeUtils.getText(node);
					Pair <String, String> o= new Pair<String, String>(anchor, link);
					list.add(o);
				}else{
					for(int i=0; i< map.getLength(); i++){
						Node tpnd = map.item(i);
						String name = tpnd.getNodeName();
						int pos = name.indexOf(":");
						if(pos != -1){
							if("href".equalsIgnoreCase(name.substring(pos +1))){
								String link = tpnd.getNodeValue();
								String anchor = DomNodeUtils.getText(node);
								Pair <String, String> o= new Pair<String, String>(anchor, link);
								list.add(o);
							}
						}
					}
				}
			}
		} else {
			NodeList nlist = node.getChildNodes();
			for (int i = 0, n = nlist.getLength(); i < n; i++) {
				Node nl = nlist.item(i);
				getLinks(nl, list);
			}
		}
	}
	
	private static Transformer getTransformer() throws TransformerException {
		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer transformer = factory.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		return transformer;
	}

	/**
	 * @param node
	 * @return output of the node's xml string.
	 * @throws TransformerException
	 */
	public static String getXmlAsString(Node node) throws TransformerException {
		Transformer transformer = getTransformer();
		DOMSource source = new DOMSource(node);
		StringWriter xmlString = new StringWriter();
		StreamResult streamResult = new StreamResult(xmlString);
		transformer.transform(source, streamResult);
		return xmlString.toString();
	}

	/**
	 * ��ȡĳ�ڵ��µ��ӽڵ㣨�������ӡ����ڵ㣩�ı�ǩ��ΪtagName�Ľڵ㡣
	 * 
	 * @param node
	 * @param tagName
	 * @return
	 */
	public static NodeList getElementsByTagName(Node node, String tagName) {
		// TODO
		return null;
	}

	/**
	 * A <code>select</code> html element, parse it, and return the html form
	 * select. pair.
	 * 
	 * @param select
	 *            a <code>select</code> html element.
	 * @return the html form select.
	 */
	public static HtmlFormSelect getSelect(Element select) {
		HtmlFormSelect hfs = new HtmlFormSelect();
//		if (log.isDebugEnabled()) {
//			try {
//				log.debug(getXmlAsString(select));
//			} catch (TransformerException e) {
//				log.warn(e);
//			}
//		}
		hfs.setName(select.getAttribute("name"));
		NodeList options = select.getElementsByTagName("option");
		if (options.getLength() == 0) {// Hack it as html and xhtml are not
			// same in capitalization-sensitivity.
			options = select.getElementsByTagName("OPTION");
		}
		List<String> candidateValues = new ArrayList<String>(options
				.getLength());
		List<String> candidateLabels = new ArrayList<String>(options
				.getLength());
		List<String> values = new ArrayList<String>(options.getLength());
		List<String> labels = new ArrayList<String>(options.getLength());
		String value, label;
		for (int i = 0; i < options.getLength(); i++) {
			Element option = (Element) options.item(i);
			value = option.getAttribute("value");
			label = option.getFirstChild().getNodeValue();
			candidateValues.add(value);
			candidateLabels.add(label);

			Node selectedNode = option.getAttributes().getNamedItem("selected");
//			log.debug("is selectedNode null: " + (selectedNode == null));
//			if (selectedNode != null) {
//				values.add(value);
//				labels.add(label);
//			}
		}

		// ���û���κα� selected����ôĬ����ѡ��ĵ�һ����
		if (values.size() == 0 && options.getLength() != 0) {
			Element option = (Element) options.item(0);
			values.add(option.getAttribute("value"));
			labels.add(option.getFirstChild().getNodeValue());
		}

		String[] candidateValueStrings = new String[candidateValues.size()];
		candidateValues.toArray(candidateValueStrings);
		hfs.setCandidateValues(candidateValueStrings);
		String[] candidateLabelStrings = new String[candidateLabels.size()];
		candidateLabels.toArray(candidateLabelStrings);
		hfs.setCandidateLabels(candidateLabelStrings);

		String[] valueStrings = new String[values.size()];
		values.toArray(valueStrings);
		hfs.setValues(valueStrings);
		String[] labelStrings = new String[labels.size()];
		labels.toArray(labelStrings);
		hfs.setLabels(labelStrings);
		return hfs;
	}

//	public static void debug(Log log, Node node) {
//		if (log.isDebugEnabled()) {
//			try {
//				log.debug(getXmlAsString(node));
//			} catch (TransformerException e) {
//				log.debug("Error while get xml as string from a node.", e);
//			}
//		}
//	}
	private static org.dutir.util.dom.HtmlFileToDocument hf2doc = new org.dutir.util.dom.HtmlFileToDocument();
	
	public static void main(String args[]) throws Exception{
		File file = new File("/media/disk/IR/Corpus/TREC/chemistry2009/PA-topics/PA-1/EP-0327505-B2.xml");
		file = new File("../lucene2.4/US-RE28681-E.xml");
//		String content = Files.readFromFile(file, "utf8");
//		String ptest = content.replaceAll("<[/!]?[^>]+>", "");
//		System.out.println(ptest);
		org.w3c.dom.Document domdoc = hf2doc.getDocumentFomeXMLFile(new FileInputStream(file));
		
		String plaincontent = DomNodeUtils.getText(domdoc,"invention-title");
		System.out.println(plaincontent);
	}
	
}
