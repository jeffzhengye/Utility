/**
 * 
 */
package org.dutir.parser;

import java.io.IOException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.aliasi.corpus.InputSourceParser;

/**
 * 
 * add some operations for xml doc parser
 * @author Yezheng
 *
 */

public class XMLDocParser extends InputSourceParser {

	/* (non-Javadoc)
	 * @see com.aliasi.corpus.Parser#parse(org.xml.sax.InputSource)
	 */
	@Override
	public void parse(InputSource arg0) throws IOException {
		// TODO Auto-generated method stub
		
	}

	
	/**
	 * 获取该节点名为<String>的孩子的value
	 * 
	 * @param node
	 * @return
	 */
	public String getString(Node node) {
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
	 * 获取改节点的值, 注意:节点的值不是直接node.getNodeValue(),而是其孩子的Value
	 * 
	 * @param node
	 * @return
	 */
	public String getValue(Node node) {
		return node.getFirstChild().getNodeValue();
	}
	
}
