package org.dutir.chinesesegment.evaluation;

import java.io.BufferedReader;
import java.io.IOException;

import org.dutir.chinesesegment.DictionaryManager;
import org.dutir.util.dict.DictionaryEntry;
import org.dutir.util.dict.FHashTrieDictionary;
import org.dutir.util.stream.StreamGenerator;

import com.aliasi.lm.CompiledNGramProcessLM;
import com.aliasi.lm.NGramProcessLM;

public class DictTest {

	public static void testCoreDict() {
		try {
			FHashTrieDictionary<String> fdict = DictionaryManager
					.getCoreDictionary();
			BufferedReader br = StreamGenerator.getConsoleReader();
			while (true) {
				System.out.print("Query:");
				String line = br.readLine();
				if (line != null && line.length() > 0) {
					DictionaryEntry<String> entry = fdict
							.firstPhraseEntries(line.toCharArray());
					if (entry != null) {
						System.out.println("\n" + entry.toString());
					} else {
						System.out.println("do not find");
					}

				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void testLMDict() {
		try {
			CompiledNGramProcessLM clm = DictionaryManager.getCNGramProcessLM();
			BufferedReader br = StreamGenerator.getConsoleReader();
			System.out.println("" + clm.log2ConditionalEstimate("星期天"));
			System.out.println("" + clm.log2ConditionalEstimate("星期 "));
			System.out.println("" + clm.log2ConditionalEstimate("星期 二"));
			while (true) {

				System.out.print("Query:");
				String line = br.readLine();
				if (line != null && line.length() > 0) {
					System.out.println("" + clm.log2Prob(line));
					System.out.println("" + clm.log2Estimate(line.toCharArray(), 0, line.length()));
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void printCoreDictStatistics(){
		System.out.println(DictionaryManager.getCoreDictionary().getStatistics());
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		testCoreDict();
//		testLMDict();
		printCoreDictStatistics();
	}

}
