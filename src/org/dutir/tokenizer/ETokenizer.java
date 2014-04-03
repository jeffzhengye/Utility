package org.dutir.tokenizer;

import com.aliasi.tokenizer.Tokenizer;

/**
 * enhanced tokenizer
 * @author zheng
 *
 */
public abstract class ETokenizer extends Tokenizer {
	
	public void reset(String input) {
		reset(input, 0);
	}
	public abstract void reset(String input,int offset);
}
