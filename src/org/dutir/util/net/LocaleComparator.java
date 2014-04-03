/**
 * Created on 2006-9-10 ионГ05:07:23
 */
package org.dutir.util.net;

import java.util.Comparator;
import java.util.Locale;

/**
 * @author Shutra
 * 
 */
public class LocaleComparator implements Comparator<Locale> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Locale locale0, Locale locale1) {
		String s0 = locale0.toString();
		String s1 = locale1.toString();
		return s0.compareTo(s1);
	}

}
