/**
 * 
 */
package org.dutir.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * @author yezheng
 *
 */
public class Filter {

	/**
	 * 
	 */
	public Filter() {
		// TODO Auto-generated constructor stub
	}
	
	public static void loadSet(Set<String> set, String filename) {

		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			String line = null;
			try {
				if (set == null)
					set = new HashSet<String>();
				if (!set.isEmpty())
					set.clear();
				while ((line = reader.readLine()) != null) {
					set.add(line.trim());
				}
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
