import org.dutir.tokenizer.ChineseTokenizerFactory;

import com.aliasi.tokenizer.IndoEuropeanTokenizerFactory;
import com.aliasi.tokenizer.NGramTokenizerFactory;
import com.aliasi.tokenizer.Tokenizer;
import com.aliasi.util.Strings;

public class TestLingpipeTokenizer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String test = "送货时间太长，从订货到我手里足足用了6天。"
				+ "还有不是说免运费吗？为什么还收了我8块钱。I wait for too long a tim";
//		test = "waiting is no good for you. How are you? ";
		// NGramTokenizerFactory factory = new NGramTokenizerFactory(0, 4);
//		IndoEuropeanTokenizerFactory factory = new IndoEuropeanTokenizerFactory();
		 ChineseTokenizerFactory factory = new ChineseTokenizerFactory();
		Tokenizer tokenizer = factory.tokenizer(test.toCharArray(), 0, test
				.length());
		String token = null;
		while ((token = tokenizer.nextToken()) != null) {
			System.out.println(token + ":" + tokenizer.lastTokenStartPosition()
					+ ", " + tokenizer.lastTokenEndPosition());
			tokenizer.nextWhitespace();
		}
		System.out.println("results:"
				+ Strings.concatenate(tokenizer.tokenize(), "\t"));

	}

}
