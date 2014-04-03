/*
 * LingPipe v. 3.8
 * Copyright (C) 2003-2009 Alias-i
 *
 * This program is licensed under the Alias-i Royalty Free License
 * Version 1 WITHOUT ANY WARRANTY, without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the Alias-i
 * Royalty Free License Version 1 for more details.
 *
 * You should have received a copy of the Alias-i Royalty Free License
 * Version 1 along with this program; if not, visit
 * http://alias-i.com/lingpipe/licenses/lingpipe-license-1.txt or contact
 * Alias-i, Inc. at 181 North 11th Street, Suite 401, Brooklyn, NY 11211,
 * +1 (718) 290-9170.
 */

package org.dutir.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Static utility methods for processing arrays.
 *
 * @author  Bob Carpenter
 * @version 4.0
 * @since   LingPipe1.0
 */
public class Arrays {

    /**
     * Forbid instance construction.
     */
    private Arrays() {
        /* no instances */
    }

    /**
     * Returns a copy of the specified array of integers of the
     * specified size.  As many of the original elements as will
     * fit in the new array are copied and the remaining elements
     * are set to zero.
     *
     * @param xs Original array.
     * @param newSize Length of returned array.
     * @return New array of specified length with as many elements
     * copied from the original array as will fit.
     */
    public static int[] reallocate(int[] xs, int newSize) {
        int[] ys = new int[newSize];
        for (int i = 0; i < xs.length && i < newSize; ++i)
            ys[i] = xs[i];
        return ys;
    }

    /**
     * Reallocates the specified integer array to be 50 percent
     * longer, with a minimum growth in length of one element.  All of
     * the elements of the specified array will be copied into the
     * resulting array and the remaining elements initialized to zero
     * (<code>0</code>).
     *
     * @param xs Array to reallocate.
     * @return Result of reallocation.
     */
    public static int[] reallocate(int[] xs) {
        int len = (xs.length * 3) / 2;
        return reallocate(xs,len == xs.length ? xs.length + 1 : len);
    }

    /**
     * Return the result of adding the specified character to the
     * specified sorted character array.  The original array will
     * be returned if the character is in the array, otherwise a
     * new array will be constructed and returned.
     *
     * <p><i>Warning:</i> No check is done that the incoming character
     * array is in order.
     *
     * @param c Character to add.
     * @param cs Array of characters in sorted order.
     * @return The result of adding the character to the array in the
     * correct order, returning a larger array if necessary.
     */
    public static char[] add(char c, char[] cs) {
        if (java.util.Arrays.binarySearch(cs,c) >= 0)
            return cs;
        char[] result = new char[cs.length+1];
        int i = 0;
        while (i < cs.length && c > cs[i]) {
            result[i] = cs[i];
            ++i;
        }
        result[i] = c;
        ++i;
        while (i < result.length) {
            result[i] = cs[i-1];
            ++i;
        }
        return result;
    }

    /**
     * Return a shallow copy of the specified array that
     * contains the same elements as the specified array.  If
     * the input is <code>null</code>, then <code>null</code>
     * is returned.
     *
     * @param cs Array to copy.
     * @return Shallow copy of array.
     */
    public static char[] copy(char[] cs) {
        if (cs == null) return null;
        char[] cs2 = new char[cs.length];
        for (int i = 0; i < cs.length; ++i)
            cs2[i] = cs[i];
        return cs2;
    }

    
    /**
     * Converts the specified character sequence to an array
     * of characters.
     *
     * @param cSeq Character sequence to convert.
     * @return Array of characters in sequence.
     */
    public static char[] toArray(CharSequence cSeq) {
        // return cSeq.toString().toCharArray();
        char[] cs = new char[cSeq.length()];
        for (int i = 0; i < cs.length; ++i)
            cs[i] = cSeq.charAt(i);
        return cs;
    }

