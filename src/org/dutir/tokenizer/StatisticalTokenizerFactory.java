//package org.dutir.tokenizer;
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashSet;
//
//import org.dutir.nlp.sentencemodel.CESentenceModel;
//import org.dutir.util.stream.StreamGenerator;
//import org.dutir.util.WordlistLoader;
//import org.dutir.util.XMLConfigure;
//
//import com.aliasi.spell.CompiledSpellChecker;
//import com.aliasi.tokenizer.IndoEuropeanTokenizerFactory;
//import com.aliasi.tokenizer.RegExTokenizerFactory;
//import com.aliasi.tokenizer.StopListFilterTokenizer;
//import com.aliasi.tokenizer.Tokenizer;
//
///**
// * used to split text written in mixture of Chinese and English.
// * 
// * problems:
// * 1. Is it possible to train a tokenizer only from a dictionary? 
// * @author Yezheng
// *
// */
//
//
//public class StatisticalTokenizerFactory extends RegExTokenizerFactory {
//
//	private final CESentenceModel smodel = new CESentenceModel(new IndoEuropeanTokenizerFactory());
//	private final CompiledSpellChecker mSpellChecker;
//	private static HashSet StopSet =null;
//	static XMLConfigure config = XMLConfigure.getConf("sigir09.xml");
//	static {
//		try {
//			String path = config.get("CHINESE-ENGLISH.STOPWORD", "Resource/CHINESE-ENGLISH.STOPWORD");
//			StopSet = WordlistLoader.getWordSet(new File(path));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	
//	private boolean filterTag = false;
//	
//	public void setFiterTag(boolean tag){
//		this.filterTag = tag;
//		System.out.println("StopWord Remove Setted");
//	}
//	
//	public StatisticalTokenizerFactory(CompiledSpellChecker spellChecker) {
//		super("\\s+"); // break on spaces
//		mSpellChecker = spellChecker;
//	}
//
//	public Tokenizer tokenizer(char[] cs, int start, int length) {
//		String input = new String(cs, start, length);
//		String output = mSpellChecker.didYouMean(input);
//		char[] csOut = output.toCharArray();
//		if(filterTag)return new StopListFilterTokenizer(super.tokenizer(csOut, 0, csOut.length),StopSet );
//		return super.tokenizer(csOut, 0, csOut.length);
//	}
//	
//	public Tokenizer tokenizer(String text) {
//		return tokenizer(text.toCharArray(), 0, text.length());
//	}
//	
//	public String[] tokenizerIntoWords(String text){
//		Tokenizer tknz = tokenizer(text);
//		ArrayList<String> tokens = new ArrayList<String>();
//		ArrayList<String> whitespaces = new ArrayList<String>();
//		tknz.tokenize(tokens, whitespaces);
//		return tokens.toArray(new String[0]);
//	}
//	
//	public String tokenizerIntoWordString(String text){
//		String[] strs = tokenizerIntoWords(text);
//		StringBuilder build = new StringBuilder();
//		for(int i=0; i < strs.length; i++){
//			build.append(strs[i] + " ");
//		}
//		return build.toString();
//	}
//	
//	public String tokenizerLongTextIntoWordString(String text){
//		String sentences[] = smodel.simpleSplite(text);
////		System.out.println("Sentence: " + text);
//		StringBuilder build = new StringBuilder();
//		for(int i=0; i< sentences.length; i++){
////			System.out.println("Sentence: " + sentences[i]);
//			String strs = tokenizerIntoWordString(sentences[i]);
//			build.append(strs + " ");
//		}
//		return build.toString();
//	}
//	
//	public void tokenizer(ArrayList<String> tokens, ArrayList<String> whitespaces, String text){
//		Tokenizer tknz = tokenizer(text);
//		tknz.tokenize(tokens, whitespaces);
//	}
//	
//	static StatisticalTokenizerFactory STF = null;
//	
//	public static StatisticalTokenizerFactory getNewTokenizerFactory(String path){
//		long start = System.currentTimeMillis();
//		CompiledSpellChecker csc =SpellCheckerTrainer.readModel(new File(path));
//		StatisticalTokenizerFactory factory = new StatisticalTokenizerFactory(csc);
//		System.out.println("load Dict time: " + (System.currentTimeMillis() - start));
//		return factory;
//	}
//	/**
//	 * if exist, return the current one, else create and return;
//	 * @param path
//	 * @return
//	 */
//	public static StatisticalTokenizerFactory getTokenizerFactory(String path){
//		long start = System.currentTimeMillis();
//		if(STF == null){
//			CompiledSpellChecker csc =SpellCheckerTrainer.readModel(new File(path));
//			STF = new StatisticalTokenizerFactory(csc);
//		}
//		System.out.println("load Dict time: " + (System.currentTimeMillis() - start));
//		return STF;
//	}
//	
//	public static void testLM(){
//		String path = "s:/Chinese_both.Model";
////		path = "s:/Chinese_corpus.Model";
////		path = "s:/Chinese_dict.Model";
//		CompiledSpellChecker csc =SpellCheckerTrainer.readModel(new File(path));
//		
//	}
//	
//	public static void main(String args[]){
//		String path = "E:/eclipse-java-europa-winter-win32/eclipse/workspace/lingpipe-3.6.0/demos/data/ChineseSegment/DictTraningFiles/Chinese_both.Model";
//		path = "s:/Chinese_corpus.Model";
//		path = config.get("Chinese.Model", path);
//		
//		CompiledSpellChecker csc =SpellCheckerTrainer.readModel(new File(path));
//		StatisticalTokenizerFactory factory = new StatisticalTokenizerFactory(csc);
////		factory.setFiterTag(true);
//		String line = null;
//		BufferedReader br = StreamGenerator.getConsoleReader();
//		System.out.print("input: ");
//		try {
//			while((line = br.readLine()) != null){
//				Tokenizer tknz = factory.tokenizer(line.toCharArray(), 0, line.length());
//				String token = null;
//				StringBuffer buf = new StringBuffer();
//				while((token = tknz.nextToken()) != null){
//					buf.append(token  + tknz.nextWhitespace());
//				}
//				System.out.println("splited:\n" + buf.toString());
//				System.out.print("input: ");
//			}
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	
//}
