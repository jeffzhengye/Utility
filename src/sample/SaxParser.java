/**
 * 
 */
package sample;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * @author Yezheng
 *
 */
public class SaxParser extends DefaultHandler {

	public SaxParser() {
		// TODO Auto-generated constructor stub
	}

	//文档开头处理，根据实际应用而变
	public void startDocument() {
		System.err.println("document begin");
	}

	//具体元素的处理，qName 元素名，attrs其对于的属性

	public void startElement(String uri, String localName, String qName,
			Attributes attrs) {
		try {
			Thread.sleep(50); //too see the output clearly
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.err.println("before: " + qName);
		if (qName.equalsIgnoreCase("property")) {
			for (int i = 0; i < attrs.getLength(); i++) {
				String value = attrs.getValue(i);
				System.out.print(value + "\t");
			}
			System.out.println();
			System.out.println("uri:"+ uri);
			System.out.println("localName:"+ localName);
			System.out.println();
		}
		
		//  
	}
	
	//Response the endElement event
	public void endElement(String uri, String localName, String qName) {
//		System.err.println("end aName:" + qName);
		System.err.println("element end ");
	}
	
	
	public void endDocument() {
		System.err.println("document end ");
	}
	
	public void characters(char[] arg0, int arg1, int arg2) {
		System.out.println( arg1 + " " +arg2);
		System.out.println("characters: "+new String(arg0, arg1, arg2));
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			XMLReader xmlReader = XMLReaderFactory.createXMLReader();
			xmlReader.setContentHandler(new SaxParser());
			xmlReader.parse(new InputSource("build.xml"));
			//	        sp.parse(new InputSource("film.xml"),reader);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