    /**
     * Converts a string of comma-separated values into an array of
     * strings.  The returned array will be at least one element long,
     * with values being the maximal-length strings between commas.
     * Values may be of length zero.
     *
     * <p>Any comma (<code>,</code>), backslash (<code>\</code>) or
     * newline (<code>\n</code>) that appears in a value is escaped
     * with a backslash (<code>\</code>).  No other use of backslash
     * or comma in values is permitted.  An attempt to decode an
     * ill-formed input will throw an exception.
     *
     * <p>Some examples of strings and arrays they return, using
     * Java's string-literal escapes, are:
     *
     * <p>
     * <table cellpadding="3" border="1">
     * <tr><td><i>CSV</i></td>
     *     <td><i>String</i></td></tr>
     * <tr><td><code>""</code></td>
     *     <td><code>{ "" }</code></td></tr>
     * <tr><td><code>"a"</code></td>
     *     <td><code>{ "a" }</code></td></tr>
     * <tr><td><code>"a,b"</code></td>
     *     <td><code>{ "a", "b" }</code></td></tr>
     * <tr><td><code>"abc,def,g"</code></td>
     *     <td><code>{ "abc", "def", "g" }</code></td></tr>
     * <tr><td><code>","</code></td>
     *     <td><code>{ "", "" }</code></td></tr>
     * <tr><td><code>",,,b"</code></td>
     *     <td><code>{ "", "", "", "b" }</code></td></tr>
     * <tr><td><code>"a\,b"</code></td>
     *     <td><code>{ "a,b" }</code></td></tr>
     * <tr><td><code>"\\\\"</code></td>
     *     <td><code>{ "\\" }</code></td></tr>
     * <tr><td><code>"ab\\\\"</code></td>
     *     <td><code>{ "ab\\" }</code></td></tr>
     * <tr><td><code>"\\\n"</code></td>
     *     <td><code>{ "\n" }</code></td></tr>
     * <tr><td><code>"a\\,bc,d\\,e,,f"</code></td>
     *     <td><code>{ "a,bc", "d\", "e", "", "f" }</code></td></tr>
     * <tr><td><code>"ab\\"</code></td>
     *     <td><code>IllegalArgumentException</code></td></tr>
     * <tr><td><code>"a\\bc"</code></td>
     *     <td><code>IllegalArgumentException</code></td></tr>
     * <tr><td><code>"\"</code></td>
     *     <td><code>IllegalArgumentException</code></td></tr>
     * <tr><td><code>"\\\"</code></td>
     *     <td><code>IllegalArgumentException</code></td></tr>
     * </table>
     *
     * <p>Given the use of java escapes, <code>"ab\\"</code> is
     * the three character sequence a, b and backslash, with the
     * backslash escaped as <code>\\</code>.  Similarly, <code>"\\\n"</code>
     * is the two character string composed of a backslash followed
     * by a newline character.
     *
     * @param csvs Comma-separated values to separate.
     * @return Array of values.
     * @throws IllegalArgumentException if the input is not well formed.
     */
    public static String[] csvToArray(String csvs) {
        List<String> list = new ArrayList<String>();
        int pos = 0;
        StringBuilder sb = new StringBuilder();
        while (pos < csvs.length()) {
            char c = csvs.charAt(pos++);
            if (c == ',') {
                list.add(sb.toString());
                sb = new StringBuilder();
            } else if (c == BACKSLASH_CHAR) {
                if (pos >= csvs.length()) {
                    String msg = "Illegal end on backslash"
                        + " CSVs=|" + csvs + "|";
                    throw new IllegalArgumentException(msg);
                }
                char nextChar = csvs.charAt(pos++);
                if (csvEscape(c)) {
                    sb.append(nextChar);
                } else {
                    String msg = "Illegal escape following backslash."
                        + " Position=" + pos
                        + " CVSs=" + csvs;
                    throw new IllegalArgumentException(msg);
                }
            } else {
                sb.append(c);
            }
        }
        list.add(sb.toString());
        return Collections.toStringArray(list);
    }

