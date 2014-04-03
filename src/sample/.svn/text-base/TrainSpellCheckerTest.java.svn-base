///**
// * 
// */
//package sample;
//
//import java.io.File;
//
//import com.aliasi.lm.NGramProcessLM;
//import com.aliasi.lm.TrieCharSeqCounter;
//import com.aliasi.spell.*;
//import org.dutir.tokenizer.*;
//import org.dutir.util.ObjectSerializer;
///**
// * @author Yezheng
// * Test TrainSpellChecker, but it is only a wrapping class, 
// * the main job was done by NGramProcessLM (the count of 
// * sequence is record by TrieCharSeqCounter),
// * So TrieCharSeqCounter does the main job.
// */
//
//public class TrainSpellCheckerTest {
//	static String path = "S:/Chinese_dict.Model";
//	static SpellCheckerTrainer.ChineseTokenizing ctnz = new SpellCheckerTrainer.ChineseTokenizing(0, 0);
//	
//	public static void testNGramPLM(){
////		TrainSpellChecker tsc = (TrainSpellChecker) ObjectSerializer.readObj(path);
////		NGramProcessLM nplm = tsc.languageModel();
//		NGramProcessLM nplm = new NGramProcessLM(3);
//		nplm.train(" abc ");
//		nplm.train("e ");
//		nplm.train("f");
////		nplm.train("d ");
//		
//		TrieCharSeqCounter tcsc = nplm.getTrie();
//		double a = nplm.log2Estimate("abcf");
//		double b = nplm.log2Estimate("abc f");
//		
//		System.out.println(a + " : " + b);
//		long ca = tcsc.count("d[");
//		long cb = tcsc.count(" [");
//		System.out.println(ca + " : " + cb);
//	}
//	
//	
//	/**
//	 * @param args
//	 */
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		testNGramPLM();
//	}
//
//}
