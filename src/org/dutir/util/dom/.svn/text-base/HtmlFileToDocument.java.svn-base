/**
 * 
 */
package org.dutir.util.dom;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.cyberneko.html.parsers.DOMParser;
import org.dutir.util.stream.StreamManager;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * 
 * Fun: generate org.w3c.dom.Document from xml file or html files.
 * @author YeZheng
 * 
 */
public class HtmlFileToDocument {
	/**
	 * Get the xml document from a url.
	 * 
	 * @param url
	 * @param encoding
	 * @return
	 * @throws IOException
	 * @throws SAXException
	 */
	public Document getDocument(URL url, String encoding) throws IOException,
			SAXException {
		InputStream inputStream = url.openStream();
		try {
			return getDocument(inputStream, encoding);
		} finally {
			inputStream.close();
		}
	}

	/**
	 * Get the xml document from a html file.
	 * 
	 * @param file
	 * @param encoding
	 * @return
	 * @throws SAXException
	 * @throws IOException
	 */
	public Document getDocument(File file, String encoding)
			throws SAXException, IOException {
		FileInputStream inputStream = new FileInputStream(file);
		try {
			return getDocument(inputStream, encoding);
		} finally {
			inputStream.close();
		}
	}

	/**
	 * Get the xml document from a html input stream.
	 * 
	 * @param inputStream
	 * @param encoding
	 * @return
	 * @throws SAXException
	 * @throws IOException
	 */
	public Document getDocument(InputStream inputStream, String encoding)
			throws SAXException, IOException {
		Reader reader = new InputStreamReader(inputStream, encoding);
		return getDocument(reader);
	}

	public Document getDocument(byte[] contents, String encoding) throws SAXException, IOException{
		
		ByteArrayInputStream bis = null ;
		try{
			bis = new ByteArrayInputStream(StreamManager.replaceAll(contents, Oldb, Newb));
			return getDocument(bis, encoding);
		}finally{
			bis.close();
		}
	}
	static byte[] Oldb = new byte[]{(byte)0xe2,(byte) 0x88, (byte) 0x92};
	static byte[] Newb = new byte[]{0x2d};
//	byte a =  0xe2;
	/**
	 * Get the xml dom document from a reader.
	 * 
	 * @param characterStream
	 * @return the xml dom of the character stream.
	 * @throws SAXException
	 * @throws IOException
	 */
	public Document getDocument(Reader characterStream) throws SAXException,
			IOException {
		DOMParser parser = new DOMParser();
		// parser.setFeature(
		// "http://apache.org/xml/features/scanner/notify-builtin-refs",
		// true);
		InputSource inputSource = new InputSource();
		inputSource.setCharacterStream(characterStream);
		parser.parse(inputSource);
		return parser.getDocument();
	}
	
    static DocumentBuilder builder = null;
    static DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        
    static {
    	try {
    		factory.setValidating(false);
    		factory.setIgnoringComments(true);
//    		factory.setIgnoringElementContentWhitespace(true);
//    		factory.setAttribute("http://xml.org/sax/features/validation", Boolean.FALSE); 
    		factory.setAttribute("http://apache.org/xml/features/nonvalidating/load-external-dtd", Boolean.FALSE);
			builder = factory.newDocumentBuilder();
//			System.out.println(builder.isValidating());
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
	public Document getDocumentFomeXMLFile(InputStream is){
	        Document doc = null;
			try {
				doc = builder.parse(is);
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return doc;
	        
	}
	
}
