/**
 * 
 */
package org.dutir.web;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author yezheng
 *
 */
public class opense {

	private static int baidu = 0;

	private static int google = 1;
	private static int yodaodic = 2;
	private static int ilib = 3;
	
	public static String getUrl(String query, int seid, int start) {
		try{
			if(seid == 0){
				String q =null;// query.replace(" ","+");
				q = URLEncoder.encode(query);
				
			    return "http://www.baidu.com/s?lm=0&si=&rn=10&ie=gb2312&ct=0&wd="+q+"&pn="+10*start;
			}else if(seid ==1){
				//return "http://www.google.cn/search?q=" + URLEncoder.encode(query, "utf8") + "&complete=1&hl=zh-CN&newwindow=1&start=" + 10*start +"&sa=N";
				 	return "http://www.google.cn/search?q=" + URLEncoder.encode(query, "utf8") + "&complete=1&hl=zh-CN&lr=lang_zh-CN%7Clang_zh-TW&newwindow=1&start=" +10*start +"&sa=N";
			}else if(seid == 2){
				return "http://dict.yodao.com/search?cl=3&keyfrom=dict.index&q=" + URLEncoder.encode(query, "utf8");
			}else if(seid == 3){
				return "http://service.ilib.cn/Search/Search.aspx?Query=" + URLEncoder.encode(query, "utf8")+ "&P="+ start;
			}
		}catch(Exception e){
			
		}
		return null;
	}

	public static byte[] getContents(String query, int seid, int start) {
		InputStream urlStream = null;
		String surl = getUrl(query ,seid, start);
		System.out.println(surl);
		URL url;
		try {
			url = new URL(surl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
			long startTime = System.currentTimeMillis();
			conn.connect();
			urlStream = conn.getInputStream();
			String resp = conn.getResponseMessage();

			String respStr = conn.getHeaderField(0);

			for (int i = 1;; i++) {
				String key = conn.getHeaderFieldKey(i);
				if (key == null) {
					break;
				}
				String value = conn.getHeaderField(key);
			}
			BufferedInputStream remoteBIS = new BufferedInputStream(conn
					.getInputStream());
			ByteArrayOutputStream baos = new ByteArrayOutputStream(10240);
			byte[] buf = new byte[1024];
			int bytesRead = 0;
			//把从remoteBIS读出得数据写到输出流baos中
			while (bytesRead >= 0) {
				baos.write(buf, 0, bytesRead);
				bytesRead = remoteBIS.read(buf);
			}

			byte[] content = baos.toByteArray();
			long timeTaken = System.currentTimeMillis() - startTime;
			if (timeTaken < 100)
				timeTaken = 500;
			//计算网速
			//int bytesPerSec = (int) ((double) content.length / ((double) timeTaken / 1000.0));
			return baos.toByteArray();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				Thread.sleep(60000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return getContents(query, seid, start);
		}
	}
	public static void save(String query,String dir){
		
	}
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		String query = "疯牛病";
		byte[] b = getContents(query,3, 1);
		String cts = new String(b, "utf-8");
		System.out.println( cts);
//		String regex = "<td class=\"attributem1web\">(.+)</td>"; //dict 
//		String regex1=  "<font color=\"#013694\"><b>(.+)</b></font>"; //net1
//		String regex2 = "<font class=\"p13\"><font color=\"#013694\">([^<]+)</font>";
//		Pattern dictPattern = Pattern.compile(regex);
//		Pattern netPattern = Pattern.compile(regex1);
//		Pattern netPattern2 = Pattern.compile(regex2);
//		
//		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
//		String sentence = null;
//		
//		while(true)
//		{
//			System.out.print("Query:\t");
//			sentence = reader.readLine();
//			if(sentence.trim().length() == 0)
//				continue;
//			else				
//				if(sentence.toLowerCase().equals("exit"))
//				break;
//			
//			///////////////////////////////////////////////////////
//			byte[] b = getContents(sentence,2, 0);
//			String cts = new String(b, "utf-8");
//			System.out.println( cts);
//			Matcher dictm=dictPattern.matcher(cts);
//
//			Matcher netm=netPattern.matcher(cts);
//			
//			Matcher netm2=netPattern2.matcher(cts);
//			
//			String mstring = cts;
//			while(netm.find()){
//				System.out.println("net:"+ netm.group(1));
//				mstring = mstring.substring(netm.end());
//				netm=netPattern.matcher(mstring);
//			}
//			
//			if(netm2.find())
//			{	
//				int len = netm2.groupCount();
//				for(int i=0; i< len; i++)
//				{
//					System.out.println("net2:" +netm2.group(i+1));
//				}
//			}else{
//				System.out.println("can not found net2 sence");
//				
//			}
//			
//			if(dictm.find())
//			{	
//				int len = dictm.groupCount();
//				for(int i=0; i< len; i++)
//				{
//					System.out.println("dict:" +dictm.group(i+1));
//				}
//			}else{
//				System.out.println("can not found dict sence");
//				
//			}
//			
//		}
		

	}

}
