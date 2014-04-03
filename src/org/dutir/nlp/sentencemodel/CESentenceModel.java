/**
 * 
 */
package org.dutir.nlp.sentencemodel;

import java.util.ArrayList;
import java.util.HashSet;

import org.dutir.tokenizer.SingleCharTokenizerFactory;

import com.aliasi.tokenizer.Tokenizer;
import com.aliasi.tokenizer.TokenizerFactory;

/**
 * a sentence heuristic model used to split Chinese and English text into
 * sentences
 * 
 * @author Yezheng
 * 
 */
public class CESentenceModel extends CHeuristicSentenceModel {

	private static final HashSet<String> POSSIBLE_STOPS = new HashSet<String>();
	static {

		POSSIBLE_STOPS.add(".");
		POSSIBLE_STOPS.add("?");
		POSSIBLE_STOPS.add("!");

		// Chinese
		POSSIBLE_STOPS.add("。");
		POSSIBLE_STOPS.add("？");
		POSSIBLE_STOPS.add("！");
		// POSSIBLE_STOPS.add(".");
	}

	private static final HashSet<String> IMPOSSIBLE_PENULTIMATES = new HashSet<String>();
	static {
		// Common Abbreviations
		// IMPOSSIBLE_PENULTIMATES.add("Bros");
		// Personal Honorifics
		IMPOSSIBLE_PENULTIMATES.add("Mme");
		// IMPOSSIBLE_PENULTIMATES.add("Mr");
		// Professional Honorifics
		IMPOSSIBLE_PENULTIMATES.add("Dr");
		// Name Suffixes
		// IMPOSSIBLE_PENULTIMATES.add("Jr");
		// Corporate Designators
		// IMPOSSIBLE_PENULTIMATES.add("Co");
	}

	private static final HashSet<String> IMPOSSIBLE_SENTENCE_STARTS = new HashSet<String>();
	static {
		IMPOSSIBLE_SENTENCE_STARTS.add(")");
		IMPOSSIBLE_SENTENCE_STARTS.add("]");
		IMPOSSIBLE_SENTENCE_STARTS.add("}");
		IMPOSSIBLE_SENTENCE_STARTS.add(">");
		// IMPOSSIBLE_SENTENCE_STARTS.add("<");
		IMPOSSIBLE_SENTENCE_STARTS.add(".");
		IMPOSSIBLE_SENTENCE_STARTS.add("!");
		IMPOSSIBLE_SENTENCE_STARTS.add("?");
		IMPOSSIBLE_SENTENCE_STARTS.add(":");
		IMPOSSIBLE_SENTENCE_STARTS.add(";");
		IMPOSSIBLE_SENTENCE_STARTS.add("-");
		IMPOSSIBLE_SENTENCE_STARTS.add("--");
		IMPOSSIBLE_SENTENCE_STARTS.add("---");
		IMPOSSIBLE_SENTENCE_STARTS.add("%");

		// chinese
		IMPOSSIBLE_SENTENCE_STARTS.add("。");
		IMPOSSIBLE_SENTENCE_STARTS.add("？");
		IMPOSSIBLE_SENTENCE_STARTS.add("！");
		IMPOSSIBLE_SENTENCE_STARTS.add("；");
		IMPOSSIBLE_SENTENCE_STARTS.add("，");
		IMPOSSIBLE_SENTENCE_STARTS.add("、");
		IMPOSSIBLE_SENTENCE_STARTS.add("（");
		IMPOSSIBLE_SENTENCE_STARTS.add("）");
		IMPOSSIBLE_SENTENCE_STARTS.add("《");
		IMPOSSIBLE_SENTENCE_STARTS.add("》");
	}

	public static String[] simpleSplite(String text) {
		ArrayList<String> retList = new ArrayList<String>();
		int start = 0;
		for (int i = 0, n = text.length(); i < n; i++) {
			int c = text.charAt(i);
			if (POSSIBLE_STOPS.contains(text.substring(i, i + 1))) {
				retList.add(text.substring(start, i + 1));
				start = i + 1;
			}
		}
		if (start < text.length()) {
			retList.add(text.substring(start));
		}
		return retList.toArray(new String[0]);
	}

	TokenizerFactory factory = null;

	/**
	 * Construct
	 */
	public CESentenceModel() {
		super(POSSIBLE_STOPS, IMPOSSIBLE_PENULTIMATES,
				IMPOSSIBLE_SENTENCE_STARTS, false, // force final stop
				true); // balance parens
	}

	public CESentenceModel(TokenizerFactory factory) {
		super(POSSIBLE_STOPS, IMPOSSIBLE_PENULTIMATES,
				IMPOSSIBLE_SENTENCE_STARTS, true, // force final stop
				true); // balance parens
		this.factory = factory;
	}

	
	static void print(String[] tokens, String[] whites) {
		for (int i = 0; i < tokens.length; i++) {
			System.out.println(tokens[i] + ":" + whites[i]);
		}
	}

	public String[] getSentences(String text) {
		if (text == null) {
			return new String[0];
		}
		ArrayList<String> tokenList = new ArrayList<String>();
		ArrayList<String> whiteList = new ArrayList<String>();
		Tokenizer tokenizer = factory.tokenizer(text.toCharArray(), 0, text
				.length());
		tokenizer.tokenize(tokenList, whiteList);

		String[] tokens = new String[tokenList.size()];
		String[] whites = new String[whiteList.size()];
		tokenList.toArray(tokens);
		whiteList.toArray(whites);
		// print(tokens, whites);
		int[] sentenceBoundaries = this.boundaryIndices(tokens, whites);
		ArrayList<String> retList = new ArrayList<String>();
		int sentStartTok = 0;
		int sentEndTok = 0;
		for (int i = 0; i < sentenceBoundaries.length; ++i) {
			sentEndTok = sentenceBoundaries[i];
			StringBuilder buf = new StringBuilder();
			for (int j = sentStartTok; j <= sentEndTok; j++) {
				buf.append(tokens[j] + whites[j + 1]);
			}
			retList.add(buf.toString());
			sentStartTok = sentEndTok + 1;
		}
		return retList.toArray(new String[0]);
	}

	public int[] getBoundries(String text) {
		ArrayList<String> tokenList = new ArrayList<String>();
		ArrayList<String> whiteList = new ArrayList<String>();
		Tokenizer tokenizer = factory.tokenizer(text.toCharArray(), 0, text
				.length());
		tokenizer.tokenize(tokenList, whiteList);

		System.out.println(tokenList.size() + " TOKENS");
		System.out.println(whiteList.size() + " WHITESPACES");

		String[] tokens = new String[tokenList.size()];
		String[] whites = new String[whiteList.size()];
		tokenList.toArray(tokens);
		whiteList.toArray(whites);

		int[] sentenceBoundaries = this.boundaryIndices(tokens, whites);
		return sentenceBoundaries;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TokenizerFactory TOKENIZER_FACTORY = new SingleCharTokenizerFactory();// new
																				// IndoEuropeanTokenizerFactory();
		CESentenceModel SENTENCE_MODEL = new CESentenceModel(TOKENIZER_FACTORY);
		String text = "be-good.(中国真大啊)。不错。good enough, bu need to improve.";
//		text = "不错。good enough";
		//		
		// System.out.println(tokenList.size() + " TOKENS");
		// System.out.println(whiteList.size() + " WHITESPACES");
		String sentences[] = SENTENCE_MODEL.getSentences(text);

		for (int i = 0; i < sentences.length; ++i) {
			System.out.print("SENTENCE " + (i + 1) + ": ");
			System.out.println(sentences[i]);
		}
	}

}
