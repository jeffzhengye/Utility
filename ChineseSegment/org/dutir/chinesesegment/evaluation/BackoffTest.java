package org.dutir.chinesesegment.evaluation;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.dutir.chinesesegment.DictionaryManager;
import org.dutir.chinesesegment.WordSegmenter;
import org.dutir.chinesesegment.WordType;
import org.dutir.util.Strings;
import org.dutir.util.dict.DictionaryEntry;
import org.dutir.util.dict.FHashTrieDictionary;


import com.aliasi.classify.PrecisionRecallEvaluation;
import com.aliasi.lm.NGramProcessLM;
import com.aliasi.lm.TrieCharSeqCounter;
import com.aliasi.spell.CompiledSpellChecker;
import com.aliasi.spell.FixedWeightEditDistance;
import com.aliasi.spell.TrainSpellChecker;
import com.aliasi.spell.WeightedEditDistance;
import com.aliasi.util.AbstractExternalizable;
import com.aliasi.util.Compilable;
import com.aliasi.util.Files;
import com.aliasi.util.ObjectToCounterMap;
import com.aliasi.util.Streams;
import com.aliasi.util.Tuple;

public class BackoffTest {
    PrecisionRecallEvaluation mBreakEval = new PrecisionRecallEvaluation();
    PrecisionRecallEvaluation mChunkEval = new PrecisionRecallEvaluation();

    ObjectToCounterMap<Integer> mReferenceLengthHistogram
        = new ObjectToCounterMap<Integer>();
    ObjectToCounterMap<Integer> mResponseLengthHistogram
        = new ObjectToCounterMap<Integer>();

    Set<Character> mTrainingCharSet = new HashSet<Character>();
    Set<Character> mTestCharSet = new HashSet<Character>();
    Set<String> mTrainingTokenSet = new HashSet<String>();
    Set<String> mTestTokenSet = new HashSet<String>();
    WordSegmenter seg = null;
    

    // parameter values
    File mDataDir;
    String mTrainingCorpusName;
    String mTestCorpusName;
    File mOutputFile;
    File mKnownToksFile;
    Writer mOutputWriter;
    String mCharEncoding;
    int mMaxNGram;
    double mLambdaFactor;
    int mNumChars;
    int mMaxNBest;
    double mContinueWeight;
    double mBreakWeight;

    public BackoffTest(String[] args) {
        System.out.println("CHINESE TOKENS");

        mDataDir = new File(args[0]);
        mTrainingCorpusName = args[1];
        mOutputFile = new File(mDataDir,args[3]+".segments");
        mKnownToksFile = new File(mDataDir,args[3]+".knownWords");
        mCharEncoding = args[4];
        mMaxNGram = Integer.valueOf(args[5]);
        mLambdaFactor = Double.valueOf(args[6]);
        mNumChars = Integer.valueOf(args[7]);
//        mMaxNBest = Integer.valueOf(args[8]);
//        mContinueWeight = Double.valueOf(args[9]);
//        mBreakWeight = Double.valueOf(args[10]);

        System.out.println("    Data Directory=" + mDataDir);
        System.out.println("    Train Corpus Name=" + mTrainingCorpusName);
        System.out.println("    Test Corpus Name=" + mTestCorpusName);
        System.out.println("    Output File Name=" + mOutputFile);
        System.out.println("    Known Tokens File Name=" + mKnownToksFile);
        System.out.println("    Char Encoding=" + mCharEncoding);
        System.out.println("    Max N-gram=" + mMaxNGram);
        System.out.println("    Lambda factor=" + mLambdaFactor);
        System.out.println("    Num chars=" + mNumChars);
        System.out.println("    Max n-best=" + mMaxNBest);
        System.out.println("    Continue weight=" + mContinueWeight);
        System.out.println("    Break weight=" + mBreakWeight);
    }

    void run() throws ClassNotFoundException, IOException {
        train();
        test();
        printResults();
    }