    /**
     * Converts a comma-separated values string to a two-dimensional
     * array of strings.  Newlines (<code>\n<\code>) are used to
     * separate rows, and each row is in CSV notation, as defined in
     * {@link #csvToArray(String)} for more information on the row
     * encoding.
     *
     * <p>Here are some examples:
     *
     * <p>
     * <table cellpadding="3" border="1">
     * <tr><td><i>CSV</i></td>
     *     <td><i>Array</i></td></tr>
     * <tr><td><code>""</code></td>
     *     <td><code>{ { "" } }</code></td></tr>
     * <tr><td><code>"a"</code></td>
     *     <td><code>{ { "a" } }</code></td></tr>
     * <tr><td><code>"a,b"</code></td>
     *     <td><code>{ { "a", "b" } }</code></td></tr>
     * <tr><td><code>"\n"</code></td>
     *     <td><code>{ { "" }, { "" } }</code></td></tr>
     * <tr><td><code>"\n\n"</code></td>
     *     <td><code>{ { "" }, { "" }, { "" } }</code></td></tr>
     * <tr><td><code>"\\\n"</code></td>
     *     <td><code>{ { "\n" } }</code></td></tr>
     * </table>
     *
     * <p>Any illegal argument for {@link #csvToArray(String)} is also an
     * illegal argument for this method.
     *
     * @param csvs Input comma-separated values.
     * @return Array of arrays of strings derived from CSVs.
     * @throws IllegalArgumentException If the CVSs are not well formed.
     */
    public static String[][] csvToArray2D(String csvs) {
        List<String[]> result = new ArrayList<String[]>();
        int start = 0;
        for (int end = start; end < csvs.length(); ++end) {
            char c = csvs.charAt(end);
            if (c == '\n') {
                result.add(csvToArray(csvs.substring(start,end)));
                start = end+1;
            } else if (c == '\\') {
                if (++end >= csvs.length()) {
                    String msg = "Premature end on backslash."
                        + " csvs=" + csvs;
                    throw new IllegalArgumentException(msg);
                }
                char escapedC = csvs.charAt(end);
                if (!csvEscape(escapedC)) {
                    String msg = "Illegal escape."
                        + " position=" + (end-1)
                        + " char= " + escapedC;
                    throw new IllegalArgumentException(msg);
                }
            }

        }
        result.add(csvToArray(csvs.substring(start)));
        return result.<String[]>toArray(Strings.EMPTY_STRING_2D_ARRAY);
    }

