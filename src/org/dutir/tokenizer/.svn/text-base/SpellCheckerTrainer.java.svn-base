///**
// * 
// */
//package org.dutir.tokenizer;
//
//import java.io.BufferedInputStream;
//import java.io.BufferedOutputStream;
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.ObjectInput;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutput;
//import java.io.ObjectOutputStream;
//import java.io.UnsupportedEncodingException;
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.Set;
//import java.util.zip.ZipEntry;
//import java.util.zip.ZipInputStream;
//
//import org.dutir.util.ObjectSerializer;
//import org.dutir.util.Pair;
//import org.dutir.util.stream.StreamGenerator;
//import org.dutir.util.stream.StreamManager;
//
//import com.aliasi.lm.NGramProcessLM;
//import com.aliasi.spell.CompiledSpellChecker;
//import com.aliasi.spell.FixedWeightEditDistance;
//import com.aliasi.spell.TrainSpellChecker;
//import com.aliasi.spell.WeightedEditDistance;
//import com.aliasi.util.AbstractExternalizable;
//import com.aliasi.util.Collections;
//import com.aliasi.util.Compilable;
//import com.aliasi.util.Files;
//import com.aliasi.util.Streams;
//
///**
// * training a TrainSpellChecker
// * 
// * @author Yezheng
// * 
// */
//public class SpellCheckerTrainer {
//	static final double MATCH_WEIGHT = -0.0;
//	static final double DELETE_WEIGHT = -4.0;
//	static final double INSERT_WEIGHT = -1.0;
//	static final double SUBSTITUTE_WEIGHT = -2.0;
//	static final double TRANSPOSE_WEIGHT = -2.0;
//	static int mMaxNGram = 4;
//	static double mLambdaFactor = 5;
//	static int mNumChars = 500;
//	static int mMaxNBest = 256;
//	static double mContinueWeight = 0.0;
//	static double mBreakWeight = 0.0;
//	static ArrayList<Pair<String, Integer>> trainList = new ArrayList<Pair<String, Integer>>();
//	private static String mCharEncoding = "utf8";
//	static {
//		trainList.add(new Pair<String, Integer>("words.txt", 5));
//		trainList.add(new Pair<String, Integer>("extend_dict.txt", 25));
//		trainList.add(new Pair<String, Integer>("punctuation.txt", 10));
//	}
//	static Set charSet = new HashSet();
//	static Set cncharSet = new HashSet();
//	static Set tokenSet = new HashSet();
//	static{
//		for(int i= 65; i < 91; i++){
//			charSet.add((char )i);
////			System.out.println("enChar:" +(char)i);
//		}
//		for(int i= 97; i < 123; i++){
//			charSet.add((char )i);
////			System.out.println("enChar:" +(char)i);
//		}
////		charSet.add('-');
//		charSet.add('0');
//		charSet.add('1');
//		charSet.add('2');
//		charSet.add('3');
//		charSet.add('4');
//		charSet.add('5');
//		charSet.add('6');
//		charSet.add('7');
//		charSet.add('8');
//		charSet.add('9');
//	}
//	
//	/**
//	 * training a TrainSpellChecker form corpus, like SIGHAN Chinese Segment
//	 * corpora.
//	 * 
//	 * @param corPath
//	 * @param mTrainingCharSet
//	 * @param modelOutFile
//	 */
//	public static void trainingFromCorpus(String corPath,
//			String mTrainingCharSet, String modelOutFile) {
//		NGramProcessLM lm = new NGramProcessLM(mMaxNGram, mNumChars,
//				mLambdaFactor);
//		WeightedEditDistance distance = new ChineseTokenizing(mContinueWeight,
//				mBreakWeight);
//		TrainSpellChecker trainer = new TrainSpellChecker(lm, distance, null);
//		//------------------keep English word as it is-------------------
//		String letters[] = Collections.toStringArray(charSet);
//		for(int i =0; i < letters.length; i++){
//			for(int j=0; j < letters.length; j++){
//				lm.train(letters[i]  + letters[j], 10);
//			}
//		}
//		//--------------------------------------------------
//		
//		File trainingFile = new File(corPath);
//		System.out.println("Training Zip File=" + trainingFile);
//		try {
//			FileInputStream fileIn = new FileInputStream(trainingFile);
//			ZipInputStream zipIn = new ZipInputStream(fileIn);
//			ZipEntry entry = null;
//			while ((entry = zipIn.getNextEntry()) != null) {
//				// String name = entry.getName();
//				String[] lines = StreamManager.extractLines(zipIn,
//						mTrainingCharSet);
//				for (int i = 0; i < lines.length; ++i){
//					String nstr = lines[i].replaceAll("\\s+", " ");
//					nstr = lines[i].replaceAll("��", " ");
//					nstr = lines[i].replaceAll("-", " ");
//					trainer.train(nstr); // normalize
////					System.out.println(lines[i]);
//				}
//			}
//			Streams.closeInputStream(zipIn);
//			writeModel(trainer, new File(modelOutFile));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	public static void trainingFromBoth(String corPath, String dictPath,
//			String mTrainingCharSet, String modelOutFile) {
//		NGramProcessLM lm = new NGramProcessLM(mMaxNGram, mNumChars,
//				mLambdaFactor);
//		WeightedEditDistance distance = new ChineseTokenizing(mContinueWeight,
//				mBreakWeight);
//		TrainSpellChecker trainer = new TrainSpellChecker(lm, distance, null);
//		
//		//------------------keep English word as it is-------------------
//		String letters[] = Collections.toStringArray(charSet);
//		for(int i =0; i < letters.length; i++){
//			for(int j=0; j < letters.length; j++){
////				System.out.println(letters[i]  + letters[j]);
//				lm.train(letters[i]  + letters[j], 20000);
//			}
//		}
//		//--------------------------------------------------
//		
//		File trainingFile = new File(corPath);
//		System.out.println("Training Zip File=" + trainingFile);
//		try {
//			FileInputStream fileIn = new FileInputStream(trainingFile);
//			ZipInputStream zipIn = new ZipInputStream(fileIn);
//			ZipEntry entry = null;
//			while ((entry = zipIn.getNextEntry()) != null) {
//				// String name = entry.getName();
//				String[] lines = StreamManager.extractLines(zipIn,
//						mCharEncoding);
//				for (int i = 0; i < lines.length; ++i) {
//					String nstr = lines[i].replaceAll("\\s+", " ");
//					nstr = lines[i].replaceAll("��", " ");
//					nstr = lines[i].replaceAll("-", " ");
//					trainer.train(nstr); // normalize
//					addTokChars(cncharSet, tokenSet, lines[i]);
//				}
//			}
//			Streams.closeInputStream(zipIn);
//			
//			// -----------------training from dictFile-------------------------------
//			trainingFile = new File(dictPath);
//			System.out
//					.println("Training Dir=" + trainingFile.getAbsolutePath());
//			for (int i = 0, n = trainList.size(); i < n; i++) {
//				String fileName = trainList.get(i).first;
//				int trainTime = trainList.get(i).second;
//				InputStream ins = StreamGenerator.getFIS(new File(trainingFile,
//						fileName));
//				String lines[] = StreamManager.extractLines(ins, mCharEncoding,
//						true);
//				ins.close();
//				if(fileName.equals("punctuation.txt")){
//					processPunctuation(lines, trainer);
//				}
//				for (int k = 0; k < lines.length; k++) {
//					trainer.train(lines[k], trainTime);
//					addTokChars(cncharSet, tokenSet, lines[k]);
//				}
//				
//			}
//			writeModel(trainer, new File(modelOutFile));
//			ObjectSerializer.writeObj(new File("S:/Chinese_dict.Model"), trainer);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	private static void processPunctuation(String[] lines,
//			TrainSpellChecker trainer) {
//		String charstr[] = Collections.toStringArray(cncharSet);
//		String enstr[] = Collections.toStringArray(charSet);
////		StringBuffer chabuf = new StringBuffer();
////		StringBuffer tokenbuf = new StringBuffer();
//		for (int i = 0; i < charstr.length; i++) {
////			chabuf.append(charstr[i] +"\n");
//			//train <cnChar-space-cnenPunctuation>
//			for (int j = 0; j < lines.length; j++) {
////				System.out.println(charstr[i] + " " + lines[j]);
//				trainer.trainENBigram(charstr[i] + " " + lines[j], 10);
//				trainer.trainENBigram(lines[j] + " " + charstr[i], 10);
//			}
//			//train <cnChar-space-enchar>
//			for (int j = 0; j < enstr.length; j++) {
////				System.out.println(charstr[i] + " " + lines[j]);
//				trainer.trainENBigram(enstr[j] + " " + charstr[i], 10);
//				trainer.trainENBigram(charstr[i] + " " + enstr[j], 10);
//			}
//		}
//		//train <encnPunctuation-space-enchar>?
//		for (int i = 0; i < lines.length; i++) {
//			for (int j = 0; j < enstr.length; j++) {
//				trainer.trainENBigram(enstr[j] + " " + lines[i], 10);
//				System.out.println(enstr[j] + " " + lines[i]);
//				trainer.trainENBigram(lines[i] + " " + enstr[j], 10);
//			}
//		}
////		String tokenstr[] = Collections.toStringArray(tokenSet);
////		for (int i = 0; i < tokenstr.length; i++) {
////			tokenbuf.append(tokenstr[i] + "\n");
////		}
////		try {
////			Files.writeBytesToFile(chabuf.toString().getBytes("utf8"), new File("s:/charList.txt"));
////			Files.writeBytesToFile(tokenbuf.toString().getBytes("utf8"), new File("s:/tokenList.txt"));
////		} catch (UnsupportedEncodingException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		} catch (IOException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
//	}
//
//	/**
//	 * Training From dictionary file(a word per line)
//	 * 
//	 * @param corPath
//	 * @param mTrainingCharSet
//	 * @param modelOutFile
//	 */
//	public static void trainingFromDictFile(String dictPath,
//			String mTrainingCharSet, String modelOutFile) {
//		NGramProcessLM lm = new NGramProcessLM(mMaxNGram, mNumChars,
//				mLambdaFactor);
//		WeightedEditDistance distance = new ChineseTokenizing(mContinueWeight,
//				mBreakWeight);
//		TrainSpellChecker trainer = new TrainSpellChecker(lm, distance, null);
//		File trainingFile = new File(dictPath);
//		System.out.println("Training Dir=" + trainingFile.getAbsolutePath());
//		for (int i = 0, n = trainList.size(); i < n; i++) {
//			String fileName = trainList.get(i).first;
//			int trainTime = trainList.get(i).second;
//			InputStream ins = StreamGenerator.getFIS(new File(trainingFile,
//					fileName));
//			String lines[] = StreamManager.extractLines(ins, mCharEncoding,
//					true);
//			for (int k = 0; k < lines.length; k++) {
//				// System.out.println("training: " + lines[k]);
//				trainer.train(" " + lines[k] + " ", trainTime);
//			}
//			try {
//				ins.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		writeModel(trainer, new File(modelOutFile));
//	}
//
//	public static void writeModel(TrainSpellChecker sc, File MODEL_FILE) {
//		// create object output stream from file
//		FileOutputStream fileOut;
//		try {
//			fileOut = new FileOutputStream(MODEL_FILE);
//			BufferedOutputStream bufOut = new BufferedOutputStream(fileOut);
//			ObjectOutputStream objOut = new ObjectOutputStream(bufOut);
//			// write the spell checker to the file
//			sc.compileTo(objOut);
//			// close the resources
//			Streams.closeOutputStream(objOut);
//			Streams.closeOutputStream(bufOut);
//			Streams.closeOutputStream(fileOut);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}
//
//   static void addTokChars(Set charSet, Set tokSet, String line) {
//        if (line.indexOf("  ") >= 0) {
//                String msg = "Illegal double space.\n"
//                    + "    line=/" + line + "/";
//                throw new RuntimeException(msg);
//        }
//        String[] toks = line.split("\\s+");
//        for (int i = 0; i < toks.length; ++i) {
//            String tok = toks[i];
//            if (tok.length() == 0) {
////                String msg = "Illegal token length= 0\n"
////                    + "    line=/" + line + "/";
////                throw new RuntimeException(msg);
//            	continue;
//            }
//            tokSet.add(tok);
//            for (int j = 0; j < tok.length(); ++j) {
//            	if(!Character.isLetter(tok.charAt(j)))
//                charSet.add(new Character(tok.charAt(j)));
//            }
//        }
//    }
//    
//	public static CompiledSpellChecker readModel(File file) {
//		try {
//			// create object input stream from file
//			FileInputStream fileIn = new FileInputStream(file);
//			BufferedInputStream bufIn = new BufferedInputStream(fileIn);
//			ObjectInputStream objIn = new ObjectInputStream(bufIn);
//
//			// read the spell checker
//			CompiledSpellChecker sc = (CompiledSpellChecker) objIn.readObject();
//
//			// close the resources and return result
//			Streams.closeInputStream(objIn);
//			Streams.closeInputStream(bufIn);
//			Streams.closeInputStream(fileIn);
//			return sc;
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//	}
//
//	public static final class ChineseTokenizing extends FixedWeightEditDistance
//			implements Compilable {
//
//		private final double mBreakWeight;
//		private final double mContinueWeight;
//
//		public ChineseTokenizing(double breakWeight, double continueWeight) {
//			mBreakWeight = breakWeight;
//			mContinueWeight = continueWeight;
//		}
//
//		public double insertWeight(char cInserted) {
//			return cInserted == ' ' ? mBreakWeight : Double.NEGATIVE_INFINITY;
//		}
//
//		public double matchWeight(char cMatched) {
//			return mContinueWeight;
//		}
//
//		public void compileTo(ObjectOutput objOut) throws IOException {
//			objOut.writeObject(new Externalizable(this));
//		}
//
//		private static class Externalizable extends AbstractExternalizable {
//			final ChineseTokenizing mDistance;
//
//			public Externalizable() {
//				this(null);
//			}
//
//			public Externalizable(ChineseTokenizing distance) {
//				mDistance = distance;
//			}
//
//			public void writeExternal(ObjectOutput objOut) throws IOException {
//				objOut.writeDouble(mDistance.mBreakWeight);
//				objOut.writeDouble(mDistance.mContinueWeight);
//			}
//
//			public Object read(ObjectInput objIn) throws IOException,
//					ClassNotFoundException {
//
//				double breakWeight = objIn.readDouble();
//				double continueWeight = objIn.readDouble();
//				return new ChineseTokenizing(breakWeight, continueWeight);
//			}
//		}
//	}
//
//	/**
//	 * @param args
//	 */
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
////		ChineseTokenizing cwed = new ChineseTokenizing();
////		System.out.println(cwed.distance("d [", "d["));
////		System.out.println(cwed.deleteWeight(' '));
////		System.exit(1);
//		
//		long start = System.currentTimeMillis();
//		String modelFile = "S:/Chinese_dict.Model";
//		String corPath = "E:/eclipse-java-europa-winter-win32/eclipse/workspace/lingpipe-3.6.0/demos/data/ChineseSegment/pk_training_utf8.zip";
//		String dictPath = "E:/eclipse-java-europa-winter-win32/eclipse/workspace/lingpipe-3.6.0/demos/data/ChineseSegment/DictTraningFiles";
//		trainingFromBoth(corPath, dictPath, "utf8", "S:/Chinese_both.Model");
////		trainingFromCorpus(corPath, "utf8", "S:/Chinese_corpus.Model");
////		trainingFromDictFile(dictPath, "utf8", "S:/Chinese_dict.Model");
//		System.out.println("time: " + (System.currentTimeMillis() - start));
//	}
//
//}
