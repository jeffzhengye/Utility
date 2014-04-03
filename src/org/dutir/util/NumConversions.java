package org.dutir.util;

//--------------------------------------------------------------------------------------------------------
//Conversions.java
//
//***  SUPER IMPORTANT CONCEPT  --  LEARN IT, LIVE IT, LOVE IT  ***
//
//The most expensive thing about conversions is allocating arrays.
//If you are optimizing for speed, do whatever it takes to avoid creating new arrays during conversion
//
//--------------------------------------------------------------------------------------------------------

import java.util.BitSet;

//--------------------------------------------------------------------------------------------------------
//Conversions
/**
* A set of static routines for converting bytes to other native types, such as ints, longs,
* doubles, chars, and strings
* 
* The most expensive thing about conversions is allocating arrays.
* If you are optimizing for speed, do whatever it takes to avoid creating new arrays during conversion
* 
*
* @author Russell F. Loane
* @version $Id: Conversions.java,v 1.3 2006/10/05 21:25:18 divita Exp $
*/
//--------------------------------------------------------------------------------------------------------
public class NumConversions {

//--------------------------------------------------------------------------------------------------------
//Conversions constants
//--------------------------------------------------------------------------------------------------------

static final String __ID = "$Id: Conversions.java,v 1.3 2006/10/05 21:25:18 divita Exp $";




//========================================================================================================
//
//Conversions from bytes at offset to native type
//
//Input params are:
//byte[] inBytes   source bytes
//int inByteDelta  offset into inBytes where conversion should start
//
//Outputs the native type converted to
//
//If inByteDelta is too big a RuntimeException will occur
//
//========================================================================================================


//--------------------------------------------------------------------------------------------------------
//bytesToInt
/**
* Converts the 4 bytes in inBytes starting at inByteDelta into an int
* 
* Throws an ArrayOutOfBounds runtime exception if any of the required 4 bytes fall outside
* the inBytes array.  Therefore, inByteDelta must be between 0 and inBytes.length-4
*
* @param   inBytes       source bytes
* @param   inByteDelta   offset into inBytes where conversion should start
*
* @return  the int value of the 4 bytes in inBytes starting at inByteDelta
*/
//--------------------------------------------------------------------------------------------------------
public static int bytesToInt(byte[] inBytes, int inByteDelta) {
 int n=inByteDelta;
 int theInt=((inBytes[n++]&0x000000ff)<<24)|
            ((inBytes[n++]&0x000000ff)<<16)|
            ((inBytes[n++]&0x000000ff)<<8)|
            ((inBytes[n++]&0x000000ff));
 return theInt;
}

//--------------------------------------------------------------------------------------------------------
//bytesToChar
/**
* Converts the 2 bytes in inBytes starting at inByteDelta into a char
* 
* Throws an ArrayOutOfBounds runtime exception if any of the required 2 bytes fall outside
* the inBytes array.  Therefore, inByteDelta must be between 0 and inBytes.length-2
*
* @param   inBytes       source bytes
* @param   inByteDelta   offset into inBytes where conversion should start
*
* @return  the char value of the 2 bytes in inBytes starting at inByteDelta
*/
//--------------------------------------------------------------------------------------------------------
public static char bytesToChar(byte[] inBytes, int inByteDelta) {
 int n=inByteDelta;
 char theChar=(char) (((inBytes[n++]&0x000000ff)<<8)|
                      ((inBytes[n++]&0x000000ff)));
 return theChar;
}

//--------------------------------------------------------------------------------------------------------
//bytesToLong
/**
* Converts the 8 bytes in inBytes starting at inByteDelta into a long
* 
* Throws an ArrayOutOfBounds runtime exception if any of the required 8 bytes fall outside
* the inBytes array.  Therefore, inByteDelta must be between 0 and inBytes.length-8
*
* @param   inBytes       source bytes
* @param   inByteDelta   offset into inBytes where conversion should start
*
* @return  the long value of the 8 bytes in inBytes starting at inByteDelta
*/
//--------------------------------------------------------------------------------------------------------
public static long bytesToLong(byte[] inBytes, int inByteDelta) {
 int n=inByteDelta;
 long theLong=((inBytes[n++]&0x00000000000000ffL)<<56)|
              ((inBytes[n++]&0x00000000000000ffL)<<48)|
              ((inBytes[n++]&0x00000000000000ffL)<<40)|
              ((inBytes[n++]&0x00000000000000ffL)<<32)|
              ((inBytes[n++]&0x00000000000000ffL)<<24)|
              ((inBytes[n++]&0x00000000000000ffL)<<16)|
              ((inBytes[n++]&0x00000000000000ffL)<<8)|
              ((inBytes[n++]&0x00000000000000ffL));
 return theLong;
}

//--------------------------------------------------------------------------------------------------------
//bytesToDouble
/**
* Converts the 8 bytes in inBytes starting at inByteDelta into a double
* 
* Throws an ArrayOutOfBounds runtime exception if any of the required 8 bytes fall outside
* the inBytes array.  Therefore, inByteDelta must be between 0 and inBytes.length-8
*
* @param   inBytes       source bytes
* @param   inByteDelta   offset into inBytes where conversion should start
*
* @return  the double value of the 8 bytes in inBytes starting at inByteDelta
*/
//--------------------------------------------------------------------------------------------------------
public static double bytesToDouble(byte[] inBytes, int inByteDelta) {
 int n=inByteDelta;
 long theLong=((inBytes[n++]&0x00000000000000ffL)<<56)|
              ((inBytes[n++]&0x00000000000000ffL)<<48)|
              ((inBytes[n++]&0x00000000000000ffL)<<40)|
              ((inBytes[n++]&0x00000000000000ffL)<<32)|
              ((inBytes[n++]&0x00000000000000ffL)<<24)|
              ((inBytes[n++]&0x00000000000000ffL)<<16)|
              ((inBytes[n++]&0x00000000000000ffL)<<8)|
              ((inBytes[n++]&0x00000000000000ffL));
 double theDouble=Double.longBitsToDouble(theLong);
 return theDouble;
}




//========================================================================================================
//
//Conversions from bytes with offset and length to array of native type at offset
//
//These routines are fast because no arrays are allocated
//
//Input params are:
//byte[] inBytes   source bytes
//int inByteDelta  offset into inBytes where conversion should start
//int inNBytes     number of bytes at inByteDelta to use in conversion
//XXX[] ioXXXs     destination array of native type, XXX
//int inXXXDelta   offset into ioXXXs where conversion should start
//
//No Output - part of ioXXXs is overwritten
//
//If inByteDelta is too big a RuntimeException will occur
//If inNBytes is too big a RuntimeException will occur
//If inNBytes is not an exact multiple of the size of the native type, the last few bytes
//will not be used
//If ioXXXs is too short a RuntimeException will occur
//If inXXXDelta is too big a RuntimeException will occur
//
//========================================================================================================

//--------------------------------------------------------------------------------------------------------
//bytesToInts
/**
* Converts from inNBytes bytes in inBytes starting at inByteDelta to inNBytes/4 ints in ioInts
* starting at inIntDelta.  Contents of ioInts are overwritten.
* 
* If inNBytes is not an exact multiple of 4, the last few bytes will not be used.
* 
* Throws an ArrayOutOfBounds runtime exception if ioInts too small.  Must have
* ioInts.length >= inNBytes/4
* 
* Throws an ArrayOutOfBounds runtime exception if any of the inNBytes source bytes fall outside
* the inBytes array.  Therefore, inByteDelta must be between 0 and inBytes.length-inNBytes
* 
* Throws an ArrayOutOfBounds runtime exception if any of the inNBytes/4 destination ints fall outside
* the ioInts array.  Therefore, inIntDelta must be between 0 and ioInts.length-inNBytes/4
*
* @param   inBytes       source bytes
* @param   inByteDelta   offset into inBytes where conversion should start
* @param   inNBytes      number of bytes in inBytes to use in conversion
* @param   ioInts        destination int array
* @param   inIntDelta    offset into ioInts where conversion should start
*/
//--------------------------------------------------------------------------------------------------------
public static void bytesToInts(byte[] inBytes, int inByteDelta, int inNBytes, int[] ioInts,
       int inIntDelta) {
 int n=inByteDelta;
 int theEndInt=inIntDelta+inNBytes/4;
 for (int i=inIntDelta; i<theEndInt; i++)
   ioInts[i]=((inBytes[n++]&0x000000ff)<<24)|
             ((inBytes[n++]&0x000000ff)<<16)|
             ((inBytes[n++]&0x000000ff)<<8)|
             ((inBytes[n++]&0x000000ff));
}

//--------------------------------------------------------------------------------------------------------
//bytesToChars
/**
* Converts from inNBytes bytes in inBytes starting at inByteDelta to inNBytes/2 chars in ioChars
* starting at inCharDelta.  Contents of ioChars are overwritten.
* 
* If inNBytes is not an exact multiple of 2, the last byte will not be used.
* 
* Throws an ArrayOutOfBounds runtime exception if ioChars too small.  Must have
* ioChars.length >= inNBytes/2
* 
* Throws an ArrayOutOfBounds runtime exception if any of the inNBytes source bytes fall outside
* the inBytes array.  Therefore, inByteDelta must be between 0 and inBytes.length-inNBytes
* 
* Throws an ArrayOutOfBounds runtime exception if any of the inNBytes/2 destination chars fall outside
* the ioChars array.  Therefore, inCharDelta must be between 0 and ioChars.length-inNBytes/2
*
* @param   inBytes       source bytes
* @param   inByteDelta   offset into inBytes where conversion should start
* @param   inNBytes      number of bytes in inBytes to use in conversion
* @param   ioChars       destination char array
* @param   inCharDelta   offset into ioChars where conversion should start
*/
//--------------------------------------------------------------------------------------------------------
public static void bytesToChars(byte[] inBytes, int inByteDelta, int inNBytes, char[] ioChars,
       int inCharDelta) {
 int n=inByteDelta;
 int theEndChar=inCharDelta+inNBytes/2;
 for (int i=inCharDelta; i<theEndChar; i++)
   ioChars[i]=(char) (((inBytes[n++]&0x000000ff)<<8)|
                      ((inBytes[n++]&0x000000ff)));
}

//--------------------------------------------------------------------------------------------------------
//bytesToLongs
/**
* Converts from inNBytes bytes in inBytes starting at inByteDelta to inNBytes/8 longs in ioLongs
* starting at inLongDelta.  Contents of ioLongs are overwritten.
* 
* If inNBytes is not an exact multiple of 8, the last few bytes will not be used.
* 
* Throws an ArrayOutOfBounds runtime exception if ioLongs too small.  Must have
* ioLongs.length >= inNBytes/8
* 
* Throws an ArrayOutOfBounds runtime exception if any of the inNBytes source bytes fall outside
* the inBytes array.  Therefore, inByteDelta must be between 0 and inBytes.length-inNBytes
* 
* Throws an ArrayOutOfBounds runtime exception if any of the inNBytes/8 destination longs fall outside
* the ioLongs array.  Therefore, inLongDelta must be between 0 and ioLongs.length-inNBytes/8
*
* @param   inBytes       source bytes
* @param   inByteDelta   offset into inBytes where conversion should start
* @param   inNBytes      number of bytes in inBytes to use in conversion
* @param   ioLongs       destination long array
* @param   inLongDelta   offset into ioLongs where conversion should start
*/
//--------------------------------------------------------------------------------------------------------
public static void bytesToLongs(byte[] inBytes, int inByteDelta, int inNBytes, long[] ioLongs,
       int inLongDelta) {
 int n=inByteDelta;
 int theEndLong=inLongDelta+inNBytes/8;
 for (int i=inLongDelta; i<theEndLong; i++)
   ioLongs[i]=((inBytes[n++]&0x00000000000000ffL)<<56)|
              ((inBytes[n++]&0x00000000000000ffL)<<48)|
              ((inBytes[n++]&0x00000000000000ffL)<<40)|
              ((inBytes[n++]&0x00000000000000ffL)<<32)|
              ((inBytes[n++]&0x00000000000000ffL)<<24)|
              ((inBytes[n++]&0x00000000000000ffL)<<16)|
              ((inBytes[n++]&0x00000000000000ffL)<<8)|
              ((inBytes[n++]&0x00000000000000ffL));
}

//--------------------------------------------------------------------------------------------------------
//bytesToDoubles
/**
* Converts from inNBytes bytes in inBytes starting at inByteDelta to inNBytes/8 doubles in ioDoubles
* starting at inDoubleDelta.  Contents of ioDoubles are overwritten.
* 
* If inNBytes is not an exact multiple of 8, the last few bytes will not be used.
* 
* Throws an ArrayOutOfBounds runtime exception if ioDoubles too small.  Must have
* ioDoubles.length >= inNBytes/8
* 
* Throws an ArrayOutOfBounds runtime exception if any of the inNBytes source bytes fall outside
* the inBytes array.  Therefore, inByteDelta must be between 0 and inBytes.length-inNBytes
* 
* Throws an ArrayOutOfBounds runtime exception if any of the inNBytes/8 destination doubles fall outside
* the ioDoubles array.  Therefore, inDoubleDelta must be between 0 and ioDoubles.length-inNBytes/8
*
* @param   inBytes         source bytes
* @param   inByteDelta     offset into inBytes where conversion should start
* @param   inNBytes        number of bytes in inBytes to use in conversion
* @param   ioDoubles       destination double array
* @param   inDoubleDelta   offset into ioDoubles where conversion should start
*/
//--------------------------------------------------------------------------------------------------------
public static void bytesToDoubles(byte[] inBytes, int inByteDelta, int inNBytes, double[] ioDoubles,
       int inDoubleDelta) {
 int n=inByteDelta;
 int theEndDouble=inDoubleDelta+inNBytes/8;
 for (int i=inDoubleDelta; i<theEndDouble; i++) {
   long theLong=((inBytes[n++]&0x00000000000000ffL)<<56)|
                ((inBytes[n++]&0x00000000000000ffL)<<48)|
                ((inBytes[n++]&0x00000000000000ffL)<<40)|
                ((inBytes[n++]&0x00000000000000ffL)<<32)|
                ((inBytes[n++]&0x00000000000000ffL)<<24)|
                ((inBytes[n++]&0x00000000000000ffL)<<16)|
                ((inBytes[n++]&0x00000000000000ffL)<<8)|
                ((inBytes[n++]&0x00000000000000ffL));
   ioDoubles[i]=Double.longBitsToDouble(theLong);
 }
}




//========================================================================================================
//
//Conversions from bytes to native type
//
//Input params are:
//byte[] inBytes   source bytes
//
//Outputs the native type converted to
//
//If inBytes is too short a RuntimeException will occur
//If inBytes is too long only the beginning of it will be used
//
//========================================================================================================


//--------------------------------------------------------------------------------------------------------
//bytesToInt
/**
* Converts the leading 4 bytes in inBytes into an int
* 
* Throws an ArrayOutOfBounds runtime exception if inBytes is less than 4 bytes long
*
* @param   inBytes       source bytes
*
* @return  the int value of the leading 4 bytes in inBytes
*/
//--------------------------------------------------------------------------------------------------------
public static int bytesToInt(byte[] inBytes) {
 return bytesToInt(inBytes,0);
}

//--------------------------------------------------------------------------------------------------------
//bytesToChar
/**
* Converts the leading 2 bytes in inBytes into a char
* 
* Throws an ArrayOutOfBounds runtime exception if inBytes is less than 2 bytes long
*
* @param   inBytes       source bytes
*
* @return  the char value of the leading 2 bytes in inBytes
*/
//--------------------------------------------------------------------------------------------------------
public static char bytesToChar(byte[] inBytes) {
 return bytesToChar(inBytes,0);
}

//--------------------------------------------------------------------------------------------------------
//bytesToLong
/**
* Converts the leading 8 bytes in inBytes into a long
* 
* Throws an ArrayOutOfBounds runtime exception if inBytes is less than 8 bytes long
*
* @param   inBytes       source bytes
*
* @return  the long value of the leading 8 bytes in inBytes
*/
//--------------------------------------------------------------------------------------------------------
public static long bytesToLong(byte[] inBytes) {
 return bytesToLong(inBytes,0);
}

//--------------------------------------------------------------------------------------------------------
//bytesToDouble
/**
* Converts the leading 8 bytes in inBytes into a double
* 
* Throws an ArrayOutOfBounds runtime exception if inBytes is less than 8 bytes long
*
* @param   inBytes       source bytes
*
* @return  the double value of the leading 8 bytes in inBytes
*/
//--------------------------------------------------------------------------------------------------------
public static double bytesToDouble(byte[] inBytes) {
 return bytesToDouble(inBytes,0);
}




//========================================================================================================
//
//Conversions from bytes to array of native type
//
//These routines are slower because arrays are allocated
//
//Input params are:
//byte[] inBytes   source bytes
//
//Outputs an array of the native type converted to
//
//If inBytes is not an exact multiple of the size of the native type, the last few bytes
//will not be used
//
//========================================================================================================


//--------------------------------------------------------------------------------------------------------
//bytesToInts
/**
* Converts inBytes into an int[] of length inNBytes.length/4
* 
* If inNBytes.length is not an exact multiple of 4, the last few bytes will not be used.
* 
*
* @param   inBytes       source bytes
*
* @return  an int[] of length inNBytes.length/4 with ints specified by bytes in inBytes
*/
//--------------------------------------------------------------------------------------------------------
public static int[] bytesToInts(byte[] inBytes) {
 int[] theInts=new int[inBytes.length/4];
 bytesToInts(inBytes,0,inBytes.length,theInts,0);
 return theInts;
}

//--------------------------------------------------------------------------------------------------------
//bytesToChars
/**
* Converts inBytes into a char[] of length inNBytes.length/2
* 
* If inNBytes.length is not an exact multiple of 2, the last byte will not be used.
* 
*
* @param   inBytes       source bytes
*
* @return  a char[] of length inNBytes.length/2 with chars specified by bytes in inBytes
*/
//--------------------------------------------------------------------------------------------------------
public static char[] bytesToChars(byte[] inBytes) {
 char[] theChars=new char[inBytes.length/2];
 bytesToChars(inBytes,0,inBytes.length,theChars,0);
 return theChars;
}

//--------------------------------------------------------------------------------------------------------
//bytesToLongs
/**
* Converts inBytes into a long[] of length inNBytes.length/8
* 
* If inNBytes.length is not an exact multiple of 8, the last few bytes will not be used.
* 
*
* @param   inBytes       source bytes
*
* @return  a long[] of length inNBytes.length/8 with longs specified by bytes in inBytes
*/
//--------------------------------------------------------------------------------------------------------
public static long[] bytesToLongs(byte[] inBytes) {
 long[] theLongs=new long[inBytes.length/8];
 bytesToLongs(inBytes,0,inBytes.length,theLongs,0);
 return theLongs;
}

//--------------------------------------------------------------------------------------------------------
//bytesToDoubles
/**
* Converts inBytes into a double[] of length inNBytes.length/8
* 
* If inNBytes.length is not an exact multiple of 8, the last few bytes will not be used.
* 
*
* @param   inBytes       source bytes
*
* @return  a double[] of length inNBytes.length/8 with doubles specified by bytes in inBytes
*/
//--------------------------------------------------------------------------------------------------------
public static double[] bytesToDoubles(byte[] inBytes) {
 double[] theDoubles=new double[inBytes.length/8];
 bytesToDoubles(inBytes,0,inBytes.length,theDoubles,0);
 return theDoubles;
}

//--------------------------------------------------------------------------------------------------------
//bytesToString
/**
* Converts inBytes into a String of length inNBytes.length/2
* 
* If inNBytes.length is not an exact multiple of 2, the last byte will not be used.
* 
*
* @param   inBytes       source bytes
*
* @return  a String of length inNBytes.length/2 with chars specified by bytes in inBytes
*/
//--------------------------------------------------------------------------------------------------------
public static String bytesToString(byte[] inBytes) {
 return new String(bytesToChars(inBytes));
}




//========================================================================================================
//
//Conversions from native type to bytes at offset
//
//These routines are fast because no arrays are allocated
//
//Input params are:
//XXX inXXX        source native type converted from
//byte[] ioBytes   destination bytes
//int inByteDelta  offset into ioBytes where conversion should start
//
//No Output - part of ioBytes is overwritten
//
//If ioBytes is too short a RuntimeException will occur
//If inByteDelta is too big a RuntimeException will occur
//
//========================================================================================================


//--------------------------------------------------------------------------------------------------------
//intToBytes
/**
* Converts the inInt to 4 bytes in ioBytes starting at inByteDelta.  Contents of ioBytes
* are overwritten.
* 
* Throws an ArrayOutOfBounds runtime exception if any of the 4 destination bytes fall outside
* the ioBytes array.  Therefore, inByteDelta must be between 0 and ioBytes.length-4
*
* @param   inInt         source int
* @param   ioBytes       destination byte array
* @param   inByteDelta   offset into ioBytes where conversion should start
*/
//--------------------------------------------------------------------------------------------------------
public static void intToBytes(int inInt, byte[] ioBytes, int inByteDelta) {
 int n=inByteDelta;
 ioBytes[n++]=((byte) (inInt>>>24));
 ioBytes[n++]=((byte) (inInt>>>16));
 ioBytes[n++]=((byte) (inInt>>>8));
 ioBytes[n++]=((byte) (inInt));
}

//--------------------------------------------------------------------------------------------------------
//charToBytes
/**
* Converts the inChar to 2 bytes in ioBytes starting at inByteDelta.  Contents of ioBytes
* are overwritten.
* 
* Throws an ArrayOutOfBounds runtime exception if any of the 2 destination bytes fall outside
* the ioBytes array.  Therefore, inByteDelta must be between 0 and ioBytes.length-2
*
* @param   inChar        source char
* @param   ioBytes       destination byte array
* @param   inByteDelta   offset into ioBytes where conversion should start
*/
//--------------------------------------------------------------------------------------------------------
public static void charToBytes(char inChar, byte[] ioBytes, int inByteDelta) {
 int n=inByteDelta;
 ioBytes[n++]=((byte) (inChar>>>8));
 ioBytes[n++]=((byte) (inChar));
}

//--------------------------------------------------------------------------------------------------------
//longToBytes
/**
* Converts the inLong to 8 bytes in ioBytes starting at inByteDelta.  Contents of ioBytes
* are overwritten.
* 
* Throws an ArrayOutOfBounds runtime exception if any of the 8 destination bytes fall outside
* the ioBytes array.  Therefore, inByteDelta must be between 0 and ioBytes.length-8
*
* @param   inLong        source long
* @param   ioBytes       destination byte array
* @param   inByteDelta   offset into ioBytes where conversion should start
*/
//--------------------------------------------------------------------------------------------------------
public static void longToBytes(long inLong, byte[] ioBytes, int inByteDelta) {
 int n=inByteDelta;
 ioBytes[n++]=((byte) (inLong>>>56));
 ioBytes[n++]=((byte) (inLong>>>48));
 ioBytes[n++]=((byte) (inLong>>>40));
 ioBytes[n++]=((byte) (inLong>>>32));
 ioBytes[n++]=((byte) (inLong>>>24));
 ioBytes[n++]=((byte) (inLong>>>16));
 ioBytes[n++]=((byte) (inLong>>>8));
 ioBytes[n++]=((byte) (inLong));
}

//--------------------------------------------------------------------------------------------------------
//doubleToBytes
/**
* Converts the inDouble to 8 bytes in ioBytes starting at inByteDelta.  Contents of ioBytes
* are overwritten.
* 
* Throws an ArrayOutOfBounds runtime exception if any of the 8 destination bytes fall outside
* the ioBytes array.  Therefore, inByteDelta must be between 0 and ioBytes.length-8
*
* @param   inDouble      source double
* @param   ioBytes       destination byte array
* @param   inByteDelta   offset into ioBytes where conversion should start
*/
//Starts at inByteDelta.  Sets next 8 bytes of ioBytes
//--------------------------------------------------------------------------------------------------------
public static void doubleToBytes(double inDouble, byte[] ioBytes, int inByteDelta) {
 int n=inByteDelta;
 long theLong=Double.doubleToLongBits(inDouble);
 ioBytes[n++]=((byte) (theLong>>>56));
 ioBytes[n++]=((byte) (theLong>>>48));
 ioBytes[n++]=((byte) (theLong>>>40));
 ioBytes[n++]=((byte) (theLong>>>32));
 ioBytes[n++]=((byte) (theLong>>>24));
 ioBytes[n++]=((byte) (theLong>>>16));
 ioBytes[n++]=((byte) (theLong>>>8));
 ioBytes[n++]=((byte) (theLong));
}




//========================================================================================================
//
//Conversions from array of native type with offset and length to bytes at offset
//
//These routines are fast because no arrays are allocated
//
//Input params are:
//XXX[] inXXXs     source array of native type converted from
//int inXXXDelta   offset into inXXXs where conversion should start
//int inNXXXs      number of native types at inXXXDelta to use in conversion
//byte[] ioBytes   destination bytes
//int inByteDelta  offset into ioBytes where conversion should start
//
//No Output - part of ioBytes is overwritten
//
//If inXXXDelta is too big a RuntimeException will occur
//If inNXXXs is too big a RuntimeException will occur
//If ioBytes is too short a RuntimeException will occur
//If inByteDelta is too big a RuntimeException will occur
//
//========================================================================================================

//--------------------------------------------------------------------------------------------------------
//intsToBytes
/**
* Converts from inNInts ints in inInts starting at inIntDelta to inNInts*4 bytes in
* ioBytes starting at inByteDelta.  Contents of ioBytes are overwritten.
* 
* Throws an ArrayOutOfBounds runtime exception if ioBytes too small.  Must have
* ioBytes.length >= inNInts*4
* 
* Throws an ArrayOutOfBounds runtime exception if any of the inNInts source ints fall outside
* the inInts array.  Therefore, inIntDelta must be between 0 and inInts.length-inNInts
* 
* Throws an ArrayOutOfBounds runtime exception if any of the inNInts*4 destination bytes fall outside
* the ioBytes array.  Therefore, inByteDelta must be between 0 and ioBytes.length-inNInts*4
*
* @param   inInts        source ints
* @param   inIntDelta    offset into inInts where conversion should start
* @param   inNInts       number of ints in inInts to use in conversion
* @param   ioBytes       destination byte array
* @param   inByteDelta   offset into ioBytes where conversion should start
*/
//--------------------------------------------------------------------------------------------------------
public static void intsToBytes(int[] inInts, int inIntDelta, int inNInts, byte[] ioBytes,
       int inByteDelta) {
 int n=inByteDelta;
 for (int i=0; i<inNInts; i++) {
   int theInt=inInts[inIntDelta+i];
   ioBytes[n++]=((byte) (theInt>>>24));
   ioBytes[n++]=((byte) (theInt>>>16));
   ioBytes[n++]=((byte) (theInt>>>8));
   ioBytes[n++]=((byte) (theInt));
 }
}

//--------------------------------------------------------------------------------------------------------
//charsToBytes
/**
* Converts from inNChars chars in inChars starting at inCharDelta to inNChars*2 bytes in
* ioBytes starting at inByteDelta.  Contents of ioBytes are overwritten.
* 
* Throws an ArrayOutOfBounds runtime exception if ioBytes too small.  Must have
* ioBytes.length >= inNChars*2
* 
* Throws an ArrayOutOfBounds runtime exception if any of the inNChars source chars fall outside
* the inChars array.  Therefore, inCharDelta must be between 0 and inChars.length-inNChars
* 
* Throws an ArrayOutOfBounds runtime exception if any of the inNChars*2 destination bytes fall outside
* the ioBytes array.  Therefore, inByteDelta must be between 0 and ioBytes.length-inNChars*2
*
* @param   inChars       source chars
* @param   inCharDelta   offset into inChars where conversion should start
* @param   inNChars      number of chars in inChars to use in conversion
* @param   ioBytes       destination byte array
* @param   inByteDelta   offset into ioBytes where conversion should start
*/
//--------------------------------------------------------------------------------------------------------
public static void charsToBytes(char[] inChars, int inCharDelta, int inNChars, byte[] ioBytes,
       int inByteDelta) {
 int n=inByteDelta;
 for (int i=0; i<inNChars; i++) {
   int theChar=inChars[inCharDelta+i];
   ioBytes[n++]=((byte) (theChar>>>8));
   ioBytes[n++]=((byte) (theChar));
 }
}

//--------------------------------------------------------------------------------------------------------
//longsToBytes
/**
* Converts from inNLongs longs in inLongs starting at inLongDelta to inNLongs*2 bytes in
* ioBytes starting at inByteDelta.  Contents of ioBytes are overwritten.
* 
* Throws an ArrayOutOfBounds runtime exception if ioBytes too small.  Must have
* ioBytes.length >= inNLongs*2
* 
* Throws an ArrayOutOfBounds runtime exception if any of the inNLongs source longs fall outside
* the inLongs array.  Therefore, inLongDelta must be between 0 and inLongs.length-inNLongs
* 
* Throws an ArrayOutOfBounds runtime exception if any of the inNLongs*2 destination bytes fall outside
* the ioBytes array.  Therefore, inByteDelta must be between 0 and ioBytes.length-inNLongs*2
*
* @param   inLongs       source longs
* @param   inLongDelta   offset into inLongs where conversion should start
* @param   inNLongs      number of longs in inLongs to use in conversion
* @param   ioBytes       destination byte array
* @param   inByteDelta   offset into ioBytes where conversion should start
*/
//--------------------------------------------------------------------------------------------------------
public static void longsToBytes(long[] inLongs, int inLongDelta, int inNLongs, byte[] ioBytes,
       int inByteDelta) {
 int n=inByteDelta;
 for (int i=0; i<inNLongs; i++) {
   long theLong=inLongs[inLongDelta+i];
   ioBytes[n++]=((byte) (theLong>>>56));
   ioBytes[n++]=((byte) (theLong>>>48));
   ioBytes[n++]=((byte) (theLong>>>40));
   ioBytes[n++]=((byte) (theLong>>>32));
   ioBytes[n++]=((byte) (theLong>>>24));
   ioBytes[n++]=((byte) (theLong>>>16));
   ioBytes[n++]=((byte) (theLong>>>8));
   ioBytes[n++]=((byte) (theLong));
 }
}

//--------------------------------------------------------------------------------------------------------
//doublesToBytes
/**
* Converts from inNDoubles doubles in inDoubles starting at inDoubleDelta to inNDoubles*2 bytes in
* ioBytes starting at inByteDelta.  Contents of ioBytes are overwritten.
* 
* Throws an ArrayOutOfBounds runtime exception if ioBytes too small.  Must have
* ioBytes.length >= inNDoubles*2
* 
* Throws an ArrayOutOfBounds runtime exception if any of the inNDoubles source doubles fall outside
* the inDoubles array.  Therefore, inDoubleDelta must be between 0 and inDoubles.length-inNDoubles
* 
* Throws an ArrayOutOfBounds runtime exception if any of the inNDoubles*2 destination bytes fall outside
* the ioBytes array.  Therefore, inByteDelta must be between 0 and ioBytes.length-inNDoubles*2
*
* @param   inDoubles       source doubles
* @param   inDoubleDelta   offset into inDoubles where conversion should start
* @param   inNDoubles      number of doubles in inDoubles to use in conversion
* @param   ioBytes         destination byte array
* @param   inByteDelta     offset into ioBytes where conversion should start
*/
//--------------------------------------------------------------------------------------------------------
public static void doublesToBytes(double[] inDoubles, int inDoubleDelta, int inNDoubles, byte[] ioBytes,
       int inByteDelta) {
 int n=inByteDelta;
 for (int i=0; i<inNDoubles; i++) {
   double theDouble=inDoubles[inDoubleDelta+i];
   long theLong=Double.doubleToLongBits(theDouble);
   ioBytes[n++]=((byte) (theLong>>>56));
   ioBytes[n++]=((byte) (theLong>>>48));
   ioBytes[n++]=((byte) (theLong>>>40));
   ioBytes[n++]=((byte) (theLong>>>32));
   ioBytes[n++]=((byte) (theLong>>>24));
   ioBytes[n++]=((byte) (theLong>>>16));
   ioBytes[n++]=((byte) (theLong>>>8));
   ioBytes[n++]=((byte) (theLong));
 }
}




//========================================================================================================
//
//Conversions from native type to bytes
//
//These routines are slower because arrays are allocated
//
//Input params are:
//XXX inXXX        source native type converted from
//
//Outputs array of bytes containing conversion
//
//========================================================================================================


//--------------------------------------------------------------------------------------------------------
//intToBytes
/**
* Converts the inInt into an array of 4 bytes
*
* @param   inInt  source int to be converted
*
* @return  array of 4 bytes containing the int value
*/
//--------------------------------------------------------------------------------------------------------
public static byte[] intToBytes(int inInt) {
 byte[] theBytes=new byte[4];
 intToBytes(inInt,theBytes,0);
 return theBytes;
}

//--------------------------------------------------------------------------------------------------------
//charToBytes
/**
* Converts the inChar into an array of 2 bytes
*
* @param   inChar  source char to be converted
*
* @return  array of 2 bytes containing the char value
*/
//--------------------------------------------------------------------------------------------------------
public static byte[] charToBytes(char inChar) {
 byte[] theBytes=new byte[2];
 charToBytes(inChar,theBytes,0);
 return theBytes;
}

//--------------------------------------------------------------------------------------------------------
//booleanToBytes
/**
* Converts the inBoolean into 1 byte
*
* @param inVal source boolean to be converted
*
* @return byte[]  containing the boolean value
*/
//--------------------------------------------------------------------------------------------------------
public static byte[] booleanToBytes(boolean inVal) {
 byte[] theBytes=new byte[1];
 if ( inVal )
   theBytes[0] = 1;
 else 
   theBytes[0] = 0;
 return theBytes;
}

//--------------------------------------------------------------------------------------------------------
//longToBytes
/**
* Converts the inLong into an array of 8 bytes
*
* @param   inLong  source long to be converted
*
* @return  array of 8 bytes containing the long value
*/
//--------------------------------------------------------------------------------------------------------
public static byte[] longToBytes(long inLong) {
 byte[] theBytes=new byte[8];
 longToBytes(inLong,theBytes,0);
 return theBytes;
}

//--------------------------------------------------------------------------------------------------------
//doubleToBytes
/**
* Converts the inDouble into an array of 8 bytes
*
* @param   inDouble  source double to be converted
*
* @return  array of 8 bytes containing the double value
*/
//--------------------------------------------------------------------------------------------------------
public static byte[] doubleToBytes(double inDouble) {
 byte[] theBytes=new byte[8];
 doubleToBytes(inDouble,theBytes,0);
 return theBytes;
}




//========================================================================================================
//
//Conversions from array of native type to bytes
//
//These routines are slower because arrays are allocated
//
//Input params are:
//XXX[] inXXXs     source array of native type converted from
//
//Outputs array of bytes containing conversion
//
//========================================================================================================


//--------------------------------------------------------------------------------------------------------
//intsToBytes
/**
* Converts the inInts array into an array of 4*inInts.length bytes
*
* @param   inInts  source ints to be converted
*
* @return  array of 4*inInts.length bytes containing the int values
*/
//--------------------------------------------------------------------------------------------------------
public static byte[] intsToBytes(int[] inInts) {
 byte[] theBytes=new byte[4*inInts.length];
 intsToBytes(inInts,0,inInts.length,theBytes,0);
 return theBytes;
}

//--------------------------------------------------------------------------------------------------------
//charsToBytes
/**
* Converts the inChars array into an array of 2*inChars.length bytes
*
* @param   inChars  source chars to be converted
*
* @return  array of 2*inChars.length bytes containing the char values
*/
//--------------------------------------------------------------------------------------------------------
public static byte[] charsToBytes(char[] inChars) {
 byte[] theBytes=new byte[2*inChars.length];
 charsToBytes(inChars,0,inChars.length,theBytes,0);
 return theBytes;
}

//--------------------------------------------------------------------------------------------------------
//longsToBytes
/**
* Converts the inLongs array into an array of 8*inLongs.length bytes
*
* @param   inLongs  source longs to be converted
*
* @return  array of 8*inLongs.length bytes containing the long values
*/
//--------------------------------------------------------------------------------------------------------
public static byte[] longsToBytes(long[] inLongs) {
 byte[] theBytes=new byte[8*inLongs.length];
 longsToBytes(inLongs,0,inLongs.length,theBytes,0);
 return theBytes;
}

//--------------------------------------------------------------------------------------------------------
//doublesToBytes
/**
* Converts the inDoubles array into an array of 8*inDoubles.length bytes
*
* @param   inDoubles  source doubles to be converted
*
* @return  array of 8*inDoubles.length bytes containing the double values
*/
//--------------------------------------------------------------------------------------------------------
public static byte[] doublesToBytes(double[] inDoubles) {
 byte[] theBytes=new byte[8*inDoubles.length];
 doublesToBytes(inDoubles,0,inDoubles.length,theBytes,0);
 return theBytes;
}

//--------------------------------------------------------------------------------------------------------
//stringToBytes
/**
* Converts inString into an array of 2*inString.length() bytes
*
* @param   inString  source String to be converted
*
* @return  array of 2*inString.length() bytes containing the String value
*/
//--------------------------------------------------------------------------------------------------------
public static byte[] stringToBytes(String inString) {
 return charsToBytes(inString.toCharArray());
}




//========================================================================================================
//
//Conversions of ints to/from longs
//
//========================================================================================================

//--------------------------------------------------------------------------------------------------------
//intHalvesToLong
/**
* Converts two source ints into a long
*
* @see Conversions#firstLongHalf
* @see Conversions#secondLongHalf
*
* @param   inFirstLongHalf    first source int
* @param   inSecondLongHalf   second source int
*
* @return  long made up of the two source ints
*/
//--------------------------------------------------------------------------------------------------------
public static long intHalvesToLong(int inFirstLongHalf, int inSecondLongHalf) {
 return ((0x00000000ffffffffL&inFirstLongHalf)<<32)|(0x00000000ffffffffL&inSecondLongHalf);
}

//--------------------------------------------------------------------------------------------------------
//firstLongHalf
/**
* Extracts the first of two ints that make up inLong
*
* @see Conversions#intHalvesToLong
*
* @param   inLong    the source long
*
* @return  the first of two ints that make up inLong
*/
//--------------------------------------------------------------------------------------------------------
public static int firstLongHalf(long inLong) {
 return (int) (0x00000000ffffffffL&(inLong>>>32));
}

//--------------------------------------------------------------------------------------------------------
//secondLongHalf
/**
* Extracts the second of two ints that make up inLong
*
* @see Conversions#intHalvesToLong
*
* @param   inLong    the source long
*
* @return  the second of two ints that make up inLong
*/
//--------------------------------------------------------------------------------------------------------
public static int secondLongHalf(long inLong) {
 return (int) (0x00000000ffffffffL&inLong);
}

//========================================================================================================
//
//Conversions of BitSet to/from bytes 
//
//========================================================================================================

//================================================|Public Method Header|====
/**
* Method bitSetToBytes returns a byte[] of the BitSet 
* 
* @return byte[] 
* 
*/
//================================================|Public Method Header|====
public static byte[] bitSetToBytes ( BitSet bits )
{ 
 byte bytes[] = null; 
 if (( bits != null ) && ( bits.length() > 0 )) {
   bytes = new byte[bits.length()/8+1];
   for (int i=0; i<bits.length(); i++) {
	if (bits.get(i)) {
	  bytes[bytes.length-i/8-1] |= 1<<(i%8);
	}
   }
 }
 return bytes;

} // *** End public static byte bitSetToBytes()

//================================================|Public Method Header|====
/**
* Method bitSetToBytes returns a byte[] of the BitSet 
* 
* @return byte[] 
* 
*/
//================================================|Public Method Header|====
public static byte[] bitSetToBytes ( BitSet bits, int fixedNumBits)
{ 
 byte bytes[] = null; 
 if (( bits != null ) && ( bits.length() > 0 )) {
   bytes = new byte[fixedNumBits/8+1];
   for (int i=0; i<bits.length(); i++) {
	if (bits.get(i)) {
	  bytes[bytes.length-i/8-1] |= 1<<(i%8);
	}
   }
 }
 return bytes;

} // *** End public static byte bitSetToBytes()


//================================================|Public Method Header|====
/**
* Method bytesToBitSet returns a BitSet from a byte[] 
* 
* @return BitSet 
* 
*/
//================================================|Public Method Header|====
public static BitSet bytesToBitSet( byte[] bytes)
{ 
 BitSet bits = new BitSet();
 for (int i=0; i<bytes.length*8; i++) {
   if ((bytes[bytes.length-i/8-1]&(1<<(i%8))) > 0) {
	bits.set(i);
   }
 }
 return bits;
 
} // *** End public static byte bytesToBitSet()

//================================================|Public Method Header|====
/**
* Method byteToBooleanSet returns a boolean from a byte
* @param pByte 
* @return boolean 
* 
*/
//================================================|Public Method Header|====
public static boolean byteToBoolean( byte pByte )
{ 
 
 boolean returnVal = false;

 if ( pByte == 1 )
   returnVal = true;

 return(returnVal); 
 
} // *** End public static byte bytesToBitSet()


/** Converts a 32 bit float to an 8 bit float.
 * <br>Values less than zero are all mapped to zero.
 * <br>Values are truncated (rounded down) to the nearest 8 bit value.
 * <br>Values between zero and the smallest representable value
 *  are rounded up.
 *
 * @param f the 32 bit float to be converted to an 8 bit float (byte)
 * @param numMantissaBits the number of mantissa bits to use in the byte, with the remainder to be used in the exponent
 * @param zeroExp the zero-point in the range of exponent values
 * @return the 8 bit float representation
 */
public static byte floatToByte(float f, int numMantissaBits, int zeroExp) {
  // Adjustment from a float zero exponent to our zero exponent,
  // shifted over to our exponent position.
  int fzero = (63-zeroExp)<<numMantissaBits;
  int bits = Float.floatToRawIntBits(f);
  int smallfloat = bits >> (24-numMantissaBits);
  if (smallfloat < fzero) {
    return (bits<=0) ?
      (byte)0   // negative numbers and zero both map to 0 byte
     :(byte)1;  // underflow is mapped to smallest non-zero number.
  } else if (smallfloat >= fzero + 0x100) {
    return -1;  // overflow maps to largest number
  } else {
    return (byte)(smallfloat - fzero);
  }
}

/** Converts an 8 bit float to a 32 bit float. */
public static float byteToFloat(byte b, int numMantissaBits, int zeroExp) {
  // on Java1.5 & 1.6 JVMs, prebuilding a decoding array and doing a lookup
  // is only a little bit faster (anywhere from 0% to 7%)
  if (b == 0) return 0.0f;
  int bits = (b&0xff) << (24-numMantissaBits);
  bits += (63-zeroExp) << 24;
  return Float.intBitsToFloat(bits);
}


//
// Some specializations of the generic functions follow.
// The generic functions are just as fast with current (1.5)
// -server JVMs, but still slower with client JVMs.
//

/** floatToByte(b, mantissaBits=3, zeroExponent=15)
 * <br>smallest non-zero value = 5.820766E-10
 * <br>largest value = 7.5161928E9
 * <br>epsilon = 0.125
 */
public static byte floatToByte315(float f) {
  int bits = Float.floatToRawIntBits(f);
  int smallfloat = bits >> (24-3);
  if (smallfloat < (63-15)<<3) {
    return (bits<=0) ? (byte)0 : (byte)1;
  }
  if (smallfloat >= ((63-15)<<3) + 0x100) {
    return -1;
  }
  return (byte)(smallfloat - ((63-15)<<3));
}

/** byteToFloat(b, mantissaBits=3, zeroExponent=15) */
public static float byte315ToFloat(byte b) {
  // on Java1.5 & 1.6 JVMs, prebuilding a decoding array and doing a lookup
  // is only a little bit faster (anywhere from 0% to 7%)
  if (b == 0) return 0.0f;
  int bits = (b&0xff) << (24-3);
  bits += (63-15) << 24;
  return Float.intBitsToFloat(bits);
}


/** floatToByte(b, mantissaBits=5, zeroExponent=2)
 * <br>smallest nonzero value = 0.033203125
 * <br>largest value = 1984.0
 * <br>epsilon = 0.03125
 */
public static byte floatToByte52(float f) {
  int bits = Float.floatToRawIntBits(f);
  int smallfloat = bits >> (24-5);
  if (smallfloat < (63-2)<<5) {
    return (bits<=0) ? (byte)0 : (byte)1;
  }
  if (smallfloat >= ((63-2)<<5) + 0x100) {
    return -1;
  }
  return (byte)(smallfloat - ((63-2)<<5));
}

/** byteToFloat(b, mantissaBits=5, zeroExponent=2) */
public static float byte52ToFloat(byte b) {
  // on Java1.5 & 1.6 JVMs, prebuilding a decoding array and doing a lookup
  // is only a little bit faster (anywhere from 0% to 7%)
  if (b == 0) return 0.0f;
  int bits = (b&0xff) << (24-5);
  bits += (63-2) << 24;
  return Float.intBitsToFloat(bits);
}

public static void main(String args[]){
	  float a = 3.12f;
	  byte b = floatToByte315(a);
	  float ac = byte315ToFloat(b);
	  System.out.println(b);
	  System.out.println(ac);
}


}
