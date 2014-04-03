/**
 * 
 */
package org.dutir.util.stream;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.*;

/**
 * @author Yezheng
 *
 */
public class StreamManager {
	
	static int NEWLINE = '\n';
	static int ENTER = '\r';
	
	
	
	public static String readline(InputStream is, String charset){
		byte[] bline = readline(is, false);
		try {
			if(bline == null){
				return null;
			}
			return new String(bline, charset);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static byte[] readline(InputStream is, boolean with_newLineMark){
//		ByteBuffer buf = ByteBuffer.allocate(128);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int c;
		try {
			int len =0;
			while((c = is.read()) != -1){
				len ++;
				if(c == ENTER || c == NEWLINE){
					if(c == ENTER){
						if(with_newLineMark){
							baos.write(c);
//							buf.put((byte) c); 
							baos.write((byte) is.read());
//							buf.put((byte) is.read()); 
						}else{
							is.skip(1);
						}
					}else{ // for sure, c ==NEWLINE
						if(with_newLineMark){
							baos.write(c);
//							buf.put((byte) c); 
						}
					}
					break;
				}else{
					try{
						baos.write(c);
//						buf.put((byte) c); 
					}catch(Exception e){
//						System.out.println(buf.arrayOffset());
//						System.out.println(len);
						e.printStackTrace();
						System.exit(1);
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		return buf.array();
		byte[] retbs = baos.toByteArray();
		if(retbs == null || retbs.length < 1){
			return null;
		}
		return retbs;
	}
	
	
	public static int indexOf(byte[] source, byte[] target){
		int retvalue = -1;
		int slen = source.length, tlen = target.length;
		if(slen < tlen){
			return retvalue;
		}
		
		for(int i=0; i <= (slen -tlen); i++){
			int j=0;
			for(; j < tlen; j ++){
				if(target[j] != source [j + i]){
					break;
				}
			}
			if(j == tlen){//matched
				retvalue = i;
			}
		}
		
		return retvalue;
	}
	
	public static byte[] replaceAll(byte[] source, byte[] oldb, byte[] newb) {
		ByteArrayOutputStream boas = new ByteArrayOutputStream();
		try {
			for (int i = 0; i < source.length;) {
				if(i >=  (source.length - oldb.length)){
					boas.write(source[i++]);
					continue;
				}
				int j = 0;
				for (j = 0; j < oldb.length; j++) {
					if (!(source[i + j] == oldb[j])) {
						break;
					}
				}
				if (j == oldb.length) {
					boas.write(newb);
					i = i +j;
				}else{
					for(int k= 0; k < j +1 ; k++){
						boas.write(source[i++]);
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return boas.toByteArray();
	}
	
	public static byte[] subbytes(byte[] source, int offset, int length){
		byte[] retvalue = new byte[length];
		System.arraycopy(source, offset, retvalue, 0, length);
		return retvalue;
	}
	
	public static String[] extractLines(InputStream in, String mCharEncoding,
			boolean skipTag) {
		ArrayList lineList = new ArrayList();
		InputStreamReader reader = null;
		try {
			reader = new InputStreamReader(in, mCharEncoding);
			BufferedReader bufReader = new BufferedReader(reader);
			String refLine;
			while ((refLine = bufReader.readLine()) != null) {
				if (skipTag) {
					if (refLine.trim().startsWith("##:")) {
						continue;
					}
				}
				lineList.add(refLine);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return (String[]) lineList.toArray(new String[0]);
	}
	
	public static String[] extractLines(InputStream in, String mCharEncoding){
		return extractLines(in, mCharEncoding, false);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		byte[] source = "this is a dog".getBytes();
		byte[] target = "his".getBytes();
		System.out.println("this is a dog");
		System.out.println("his");
//		System.out.println(indexOf(source, target));
//		System.out.println(new String(subbytes(source, 0, 3)));
		System.out.println(new String(replaceAll(source, target, "-".getBytes())));
	}


	public static byte[] subbytes(byte[] source, int offset) {
		// TODO Auto-generated method stub
		return subbytes(source, offset, source.length - offset);
	}


	public static byte[] readline(InputStream fis) {
		// TODO Auto-generated method stub
		return readline(fis, true);
	}

}
