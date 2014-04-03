/**
 * 
 */
package org.dutir.parser;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.dutir.document.CNKIItem;
import org.dutir.document.mesh.Concept;
import org.dutir.document.mesh.Des;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.aliasi.corpus.InputSourceParser;

/**
 * used to parser MeSH dictionary in XML format.
 * @author yezheng
 *
 */

public class MeSHParser extends InputSourceParser {

	
	static DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
	static DocumentBuilder builder= null;
	static {
		try {
			builder=factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/* (non-Javadoc)
	 * @see com.aliasi.corpus.Parser#parse(org.xml.sax.InputSource)
	 */
	@Override
	public void parse(InputSource in) throws IOException {
		// TODO Auto-generated method stub
		MeSHHandle handler = (MeSHHandle)getHandler();
		int count =0;
		Reader reader1 = null;
        InputStream inStr = null;
        reader1 = in.getCharacterStream();
        if (reader1 == null) {
            inStr = in.getByteStream();
            if (inStr == null)
            {
            	inStr = new URL(in.getSystemId()).openStream();
            }
            String charset = in.getEncoding();
            if (charset == null)
                reader1 = new InputStreamReader(inStr);
            else
                reader1 = new InputStreamReader(inStr,charset);
        }
		BufferedReader br = new BufferedReader(reader1);
		
		String line = null;
		while(true){
			StringBuffer sb = new StringBuffer();
			sb.append("<?xml version=\"1.0\"?>" + "\n");
			boolean flag = false;
			try {
				while ((line = br.readLine()) != null) {
					if (line.indexOf("<DescriptorRecord DescriptorClass") != -1
							|| flag == true) {
						sb.append(line + "\n");
						flag = true;
					}
					if (line.indexOf("</DescriptorRecord") != -1) {
						// sb.append(line);
						flag = false;
						break;
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(null ==line){
				break ;
			}
			
			ByteArrayInputStream stream = new ByteArrayInputStream(sb.toString()
					.getBytes());
			org.w3c.dom.Document doc = null;
			try {
				doc = builder.parse(stream);
			} catch (Exception e) {
				return ;
			}
			
			NodeList nl = doc.getChildNodes();
			Des des = new Des();
			processNodeList(nl, des);
			handler.handle(des);
		}
	}

	
	private void processNodeList(NodeList nl, Des des) {
		// TODO Auto-generated method stub
		if (null == nl) {
			return;
		}
		for (int i = 0; i < nl.getLength(); i++) {
			Node node = nl.item(i);
			String name = node.getNodeName();
			if (name.equalsIgnoreCase("DescriptorUI")) {
				if (node.getParentNode().getNodeName().equals(
						"DescriptorRecord")) {

					String svalue = node.getFirstChild().getNodeValue();

					// println("DescriptorUI:" + svalue);
					des.setUI(svalue);
				}
				// System.out.println("DescriptorUI:" + );
			} else if (name.equalsIgnoreCase("DescriptorName")) {
				if (node.getParentNode().getNodeName().equals(
						"DescriptorRecord")) {
					String svalue = getString(node);
					// println("DescriptorName:" + svalue);
					des.setMainHeading(getString(node));
				}
			} else if (name.equalsIgnoreCase("TreeNumber")) {
				des.addTreeNumber(node.getFirstChild().getNodeValue());
			} else if (name.equalsIgnoreCase("Concept")) {
				// println(node.getNodeValue()); //只有名为#text才有value, (maybe)
				Concept cspt = new Concept();
				processConceptNode(node, cspt);
				des.addConcept(cspt);
			} else {
				NodeList cnl = node.getChildNodes();
				processNodeList(cnl, des);
			}
			// System.out.println(name);
		}
	}
	
	private void processConceptNode(Node node, Concept cspt) {
		// TODO Auto-generated method stub
		// NamedNodeMap map = node.getAttributes();
		// map.getNamedItem("PreferredConceptYN");
		if (null == node) {
			return;
		}
		String name = node.getNodeName();
		if (name.equalsIgnoreCase("SemanticTypeUI")) {
			String svalue = node.getFirstChild().getNodeValue();
			// println("SemanticTypeUI:" + svalue);
			cspt.addST(svalue);
		} else if (name.equalsIgnoreCase("ConceptUI")) {
			cspt.setUI(getValue(node));
		} else if (name.equalsIgnoreCase("Term")) {
			String sterm = getString(node);
			// println(sterm);
			cspt.addTerm(sterm);
		} else {
			NodeList cnl = node.getChildNodes();
			for (int i = 0; i < cnl.getLength(); i++) {
				this.processConceptNode(cnl.item(i), cspt);
			}
		}

	}
	/**
	 * 获取该节点名为<String>的孩子的value
	 * 
	 * @param node
	 * @return
	 */
	private String getString(Node node) {
		if (node == null) {
			return null;
		}
		NodeList ndlist = node.getChildNodes();
		for (int j = 0; j < ndlist.getLength(); j++) {
			Node nd = ndlist.item(j);
			if (nd.getNodeName().equalsIgnoreCase("String")) {
				return nd.getFirstChild().getNodeValue();
			}
		}
		return "";
	}

	/**
	 * 获取改建点的值, 注意:节点的值不是直接node.getNodeValue(),而是其孩子的Value
	 * 
	 * @param node
	 * @return
	 */
	private String getValue(Node node) {
		return node.getFirstChild().getNodeValue();
	}
}
