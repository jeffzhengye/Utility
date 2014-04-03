/**
 * 
 */
package org.dutir.util;

import java.io.*;

/**
 * @author yezheng
 *
 */
public class CharOper {
	static PrintStream out = System.out;
	
	public static final char[] Sign_Connector = new char[]{'-','_','.','@','&'};
	
	public static boolean isChinese(char c)
	{
		
		if( c >= '\u4e00' && c <= '\u9fa5'){
			return true;
		}
		return false;
	}
	
	public static boolean isAllChinese(String chars)
	{
		for(int i=0; i < chars.length(); i++){
			if(!isChinese(chars.charAt(i))){
				return false;
			}
		}

		return true;
	}
	
	public static boolean isAllDigit(String filteStr){
		boolean retValue = true;
		for(int i=0; i < filteStr.length(); i++){
			if(!Character.isDigit(filteStr.charAt(i))){
				retValue =false;
				break;
			}
		}
		return retValue;
	}
	
	public static boolean isDigit(char c){
		return Character.isDigit(c);
	}
	
	public static boolean isLetter(char c){
		return Character.isLetter(c);
	}
	
	public static boolean isEnglishLetter(char input){
		return (input >= 'a' && input <= 'z') 
				|| (input >= 'A' && input <= 'Z');
	}
	public static boolean isAllEnglishLetter(String chars){
		for(int i=0; i < chars.length(); i++){
			if(!isEnglishLetter(chars.charAt(i))){
				return false;
			}
		}

		return true;
	}
	
	public static boolean isArabicNumber(char input){
		return input >= '0' && input <= '9';
	}
	
	
	public static boolean isPunctuation(char c){
		return false;
	}
	
	public static boolean isBlank(char c)
	{
		return c == ' ';
	}
	
	public static boolean isCJKCharacter(char input){
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(input);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS  
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS  
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A  
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION  
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
				//韩文字符集
				|| ub == Character.UnicodeBlock.HANGUL_SYLLABLES 
				|| ub == Character.UnicodeBlock.HANGUL_JAMO
				|| ub == Character.UnicodeBlock.HANGUL_COMPATIBILITY_JAMO
				//日文字符集
				|| ub == Character.UnicodeBlock.HIRAGANA //平假名
				|| ub == Character.UnicodeBlock.KATAKANA //片假名
				|| ub == Character.UnicodeBlock.KATAKANA_PHONETIC_EXTENSIONS
				) {  
			return true;
		}else{
			return false;
		}
	}

	/**
	 * 进行字符规格化（全角转半角，大写转小写处理）
	 * @param input
	 * @return char
	 */
	public static char regularize(char input){
        if (input == 12288) {
            input = (char) 32;
            
        }else if (input > 65280 && input < 65375) {
            input = (char) (input - 65248);
            
        }else if (input >= 'A' && input <= 'Z') {
        	input += 32;
		}
        
        return input;
	}
	
	private boolean isLetterConnector(char input){
		for(char c : Sign_Connector){
			if(c == input){
				return true;
			}
		}
		return false;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		char a ='o';
		out.println(isChinese(a));
	}

}
