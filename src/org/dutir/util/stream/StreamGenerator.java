package org.dutir.util.stream;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class StreamGenerator {

	static BufferedReader consoleReader = null; 
	static int mbSize = 1024*1024;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public static BufferedReader getConsoleReader()
	{
		if(null == consoleReader){
			consoleReader = new BufferedReader(new InputStreamReader(System.in));
		}
		return consoleReader;
	}
	
	public static BufferedWriter getBufferFileWriter(String path, int mbsize){
		try {
			return new BufferedWriter(new FileWriter(new File(path)),mbsize*mbSize );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static BufferedWriter getBufferFileWriter(String path, String charset, int mbsize){
		try {
			return new BufferedWriter(new OutputStreamWriter(getFOS(path, mbsize*mbSize), charset));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static BufferedReader getBufferFileReader(String path, String charset, int mbsize){
		try {
			return new BufferedReader(new InputStreamReader(new BufferedInputStream(new FileInputStream(path)), charset), mbsize*mbSize);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static BufferedReader getBufferFileReader(String path, int mbsize){
		try {
			return new BufferedReader(new FileReader(new File(path)),mbsize*mbSize );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static ObjectOutputStream getFileOOS(String path ,int mbsize){
		try {
			ObjectOutputStream returnOOS = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(path), mbsize*mbSize));
			return returnOOS;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static OutputStream getFOS(String path ,int mbsize){
		try {
			OutputStream returnOOS = new BufferedOutputStream(new FileOutputStream(path), mbsize*mbSize);
			return returnOOS;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static OutputStream getFOS(File file ,int mbsize){
		try {
			OutputStream returnOOS = new BufferedOutputStream(new FileOutputStream(file), mbsize*mbSize);
			return returnOOS;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	public static InputStream getFIS(String path){
		return getFIS(path, 2);
	}
	public static InputStream getFIS(File file){
		return getFIS(file, 2);
	}
	public static InputStream getFIS(String path ,int mbsize ){
		try {
			InputStream returnOIS = new BufferedInputStream(new FileInputStream(path), mbsize*mbSize);
			return returnOIS;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static InputStream getFIS(File file,int mbsize ){
		try {
			InputStream returnOIS = new BufferedInputStream(new FileInputStream(file), mbsize*mbSize);
			return returnOIS;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	public static ObjectInputStream getFileOIS(String path ,int mbsize ){
		try {
			ObjectInputStream returnOIS = new ObjectInputStream(new BufferedInputStream(new FileInputStream(path), mbsize*mbSize));
			return returnOIS;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
}
