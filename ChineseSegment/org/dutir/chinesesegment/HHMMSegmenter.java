package org.dutir.chinesesegment;

import java.util.ArrayList;
import java.util.List;

import org.dutir.util.dict.FHashTrieDictionary;
import org.dutir.util.dict.Node;

/**
 * Finds the optimal segmentation of a sentence into Chinese words
 */
public class HHMMSegmenter {

	// private static WordDictionary wordDict = WordDictionary.getInstance();
	static FHashTrieDictionary<String> wordDict = DictionaryManager
			.getCoreDictionary();

	/**
	 * Create the {@link SegGraph} for a sentence.
	 * 
	 * 
	 * @param sentence
	 *            input sentence, without start and end markers
	 * @return {@link SegGraph} corresponding to the input sentence.
	 */
	// TODO We need to find a smart way to add OVVs into SegGraph as SegTokens
	private SegGraph createSegGraph(String sentence) {
		int i = 0, j;
		int length = sentence.length();
		Node<String> foundNode = null;

		int[] charTypeArray = getCharTypes(sentence);
		boolean singleTW[] = new boolean[length];
		java.util.Arrays.fill(singleTW, false);

		StringBuilder wordBuf = new StringBuilder();
		SegToken token;
		int frequency = 1; // the number of times word appears.
		boolean hasFullWidth;
		int wordType;
		char[] charArray;

		SegGraph segGraph = new SegGraph();
		while (i < length) {
			hasFullWidth = false;
			switch (charTypeArray[i]) {

			case CharType.SPACE_LIKE:
				j = i + 1;
				while (j < length && (charTypeArray[j] == CharType.SPACE_LIKE)) {
					j++;
				}
				frequency = Utility.MAX_FREQUENCE;

				charArray = sentence.substring(i, j).toCharArray();
				token = new SegToken(charArray, i, j, WordType.DELIMITER,
						frequency);
				segGraph.addToken(token);
				i = j;
				break;
			// *****************************************/
			case CharType.HANZI:
				j = i + 1;
				wordBuf.delete(0, wordBuf.length());
				// It doesn't matter if a single Chinese character (Hanzi) can
				// form a phrase or not,
				// it will store that single Chinese character (Hanzi) in the
				// SegGraph. Otherwise, it will
				// cause word division.
				wordBuf.append(sentence.charAt(i));
				charArray = new char[] { sentence.charAt(i) };
				frequency = wordDict.getFrequency(charArray);
				token = new SegToken(charArray, i, j, WordType.CHINESE_WORD,
						frequency);
				segGraph.addToken(token);
				boolean addedTag = false;

				foundNode = wordDict.getPrefixMatch(charArray);
				while (j <= length && foundNode != null) {
					if (foundNode.getEntries().length > 0
							&& charArray.length > 1) {
						// It is the phrase we are looking for; In other words,
						// we have found a phrase SegToken
						// from i to j. It is not a monosyllabic word (single
						// word).
						// frequency = foundNode.getEntries()[0].count();
						token = new SegToken(charArray, i, j,
								WordType.CHINESE_WORD, frequency);
						segGraph.addToken(token);
						addedTag = true;
						singleTW[j - 1] = true;
					}

					if (j < length && charTypeArray[j] == CharType.HANZI) {
						wordBuf.append(sentence.charAt(j));
						charArray = new char[wordBuf.length()];
						wordBuf.getChars(0, charArray.length, charArray, 0);
						foundNode = wordDict.getPrefixMatch(sentence.charAt(j),
								foundNode);
						j++;
					} else {
						break;
					}
				}
				// TODO 
				//addedTag = false means that a character only occurs in one SegToken 
				//(namely the combinations with the following characters do not occur in the wordDict), 
				// In this circumstance, if this character is not likely to be a single character word, 
				// maybe there an OOV begins from this charater.  
//				if (!addedTag && !singleTW[i]) {
//					// probability of being a single character word
//					double tprob = DictionaryManager.getCNGramProcessLM()
//							.log2Prob(" " + sentence.charAt(i) + " ");
//					if (tprob < -20.0d) {
//						if (i + 1 < length
//								&& charTypeArray[i + 1] == CharType.HANZI) {
//							token = new SegToken(sentence.substring(i, i + 2)
//									.toCharArray(), i, i + 2,
//									WordType.CHINESE_WORD, frequency);
//							segGraph.addToken(token);
//
//							if (i + 3 <= length
//									&& charTypeArray[i + 3] == CharType.HANZI) {
//								token = new SegToken(sentence.substring(i,
//										i + 3).toCharArray(), i, i + 3,
//										WordType.CHINESE_WORD, frequency);
//								segGraph.addToken(token);
//							}
//						}
//					}
//				}
				i++;
				break;
			// *****************************************/
			case CharType.FULLWIDTH_LETTER:
				hasFullWidth = true;
				// *****************************************/
			case CharType.LETTER:
				j = i + 1;
				while (j < length
						&& (charTypeArray[j] == CharType.LETTER || charTypeArray[j] == CharType.FULLWIDTH_LETTER)) {
					if (charTypeArray[j] == CharType.FULLWIDTH_LETTER)
						hasFullWidth = true;
					j++;
				}
				// Found a Token from i to j. Type is LETTER char string.
				charArray = Utility.STRING_CHAR_ARRAY;
				// frequency = wordDict.getFrequency(charArray);
				wordType = hasFullWidth ? WordType.FULLWIDTH_STRING
						: WordType.STRING;
				token = new SegToken(charArray, i, j, wordType, frequency);
				segGraph.addToken(token);
				i = j;
				break;
			// *****************************************/
			case CharType.FULLWIDTH_DIGIT:
				hasFullWidth = true;
				// *****************************************/
			case CharType.DIGIT:
				j = i + 1;
				while (j < length
						&& (charTypeArray[j] == CharType.DIGIT || charTypeArray[j] == CharType.FULLWIDTH_DIGIT)) {
					if (charTypeArray[j] == CharType.FULLWIDTH_DIGIT)
						hasFullWidth = true;
					j++;
				}
				// Found a Token from i to j. Type is NUMBER char string.
				charArray = Utility.NUMBER_CHAR_ARRAY;
				// frequency = wordDict.getFrequency(charArray);
				wordType = hasFullWidth ? WordType.FULLWIDTH_NUMBER
						: WordType.NUMBER;
				token = new SegToken(charArray, i, j, wordType, frequency);
				segGraph.addToken(token);
				i = j;
				break;
			case CharType.DELIMITER:
				j = i + 1;
				// No need to search the weight for the punctuation. Picking the
				// highest frequency will work.
				frequency = Utility.MAX_FREQUENCE;
				charArray = new char[] { sentence.charAt(i) };
				token = new SegToken(charArray, i, j, WordType.DELIMITER,
						frequency);
				segGraph.addToken(token);
				i = j;
				break;
			// *****************************************/
			default:
				j = i + 1;
				// Treat the unrecognized char symbol as unknown string.
				// For example, any symbol not in GB2312 is treated as one of
				// these.
				charArray = Utility.STRING_CHAR_ARRAY;
				// frequency = wordDict.getFrequency(charArray);
				token = new SegToken(charArray, i, j, WordType.STRING,
						frequency);
				segGraph.addToken(token);
				i = j;
				break;
			}
		}

		// Add two more Tokens: "beginning xx beginning"
		charArray = Utility.START_CHAR_ARRAY;
		// frequency = wordDict.getFrequency(charArray);
		token = new SegToken(charArray, -1, 0, WordType.SENTENCE_BEGIN,
				frequency);
		segGraph.addToken(token);

		// "end xx end"
		charArray = Utility.END_CHAR_ARRAY;
		// frequency = wordDict.getFrequency(charArray);
		token = new SegToken(charArray, length, length + 1,
				WordType.SENTENCE_END, frequency);
		segGraph.addToken(token);

		return segGraph;
	}

