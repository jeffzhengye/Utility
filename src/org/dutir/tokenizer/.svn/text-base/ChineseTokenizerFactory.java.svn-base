package org.dutir.tokenizer;

import com.aliasi.tokenizer.Tokenizer;
import com.aliasi.tokenizer.TokenizerFactory;

public class ChineseTokenizerFactory implements TokenizerFactory {

	public Tokenizer tokenizer(char[] arg0, int start, int length) {
		// TODO Auto-generated method stub
		return new ChineseTokenizer(arg0, start, length);
	}
	
	public Tokenizer tokenizer(String text){
		return this.tokenizer(text.toCharArray(), 0, text.length());
	}
}
