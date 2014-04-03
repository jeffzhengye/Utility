/**
 * class ObjectSerializer
 */
package org.dutir.util;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.zip.*;
import java.util.*;

/**
 * the main function of this class is to serialize and compress an object
 * @author yezheng
 *@Fun: serializer an object
 *the main function of this class is to serialize and compress an object
 *
 */
public class ObjectSerializer {
		
	
	public static void writerObj(String path, Object obj){
		writeObj(new File(path), obj);
	}
	
	public static void writeObj(File file, Object obj){
		try {
			
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
			oos.writeObject(obj);
			oos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static Object readObj(String path){
		return readObj(new File(path));
	}
	
	public static Object readObj(File file){
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
			Object obj =ois.readObject();
			ois.close();
			return obj;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static byte[] serializeObject_compress(Object pObject){
		return serializeObject(pObject, true);
	}
	
	
	/**
	 * 
	 * @param pObject
	 * @param tag, if true,then compress
	 * @return
	 * @throws Exception
	 */
	public static byte[] serializeObject(Object pObject, boolean tag) {
		byte[] returnValue = new byte[0];
		try {
			ByteArrayOutputStream byteStream = new ByteArrayOutputStream(5000);
			ObjectOutputStream os = new ObjectOutputStream(
					new BufferedOutputStream(byteStream));
			os.flush();
			os.writeObject(pObject);
			os.flush();
			// retrieves byte array
			if (tag) {
				returnValue = compressBytes(byteStream.toByteArray());
			} else {
				returnValue = byteStream.toByteArray();
			}

			os.close();
			os = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (returnValue);

	} // ***End public byte[] serializeObject (Object pObject)

	// ================================================|Public Method
	// Header|====

	
	
	/**
	 * Method unSerializeObject
	 * 
	 * @param pBytes
	 * @param tag, if true, need to uncompress first
	 * @return Object
	 * @exception Exception
	 * 
	 */
	// ================================================|Public Method
	// Header|====
	public static Object unSerializeObject(byte[] pBytes, boolean tag) {
		Object returnValue = null;
		try {
			if (true) {
				pBytes = decompressBytes(pBytes);
			}
			ByteArrayInputStream byteStream = new ByteArrayInputStream(pBytes);
			ObjectInputStream is = new ObjectInputStream(
					new BufferedInputStream(byteStream));
			returnValue = is.readObject();
			is.close();
			byteStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return (returnValue);

	} // ***End public Object unSerializeObject (byte[] pBytes )
	
	public static Object unSerializeObject_decompress(byte[] pBytes)
	{
		return unSerializeObject(pBytes, true);
	}
	
	/**
	 * use class Inflater to decompress data
	 * 
	 * @param input
	 * @return
	 */
	public static byte[] compressBytes(byte input[]) {
		compresser.reset();//do not forgot this statement, 
		compresser.setInput(input);
		compresser.finish();//do not forgot this statement, 
		byte output[] = new byte[0];
		ByteArrayOutputStream o = new ByteArrayOutputStream(input.length);
		try {
			byte[] buf = new byte[cachesize];
			int got;
			while (!compresser.finished()) {
				got = compresser.deflate(buf);
				o.write(buf, 0, got);
			}
			output = o.toByteArray();
//			System.out.println("len after compress: " +output.length);
		} finally {
			try {
				o.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return output;
	}
	/**
	 * use class Deflater to compress data
	 * @param input
	 * @return
	 */
	public static byte[] decompressBytes(byte input[])
	{
		byte output[] = new byte[0];
		decompresser.reset();
		decompresser.setInput(input);
		ByteArrayOutputStream o = new ByteArrayOutputStream(input.length);
		try {
			
			byte[] buf = new byte[cachesize];
			int got;
			while (!decompresser.finished()) {
				got = decompresser.inflate(buf);
				o.write(buf, 0, got);
			}
			output = o.toByteArray();
//			System.out.println("len after decompress: " +output.length);
		}catch(Exception e)
		{
			e.printStackTrace();
		}finally {
			try {
				o.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return output;
	}
	
	public static void test(){
		String inputString = "blahblahblah8888888888888888888888888888888888888888888888999";
		System.out.println(inputString);
		
		byte input[] = inputString.getBytes();
		System.out.println("original len = " + input.length);
		
		byte compressed[] = compressBytes(input);
		System.out.println("compressed length = " + compressed.length);
		byte output[]= decompressBytes(compressBytes(input));
		String outputstring = new String(output);
		System.out.println(outputstring);
	}
	
	public static void testObject()
	
	{
		HashMap map = new HashMap();
		map.put("111111", "v1111111");
		map.put("22222222", "v2111112");
		map.put("333333", "v3333333333");
		map.put("444444444", "v4444444");
		byte byteobj[] = serializeObject_compress(map);
		byte byteobj1[] = serializeObject(map, false);
		System.out.println("size com:" + byteobj.length);
		System.out.println("size uncom:" + byteobj1.length);
		HashMap map1 = (HashMap) unSerializeObject_decompress(byteobj);
		if(map1.containsKey("111111"))
		{
			System.out.println("sucessed");
		}
	}
	
	public static void setCachesize(int size)
	{
		if(size <1 || (size%256) !=0 )
		{
			return;
		}
		cachesize = size;
	}
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		 // Encode a String into bytes
//		 String inputString = "blahblahblah8888888888888888888888888888888888888888888888999";
		test();
		testObject();
	}
	
	
	private static Deflater compresser = new Deflater();
	private static Inflater decompresser = new Inflater();
	private static int cachesize = 1024;

}