	void train() throws IOException, ClassNotFoundException {
		FHashTrieDictionary<String> wordDict = new FHashTrieDictionary<String>();
		NGramProcessLM lm = new NGramProcessLM(mMaxNGram);
		lm.setLambdaFactor(mLambdaFactor); 

		File trainingFile = new File(mDataDir, mTrainingCorpusName
				+ "_training.zip");

		try {
			System.out.println("Training Zip File=" + trainingFile);
			FileInputStream fileIn = new FileInputStream(trainingFile);
			ZipInputStream zipIn = new ZipInputStream(fileIn);
			ZipEntry entry = null;
			while ((entry = zipIn.getNextEntry()) != null) {
				String name = entry.getName();
				String[] lines = extractLines(zipIn, mTrainingCharSet,mTrainingTokenSet, wordDict, mCharEncoding);

				for (int i = 0; i < lines.length; ++i) {
					lm.train(lines[i]);
					if (i % 100 == 0) {
						System.out.println("process line: " + i + ", "
								+ lines[i]);
					}
				}
			}
			DictionaryManager.setup(wordDict, lm);
			seg = new WordSegmenter();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    void test() throws IOException {
        OutputStream out = new FileOutputStream(mOutputFile);
        mOutputWriter = new OutputStreamWriter(out,mCharEncoding);
        File file = new File(mDataDir,mTestCorpusName+"-testref.txt");
        System.out.println("Testing Results. File=" + file);
        FileInputStream fileIn = new FileInputStream(file);
        String[] lines = extractLines(fileIn, mTestCharSet,mTestTokenSet, null, mCharEncoding);
        for (int i = 0; i < lines.length; ++i)
            test(lines[i]);
        Streams.closeInputStream(fileIn);
        Streams.closeWriter(mOutputWriter);
    }
    
    void test(String reference) throws IOException {
        String testInput = reference.replaceAll(" ","");

        String response = Strings.concatenate(seg.segmentSentence(testInput));
        response += ' ';

        mOutputWriter.write(response);
        mOutputWriter.write("\n");

        Set<Integer> refSpaces = getSpaces(reference);
        Set<Integer> responseSpaces = getSpaces(response);
        prEval("Break Points",refSpaces,responseSpaces,mBreakEval);

        Set<Tuple<Integer>> refChunks
            = getChunks(reference,mReferenceLengthHistogram);
        Set<Tuple<Integer>> responseChunks
            = getChunks(response,mResponseLengthHistogram);
        prEval("Chunks",refChunks,responseChunks,mChunkEval);
    }

    void printResults() throws IOException {
        StringBuilder sb = new StringBuilder();
        for (String token : mTrainingTokenSet) {
            sb.append(token);
            sb.append('\n');
        }
        Files.writeStringToFile(sb.toString(),mKnownToksFile,mCharEncoding);

        System.out.print("  # Training Toks=" + mTrainingTokenSet.size());
        System.out.println("  # Unknown Test Toks="
                           + sizeOfDiff(mTestTokenSet,mTrainingTokenSet));

        System.out.print("  # Training Chars=" + mTrainingCharSet.size());
        System.out.println("  # Unknown Test Chars="
                           + sizeOfDiff(mTestCharSet,mTrainingCharSet));

        System.out.println("Token Length, #REF, #RESP, Diff");
        for (int i = 1; i < 10; ++i) {
            Integer iObj = Integer.valueOf(i);
            int refCount = mReferenceLengthHistogram.getCount(iObj);
            int respCount = mResponseLengthHistogram.getCount(iObj);
            int diff = respCount-refCount;
            System.out.println("    " + i
                               + ", " + refCount
                               + ", " + respCount
                               + ", " + diff);
        }

        System.out.println("Scores");
        System.out.println("  EndPoint:"
                           + " P=" + mBreakEval.precision()
                           + " R=" + mBreakEval.recall()
                           + " F=" + mBreakEval.fMeasure());
        System.out.println("     Chunk:"
                           + " P=" + mChunkEval.precision()
                           + " R=" + mChunkEval.recall()
                           + " F=" + mChunkEval.fMeasure());
    }





	static void addTokChars(Set<Character> charSet,
            Set<String> tokSet, FHashTrieDictionary<String> wordDict, String line) {
		if (line.indexOf("  ") >= 0) {
			String msg = "Illegal double space.\n" + "    line=/" + line + "/";
			throw new RuntimeException(msg);
		}
		String[] toks = line.split("\\s+");
		for (int i = 0; i < toks.length; ++i) {
			String tok = toks[i];
			if (tok.length() == 0) {
				String msg = "Illegal token length= 0\n" + "    line=/" + line
						+ "/";
				throw new RuntimeException(msg);
			}
			tokSet.add(tok);
			if(wordDict != null){
				DictionaryEntry<String> entry = wordDict.firstPhraseEntries(tok.toCharArray());
				if(entry == null){
					entry = new DictionaryEntry<String>(tok, "" +WordType.STRING, 1, 1);
					wordDict.addEntry(entry);
				}else{
					entry.setCount(entry.count() + 1);
				}
			}

			for (int j = 0; j < tok.length(); ++j) {
				charSet.add(Character.valueOf(tok.charAt(j)));
			}
		}
	}


    static <E> void prEval(String evalName,
                           Set<E> refSet,
                           Set<E> responseSet,
                           PrecisionRecallEvaluation eval) {
        for (E e : refSet)
            eval.addCase(true,responseSet.contains(e));

        for (E e : responseSet)
            if (!refSet.contains(e))
                eval.addCase(false,true);
    }



    // size of (set1 - set2)
    static <E> int sizeOfDiff(Set<E> set1, Set<E> set2) {
        HashSet<E> diff = new HashSet<E>(set1);
        diff.removeAll(set2);
        return diff.size();
    }

//    static String[] extractLines(InputStream in, Set<Character> charSet, Set<String> tokenSet,
//                                 String encoding)
//        throws IOException {
//
//        ArrayList<String> lineList = new ArrayList<String>();
//        InputStreamReader reader = new InputStreamReader(in);
//        BufferedReader bufReader = new BufferedReader(reader);
//        String refLine;
//        while ((refLine = bufReader.readLine()) != null) {
//            String trimmedLine = refLine.trim() + " ";
//            String normalizedLine = trimmedLine.replaceAll("\\s+"," ");
//            lineList.add(normalizedLine);
//            addTokChars(normalizedLine);
//        }
//        return lineList.toArray(new String[0]);
//    }
    
	static String[] extractLines(InputStream in, Set<Character> charSet, Set<String> tokenSet,
			FHashTrieDictionary<String> wordDict, String encoding) throws IOException {

		ArrayList<String> lineList = new ArrayList<String>();
		InputStreamReader reader = new InputStreamReader(in, encoding);
		BufferedReader bufReader = new BufferedReader(reader);
		String refLine;
		while ((refLine = bufReader.readLine()) != null) {
			String trimmedLine = refLine.trim() + " ";
			String normalizedLine = trimmedLine.replaceAll("\\s+", " ");
			lineList.add(normalizedLine);
//			System.out.println(trimmedLine);
			addTokChars(charSet,tokenSet,wordDict, normalizedLine);
		}
		return lineList.toArray(new String[0]);
	}

    static Set<Integer> getSpaces(String xs) {
        Set<Integer> breakSet = new HashSet<Integer>();
        int index = 0;
        for (int i = 0; i < xs.length(); ++i)
            if (xs.charAt(i) == ' ')
                breakSet.add(Integer.valueOf(index));
            else
                ++index;
        return breakSet;
    }

    static Set<Tuple<Integer>>
        getChunks(String xs,
                  ObjectToCounterMap<Integer> lengthCounter) {
        Set<Tuple<Integer>> chunkSet = new HashSet<Tuple<Integer>>();
        String[] chunks = xs.split(" ");
        int index = 0;
        for (int i = 0; i < chunks.length; ++i) {
            int len = chunks[i].length();
            Tuple<Integer> chunk
                = Tuple.create(Integer.valueOf(index),
                               Integer.valueOf(index+len));
            chunkSet.add(chunk);
            index += len;
            lengthCounter.increment(Integer.valueOf(len));
        }
        return chunkSet;
    }

    public static final class ChineseTokenizing
        extends FixedWeightEditDistance
        implements Compilable {

        static final long serialVersionUID = -756371L;

        private final double mBreakWeight;
        private final double mContinueWeight;

        public ChineseTokenizing(double breakWeight, double continueWeight) {
            mBreakWeight = breakWeight;
            mContinueWeight = continueWeight;
        }

        public double insertWeight(char cInserted) {
            return cInserted == ' ' ? mBreakWeight : Double.NEGATIVE_INFINITY;
        }
        public double matchWeight(char cMatched) {
            return mContinueWeight;
        }
        public void compileTo(ObjectOutput objOut) throws IOException {
            objOut.writeObject(new Externalizable(this));
        }
        private static class Externalizable extends AbstractExternalizable {
            static final long serialVersionUID = -756373L;
            final ChineseTokenizing mDistance;
            public Externalizable() { this(null); }
            public Externalizable(ChineseTokenizing distance) {
                mDistance = distance;
            }
            public void writeExternal(ObjectOutput objOut) throws IOException {
                objOut.writeDouble(mDistance.mBreakWeight);
                objOut.writeDouble(mDistance.mContinueWeight);
            }
            public Object read(ObjectInput objIn)
                throws IOException, ClassNotFoundException {

                double breakWeight = objIn.readDouble();
                double continueWeight = objIn.readDouble();
                return new ChineseTokenizing(breakWeight,continueWeight);
            }
        }
    }
    
    public static void main(String[] args) {
        try {
        	String[] para = new String[]{"/win/workspace/OpenSource/LingPipe/lingpipe-3.9.0/demos/data/backoff2003", 
        			"cityu", "hk", "cityu.out", "Big5_HKSCS", "4", "5", "5000"};
        	para = new String[]{"/win/workspace/OpenSource/LingPipe/lingpipe-3.9.0/demos/data/backoff2003", 
        			"as", "as", "as.out", "CP950", "4", "5", "5000"};
            new BackoffTest(para).run();
        } catch (Throwable t) {
            System.out.println("EXCEPTION IN RUN:");
            t.printStackTrace(System.out);
        }
    }
    
}