    /**
     * Converts the two-dimensional array of strings to comma-separated
     * value notaiton.  See {@link #csvToArray2D(String)} for more
     * information on the encoding.  The input may be a ragged array.
     *
     * @param elts Array of arrays to encode.
     * @return Comma-separated values representation of the array.
     */
    public static String arrayToCSV(String[][] elts) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < elts.length; ++i) {
            if (i > 0) sb.append('\n');
            arrayToCSV(elts[i],sb);
        }
        return sb.toString();
    }

    /**
     * Converts an array of objects to comma-separated values
     * notation.  Each object will be converted to a string using its
     * <code>toString()</code> method.
     *
     * <p>See {@link #csvToArray(String)} for information on
     * the encoding and the reverse mapping.
     *
     * @param xs Array of strings to encode.
     * @return CSV-encoded array of strings.
     */
    public static String arrayToCSV(Object[] xs) {
        StringBuilder sb = new StringBuilder();
        arrayToCSV(xs,sb);
        return sb.toString();
    }

    static void arrayToCSV(Object[] xs, StringBuilder sb) {
        for (int i = 0; i < xs.length; ++i) {
            if (i > 0) sb.append(',');
            csvEncode(xs[i].toString(), sb);
        }
    }


    static final char BACKSLASH_CHAR = '\\';

    static void csvEncode(String in, StringBuilder sb) {
        for (int i = 0; i < in.length(); ++i) {
            char c = in.charAt(i);
            if (csvEscape(c))
                sb.append(BACKSLASH_CHAR);
            sb.append(c);
        }
    }

    static boolean csvEscape(char c) {
        return c == BACKSLASH_CHAR || c == ',' || c == '\n';
    }

    /**
     * Returns true if the specified object is an element of the
     * specified array.  Returns <code>false</code> if the specified
     * array is null.
     *
     * @param x Object to test for membership.
     * @param xs Array to test for object.
     * @return <code>true</code> if the specified object is an element
     * of the specified array.
     */
    public static boolean member(Object x, Object[] xs) {
        if (xs == null) return false;
        for (int i = xs.length; --i >= 0; ) {
            if (xs[i] == null) continue;
            if (xs[i].equals(x)) return true;
        }
        return false;
    }

    /**
     * Returns true if the specified character is a member of the
     * specified array.  Returns <code>false</code> if the specified
     * array is null.
     *
     * @param c Character to test for membership.
     * @param cs Array to test for character.
     * @return <code>true</code> if the specified character is an
     * element of the specified array.
     */
    public static boolean member(char c, char[] cs) {
        if (cs == null) return false;
        for (int i = 0; i < cs.length; ++i) {
            if (cs[i] == c) return true;
        }
        return false;
    }

    /**
     * Returns the concatenation of the string representations of the
     * specified objects separated by commas, with the whole
     * surrounded by square brackets and separated by a comma.
     *
     * @param xs Array of which to return a string representation.
     * @return String representation of the specified array.
     */
    public static String arrayToString(Object[] xs) {
        StringBuilder sb = new StringBuilder();
        arrayToStringBuilder(sb,xs);
        return sb.toString();
    }

    /**
     * Appends to the string buffer the concatenation of the string
     * representations of the specified objects separated by commas,
     * with the whole surrounded by square brackets and separated by a
     * comma.
     *
     * @param sb String buffer to which string representation is
     * appended.
     * @param xs Array of which to return a string representation.
     */
    public static void arrayToStringBuilder(StringBuilder sb, Object[] xs) {
        sb.append('[');
        for (int i = 0; i < xs.length; ++i) {
            if (i > 0) sb.append(',');
            sb.append(xs[i]);
        }
        sb.append(']');
    }

    /**
     * Returns the array of characters consisting of the members of
     * the first specified array followed by the specified character.
     *
     * @param cs Characters to start resulting array.
     * @param c Last character in resulting array.
     * @return Array of characters consisting of the characters in the
     * first array followed by the last character.
     * @throws NullPointerException If the array of characters is
     * null.
     */
    public static char[] concatenate(char[] cs, char c) {
        char[] result = new char[cs.length+1];
        for (int i = 0; i < cs.length; ++i)
            result[i] = cs[i];
        result[result.length-1] = c;
        return result;
    }



    /**
     * Returns a new array of strings containing the elements of the
     * first array of strings specified followed by the elements of
     * the second array of strings specified.
     *
     * @param xs First array of strings.
     * @param ys Second array of strings.
     * @return Concatenation of first array of strings followed by the
     * second array of strings.
     */
    public static String[] concatenate(String[] xs, String[] ys) {
        String[] result = new String[xs.length + ys.length];
        System.arraycopy(xs,0,result,0,xs.length);
        System.arraycopy(ys,0,result,xs.length,ys.length);
        return result;
    }


    /**
     * Returns the sum of the specified integer array.  Note that
     * there is no check for overflow.
     *
     * @param xs Array of integers to sum.
     * @return Sum of the array.
     */
    public static int sum(int[] xs) {
        int sum = 0;
        for (int i = 0; i < xs.length; ++i)
            sum += xs[i];
        return sum;
    }

    /**
     * Return <code>true</code> if the specified arrays are
     * the same length and contain the same elements.
     *
     * @param xs First array.
     * @param ys Second array.
     * @return <code>true</code> if the specified arrays are the same
     * length and contain the same elements.
     */
    public static boolean equals(Object[] xs, Object[] ys) {
        if (xs.length != ys.length) return false;
        for (int i = 0; i < xs.length; ++i)
            if (!xs[i].equals(ys[i])) return false;
        return true;
    }

    /**
     * Randomly permutes the elements of the specified array using
     * a freshly generated randomizer.  The
     * resulting array will have the same elements, but arranged into
     * a (possibly) different order.
     *
     * @param xs Array to permute.
     * @param <E> the type of objects in the array being permuted
     */
    public static <E> void permute(E[] xs) {
        permute(xs,new Random());
    }

    /**
     * Randomly permutes the elements of the specified array using the
     * specified randomizer.  The resulting array will have the same
     * elements, but arranged into a (possibly) different order.
     *
     * @param xs Array to permute.
     * @param random Randomizer to use for permuation.
     * @param <E> the type of objects in the array being permuted
     */
    public static <E> void permute(E[] xs, Random random) {
        for (int i = xs.length; --i > 0; ) {
            int pos = random.nextInt(i);
            E temp = xs[pos];
            xs[pos] = xs[i];
            xs[i] = temp;
        }
    }

    /**
     * Randomly permutes the elements of the specified integer array
     * using a newly created randomizer.  The resulting array will
     * have the same elements, but arranged into a (possibly)
     * different order.  The randomizer is created with a call to
     * the nullary constructor {@link java.util.Random#Random()}.
     *
     * @param xs Array to permute.
     */
    public static void permute(int[] xs) {
        permute(xs, new Random());
    }

    /**
     * Randomly permutes the elements of the specified integer
     * array using the specified randomizer.
     *
     * @param xs Array to permute.
     * @param random Randomizer to use for permutations.
     */
    public static void permute(int[] xs, Random random) {
        for (int i = xs.length; --i > 0; ) {
            int pos = random.nextInt(i);
            int temp = xs[pos];
            xs[pos] = xs[i];
            xs[i] = temp;
        }
    }


    /**
     * A length <code>0</code> array of integers.
     */
    public static final int[] EMPTY_INT_ARRAY = new int[] { };

    /**
     * A length <code>0</code> array of characters.
     *
     * @deprecated Use {@code Strings.EMPTY_CHAR_ARRAY} instead.
     */
    @Deprecated
    public static final char[] EMPTY_CHAR_ARRAY = Strings.EMPTY_CHAR_ARRAY;


    /**
     * A length <code>0</code> array of strings.
     *
     * @deprecated Use {@code Strings.EMPTY_STRING_ARRAY} instead.
     */
    @Deprecated
    public static final String[] EMPTY_STRING_ARRAY = Strings.EMPTY_STRING_ARRAY;

    //----------------
    //added methods
    public static int findMax(int arrays[]){
    	int max = Integer.MIN_VALUE;
    	int pos = 0;
		for(int i=0; i < arrays.length; i++){
			if(max < arrays[i]){
				max = arrays[i];
				pos = i;
			}
		}
    	return max;
    }
    
    public static int findMaxPos(int arrays[]){
    	int max = Integer.MIN_VALUE;
    	int pos = 0;
		for(int i=0; i < arrays.length; i++){
			if(max < arrays[i]){
				max = arrays[i];
				pos = i;
			}
		}
    	return pos;
    }
    
    public static double findMax(double[] arrays) {
    	double max = Double.NEGATIVE_INFINITY;
		int mid =0;
		for(int i=0; i < arrays.length; i++){
			if(max < arrays[i]){
				max = arrays[i];
				mid = i;
			}
		}
		return arrays[mid];
	}
    
    public static float findMax(float[] arrays) {
		float max = Float.NEGATIVE_INFINITY;
		int mid =0;
		for(int i=0; i < arrays.length; i++){
			if(max < arrays[i]){
				max = arrays[i];
				mid = i;
			}
		}
		return arrays[mid];
	}
    
    public static float findMin(float[] arrays) {
		float min = Float.MAX_VALUE;
		int mid =0;
		for(int i=0; i < arrays.length; i++){
			if(min > arrays[i]){
				min = arrays[i];
				mid = i;
			}
		}
		return arrays[mid];
	}
    
	public static int findMaxPos(float[] arrays) {
		float max = Float.NEGATIVE_INFINITY;
		int mid =0;
		for(int i=0; i < arrays.length; i++){
			if(max < arrays[i]){
				max = arrays[i];
				mid = i;
			}
		}
		return mid;
	}
	
	public static double sum(double[] arrays) {
		double sum = 0;
		for (int i = 0; i < arrays.length; i++) {
			sum += arrays[i];
		}
		return sum ;
	}
	
	public static float sum(float[] arrays) {

		float sum = 0;
		for (int i = 0; i < arrays.length; i++) {
			sum += arrays[i];
		}
		return sum ;
	}
	
	
	public static float aver(int[] pos) {

		float sum = 0;
		for (int i = 0; i < pos.length; i++) {
			sum += pos[i];
		}
		return sum / pos.length;
	}
	
	public static float aver(float[] pos) {
		
		float sum = 0;
		for (int i = 0; i < pos.length; i++) {
			sum += pos[i];
		}
		return sum / pos.length;
	}
	
	public static int[] seq(int start, int gap, int num){
		int retV[] = new int[num];
		retV[0] = start;
		for(int i=1; i < num; i++){
			retV[i] = retV[i-1] + gap;
		}
		return retV;
	}

	public static int[] indexSort(final float[] o){
		int index[] = seq(0, 1, o.length);
		sort1(o, index, 0, o.length);
				
		return index;
	}
	
    /**
     * Sorts the specified sub-array of integers into ascending order.
     */
    private static void sort1(float o[], int x[], int off, int len) {
	// Insertion sort on smallest arrays
	if (len < 7) {
	    for (int i=off; i<len+off; i++)
		for (int j=i; j>off && o[x[j-1]]>o[x[j]]; j--)
		    swap(x, j, j-1);
	    return;
	}

	// Choose a partition element, v
	int m = off + (len >> 1);       // Small arrays, middle element
	if (len > 7) {
	    int l = off;
	    int n = off + len - 1;
	    if (len > 40) {        // Big arrays, pseudomedian of 9
		int s = len/8;
		l = med3(o, x, l,     l+s, l+2*s);
		m = med3(o, x, m-s,   m,   m+s);
		n = med3(o, x, n-2*s, n-s, n);
	    }
	    m = med3(o, x, l, m, n); // Mid-size, med of 3
	}
	float v = o[x[m]];

	// Establish Invariant: v* (<v)* (>v)* v*
	int a = off, b = a, c = off + len - 1, d = c;
	while(true) {
	    while (b <= c && o[x[b]] <= v) {
		if (o[x[b]] == v)
		    swap(x, a++, b);
		b++;
	    }
	    while (c >= b && o[x[c]] >= v) {
		if (o[x[c]] == v)
		    swap(x, c, d--);
		c--;
	    }
	    if (b > c)
		break;
	    swap(x, b++, c--);
	}

	// Swap partition elements back to middle
	int s, n = off + len;
	s = java.lang.Math.min(a-off, b-a  );  vecswap(x, off, b-s, s);
	s = java.lang.Math.min(d-c,   n-d-1);  vecswap(x, b,   n-s, s);

	// Recursively sort non-partition-elements
	if ((s = b-a) > 1)
	    sort1(o, x, off, s);
	if ((s = d-c) > 1)
	    sort1(o, x, n-s, s);
    }

    /**
     * Swaps x[a .. (a+n-1)] with x[b .. (b+n-1)].
     */
    private static void vecswap(int x[], int a, int b, int n) {
	for (int i=0; i<n; i++, a++, b++)
	    swap(x, a, b);
    }
    
    private static int med3(float o[], int x[], int a, int b, int c) {
    	return (o[x[a]] < o[x[b]] ?
    		(o[x[b]] < o[x[c]] ? b : o[x[a]] < o[x[c]] ? c : a) :
    		(o[x[b]] > o[x[c]] ? b : o[x[a]] > o[x[c]] ? c : a));
        }
    /**
     * Swaps x[a] with x[b].
     */
    private static void swap(int x[], int a, int b) {
	int t = x[a];
	x[a] = x[b];
	x[b] = t;
    }
    
    /**
     * Return the string that is the reverse of the specified
     * character sequence.
     */
    public static float[] reverse(float cs[]) {
        float[] retValue = new float[cs.length];
        for (int i = cs.length; --i >= 0; )
            retValue[cs.length -i -1] = cs[i];
        return retValue;
    }
    
    public static void main(String args[]){
    	float a[] = {3,4,5,8,1, 3, 5, 18, 19, 13, 11};
    	int indexes[] = indexSort(a);
    	for(int i=0; i < a.length; i++){
    		System.out.println(indexes[i] + ": " + a[indexes[i]]);
    	}   	
    }
}
