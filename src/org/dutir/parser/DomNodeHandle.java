/**
 * 
 */
package org.dutir.parser;

import org.w3c.dom.Node;

/**
 * 
 * @author Yezheng
 *
 */
public interface DomNodeHandle {
	/**
	 * 
	 * @param node
	 * @return true:   interested node that will be handled in DomNodeHandle
	 *         false:  unwanted node, so we recursively visit its children.  
	 */
	public boolean handle(Node node);
}
