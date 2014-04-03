/**
 * 
 */
package org.dutir.tokenizer;

import com.aliasi.tokenizer.Tokenizer;
import com.aliasi.util.Strings;

/**
 * @author Yezheng
 *
 */
public class SingleCharTokenizer extends ETokenizer {

	
    private char[] mChars;
	private int mPosition;
	private int mLastPosition;
	private int mTokenIndex;

	public SingleCharTokenizer(String chars) {
        this(chars.toCharArray(),0,chars.length());
    }
    
    public SingleCharTokenizer(StringBuffer chars) {
        this(chars.toString());
    }
    
    public SingleCharTokenizer(char[] ch, int offset, int length) {
        if (offset < 0 || offset + length > ch.length) {
            String msg = "Illegal slice."
            + " cs.length=" + ch.length
            + " offset=" + offset
            + " length=" + length;
            throw new IllegalArgumentException(msg);
        }
            mChars = ch;
            mPosition = offset;
            mLastPosition = offset+length;
            mTokenIndex = 0;
        }
	/* (non-Javadoc)
	 * @see com.aliasi.tokenizer.Tokenizer#nextToken()
	 */
	@Override
	public String nextToken() {
		// TODO Auto-generated method stub
		if(mTokenIndex < mLastPosition){
			return new String(mChars, mPosition + mTokenIndex++, 1);
		}else{
			return null;
		}
	}

    public String nextWhitespace() {
        return Strings.EMPTY_STRING;
    }
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	@Override
	public void reset(String input, int offset) {
		mChars = input.toCharArray();
		
	}

}
