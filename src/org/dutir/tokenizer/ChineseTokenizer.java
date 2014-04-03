package org.dutir.tokenizer;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.dutir.chinesesegment.SegToken;
import org.dutir.chinesesegment.WordSegmenter;

import com.aliasi.tokenizer.CharacterTokenizerFactory;
import com.aliasi.tokenizer.Tokenizer;
import com.aliasi.tokenizer.TokenizerFactory;
import com.aliasi.util.Iterators;

/**
 * A ChineseTokenzier, LingPipe style A sequence of whitespace is treated as a
 * single token.
 * 
 * @author Zheng Ye
 * 
 */
public class ChineseTokenizer extends ETokenizer {

	static WordSegmenter seg = new WordSegmenter();
	List<SegToken> tokenList = null;
	private String sentence;
	int start = 0; // where the current sentence starts in a document.
	boolean isPos = false;
	int pos, len = 0;
	private int lastEnd;
	private int lastStart;

	public String nextToken() {
		// TODO Auto-generated method stub
		if (tokenList == null) {
			this.tokenList = seg.segmentSentence(sentence, start);
			len = tokenList.size();
		}
		if (pos < len) {
			SegToken token = tokenList.get(pos++);
			String text = token.text();
			lastStart = token.startOffset;
			lastEnd = token.endOffset;
			if (org.dutir.util.Strings.allWhitespace(text)) {
				return nextToken();
			}
			return token.text();
		}
		return null;
	}

	public String nextWhitespace() {
		if (tokenList == null) {
			this.tokenList = seg.segmentSentence(sentence, start);
			len = tokenList.size();
		}
		if (pos < len) {
			SegToken token = tokenList.get(pos);
			String text = token.text();
			if (org.dutir.util.Strings.allWhitespace(text)) {
				lastStart = token.startOffset;
				lastEnd = token.endOffset;
				pos++;
				return text;
			}
		}
		return org.dutir.util.Strings.EMPTY_STRING;
	}

	public int lastTokenEndPosition() {
		return lastEnd;
	}

	public int lastTokenStartPosition() {
		return lastStart;
	}

	public Iterator<String> iterator() {
		return new TokenIterator();
	}

	class TokenIterator extends Iterators.Buffered<String> {
		@Override
		public String bufferNext() {
			return nextToken();
		}
	}

	public ChineseTokenizer(String sentence) {
		this.sentence = sentence;
	}

	public ChineseTokenizer(char cs[], int start, int length) {
		this.sentence = new String(cs, start, length);
	}

	@Override
	public void reset(String input, int offset) {
		this.sentence = input;
		this.start = offset;
		this.tokenList = null;

	}

	public static void main(String args[]) throws IOException {
		// System.out.println(PorterStemmer.stem("token"));
		String text = "diseases this12 is a  test中国人。 热爱中华人们搞哦呢喝过，做好本分。this is good too.";
		ChineseTokenizer tokenizer = new ChineseTokenizer(text);

		String token = null;
		while ((token = tokenizer.nextToken()) != null) {
			System.out.println(token + ": "
					+ tokenizer.lastTokenStartPosition() + ","
					+ tokenizer.lastTokenEndPosition());
		}

		TokenizerFactory factory = CharacterTokenizerFactory.INSTANCE;
		Tokenizer tokenizer1 = factory.tokenizer(text.toCharArray(), 0, text
				.length());
		while ((token = tokenizer1.nextToken()) != null) {
			System.out.print(token + tokenizer1.nextWhitespace());
		}
	}

}
