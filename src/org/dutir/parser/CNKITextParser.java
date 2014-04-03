///**
// * 
// */
//package org.dutir.parser;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.Reader;
//import java.net.URL;
//import java.util.HashSet;
//import java.util.Set;
//
//import org.dutir.document.CNKIItem;
//import org.dutir.util.*;
//import org.xml.sax.InputSource;
//
//import com.aliasi.corpus.InputSourceParser;
//
//
///**
// * @author yezheng
// *
// */
//public class CNKITextParser extends InputSourceParser {
//
//
//	/* (non-Javadoc)
//	 * @see com.aliasi.corpus.Parser#parse(org.xml.sax.InputSource)
//	 */
//	@Override
//	public void parse(InputSource in) throws IOException {
//		// TODO Auto-generated method stub
//		
//		CNKIHandle handler = (CNKIHandle)getHandler();
//
//			int count =0;
//			
//			Reader reader1 = null;
//	        InputStream inStr = null;
//	        reader1 = in.getCharacterStream();
//	        if (reader1 == null) {
//	            inStr = in.getByteStream();
//	            if (inStr == null)
//	            {
//	            	inStr = new URL(in.getSystemId()).openStream();
//	            }
//	               
//	            String charset = in.getEncoding();
//	            if (charset == null)
//	                reader1 = new InputStreamReader(inStr);
//	            else
//	                reader1 = new InputStreamReader(inStr,charset);
//	        }
//			BufferedReader reader = new BufferedReader(reader1);
//			
//			String line = reader.readLine();
//			while (line != null && !line.startsWith("<REC>")) {
//				//System.out.println("format error : " + line);
//				line = reader.readLine();
//			}
//			if (line == null) {
//				return ;
//			}
//
//			CNKIItem token = new CNKIItem();
//			
//			boolean isValidJournal = false;
//			boolean isRead = true;
//			while (true) {
//				
//				if(isRead)
//					line = reader.readLine();
//				
//				if (line == null)
//					return ;
//				if (line.trim().length() == 0)
//					continue;
//				else if (line.startsWith("<REC>"))
//				{
//					handler.handle(token);
//					token = new CNKIItem();
//					continue;
//				}
//					
//
//				if (line.startsWith("<Ӣ��ժҪ>=")) {
//					
//					token.enAbstract = line.substring(7);
//					line = reader.readLine().trim();
//					while (line.trim().length() == 0 || !line.startsWith("<")) {
//						token.enAbstract += line;
//						line = reader.readLine();
//					}
//					isRead = false;
//					
//				} else if (line.startsWith("<����ժҪ>")) {
//
//					token.cnAbstract = line.substring(7);
//					if(token.cnAbstract.startsWith("<��>")){
//						token.cnAbstract = token.cnAbstract.substring(3);
//					}
//					line = reader.readLine().trim();
//					while (line.length() == 0 || !line.startsWith("<")) {
//						token.cnAbstract += line;
//						line = reader.readLine();
//					}
//					isRead = false;
//					
//				} else if (line.startsWith("<ƪ��>=")) {
//					
//					token.cnTitle = line.substring(5);
//					isRead = true;
//				}
//				else if (line.startsWith("<���Ĺؼ��>=")){
//					
//					token.cnKeywords = line.substring(8);
//					isRead = true;
//				}
//				else if (line.startsWith("<���Ŀ���>=")) {
//					
//					token.journal = line.substring(7);
////					if (limitedJournal.contains(token.journal))
//						isValidJournal = true;
//					isRead = true;
//					
//				} else if (line.startsWith("<��>=")) {
//					
//					token.date = line.substring(4);
////					if (!isValidJournal)
////						token.clear();
////					else if (isApproximateChinese(token.cnAbstract))
////						break;
//					isRead = true;
//					
//				} else if (line.startsWith("<Ӣ��ƪ��>=")) {
//
//					token.enTitle = line.substring(7);
//					line = reader.readLine().trim();
//					while (line.length() == 0 || !line.startsWith("<")) {
//						token.enTitle += line;
//						line = reader.readLine();
//					}
//					isRead = false;
//				} else if (line.startsWith("<Ӣ�Ĺؼ��>=")) {
//					token.enKeywords = line.substring(8);
//					isRead = true;
//				} else if(line.startsWith("<��>")){
//					isRead = true;
//				}
//				else{
//					System.out.println("Error: " + line);
//					isRead = true;
//				}
//			}
//	}
//
//	/**
//	 * @param args
//	 */
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//
//	}
//
//}