	/**
	 * Get the character types for every character in a sentence.
	 * 
	 * @see Utility#getCharType(char)
	 * @param sentence
	 *            input sentence
	 * @return array of character types corresponding to character positions in
	 *         the sentence
	 */
	private static int[] getCharTypes(String sentence) {
		int length = sentence.length();
		int[] charTypeArray = new int[length];
		// the type of each character by position
		for (int i = 0; i < length; i++) {
			charTypeArray[i] = Utility.getCharType(sentence.charAt(i));
		}

		return charTypeArray;
	}

	/**
	 * Return a list of {@link SegToken} representing the best segmentation of a
	 * sentence
	 * 
	 * @param sentence
	 *            input sentence
	 * @return best segmentation as a {@link List}
	 */
	public List<SegToken> process(String sentence) {
		SegGraph segGraph = createSegGraph(sentence);
		BiSegGraph biSegGraph = new BiSegGraph(segGraph);
		List<SegToken> shortPath = biSegGraph.getShortPath();
		return shortPath;
	}

	public static void main(String args[]) {
		HHMMSegmenter seg = new HHMMSegmenter();
		List<String> testStr = new ArrayList<String>();
		testStr.add("this is a boring 第");
		testStr.add("12.第");
		testStr.add("一九九五年12月31日,");
		testStr.add("1/++ ￥+400 ");
		testStr.add("-2e-12 xxxx1E++300/++");
		testStr.add("1500名常用的数量和人名的匹配 超过22万个");
		testStr.add("据路透社报道，印度尼西亚社会事务部一官员星期二(29日)表示，"
				+ "日惹市附近当地时间27日晨5时53分发生的里氏6.2级地震已经造成至少5427人死亡，"
				+ "20000余人受伤，近20万人无家可归。");
		testStr.add("古田县城关六一四路四百零五号");
		testStr.add("欢迎使用阿江统计2.01版");
		testStr.add("51千克五十一千克五万一千克两千克拉 五十一");
		testStr.add("十一点半下班十一点下班");
		testStr.add("福州第一中学福州一中福州第三十六中赐进士及第");

		for (String t : testStr) {
			System.out.println(t);
			List<SegToken> list = seg.process(t);
			for (SegToken token : list) {
				System.err.print(token.toString() + " | ");
			}

		}
		System.out.println("***************");
	}

}
