package org.dutir.chinesesegment.evaluation;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.dutir.chinesesegment.CharType;
import org.dutir.chinesesegment.DictionaryManager;
import org.dutir.chinesesegment.Utility;
import org.dutir.chinesesegment.WordSegmenter;
import org.dutir.chinesesegment.WordType;
import org.dutir.util.Strings;
import org.dutir.util.dict.DictionaryEntry;
import org.dutir.util.dict.FHashTrieDictionary;

import com.aliasi.classify.PrecisionRecallEvaluation;
import com.aliasi.lm.NGramProcessLM;
import com.aliasi.lm.TrieCharSeqCounter;
import com.aliasi.spell.FixedWeightEditDistance;
import com.aliasi.util.AbstractExternalizable;
import com.aliasi.util.Compilable;
import com.aliasi.util.Files;
import com.aliasi.util.ObjectToCounterMap;
import com.aliasi.util.Streams;
import com.aliasi.util.Tuple;

public class BackoffTest2005 {
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
    NGramProcessLM lm = null;
    FHashTrieDictionary<String> wordDict = null;
    
    // parameter values
    File mDataDir;
    String mTrainingCorpusName;
    String mTestCorpusName;
    File mOutputFile;
    File mKnownToksFile;
    Writer mOutputWriter;
    String mCharEncoding = "utf8";
    int mMaxNGram;
    double mLambdaFactor;
    int mNumChars;
    int mMaxNBest;
    double mContinueWeight;
    double mBreakWeight;
	private File mZipFile;
	private String mCorpusName;

    public BackoffTest2005(String[] args) {
        System.out.println("CHINESE TOKENS 2005");

        mZipFile = new File(args[0],"icwb2-data.zip");
        mCorpusName = args[1];
        mOutputFile = new File(mCorpusName + ".segments");
        mKnownToksFile = new File(mCorpusName + ".knownWords");
        mMaxNGram = Integer.valueOf(args[2]);
        mLambdaFactor = Double.valueOf(args[3]);
        mNumChars = Integer.valueOf(args[4]);

        System.out.println("    Data Zip File=" + mZipFile);
        System.out.println("    Corpus Name=" + mCorpusName);
        System.out.println("    Output File Name=" + mOutputFile);
        System.out.println("    Known Tokens File Name=" + mKnownToksFile);
        System.out.println("    Max N-gram=" + mMaxNGram);
        System.out.println("    Lambda factor=" + mLambdaFactor);
        System.out.println("    Num chars=" + mNumChars);
    }

    void run() throws ClassNotFoundException, IOException {
        train();
        test();
        printResults();
    }

	void train() throws IOException, ClassNotFoundException {
		wordDict = new FHashTrieDictionary<String>();
		lm = new NGramProcessLM(mMaxNGram);
		lm.setLambdaFactor(mLambdaFactor); 
//		lm.setNumChars(mNumChars);
		mLambdaFactor = lm.getLambdaFactor();
		
		 
        String trainingEntryName = mCorpusName + "_training.utf8";
        FileInputStream fileIn = new FileInputStream(mZipFile);
        ZipInputStream zipIn = new ZipInputStream(fileIn);
        ZipEntry entry = null;
        while ((entry = zipIn.getNextEntry()) != null) {
            String name = entry.getName();
            if (!name.endsWith(trainingEntryName)) continue;
            System.out.println("Reading Data from entry=" + name);
            String[] lines = extractLines(zipIn,mTrainingCharSet,
                                                        mTrainingTokenSet,  wordDict, lm, mCharEncoding);
            //////////////////////
		    File tfile = new File(trainingEntryName);
		    if(!tfile.exists()){
		    	Files.writeStringToFile(Strings.concatenate(lines, "\n"),tfile,mCharEncoding);
		    }
		    /////////////////////
            System.out.println("  Found " + lines.length + " sentences.");
            System.out.println("  Found " + mTrainingCharSet.size()
                               + " distinct characters.");
            System.out.println("  Found " + mTrainingTokenSet.size()
                               + " distinct tokens.");
            for (int i = 0; i < lines.length; ++i)
            	lm.train(Utility.START_ARRAY + lines[i] + Utility.END_ARRAY);
            
			DictionaryManager.setup(wordDict, lm);
			seg = new WordSegmenter();
			DictionaryManager.save();
        }
        Streams.closeInputStream(zipIn);

	}

	
    void test() {
        try {
			OutputStream out = new FileOutputStream(mOutputFile);
			mOutputWriter = new BufferedWriter(new OutputStreamWriter(out,Strings.UTF8));;
			String testEntryName = mCorpusName + "_test_gold.utf8";
			String testEntryName2 = mCorpusName + "_testing_gold.utf8";
			// fudge for inconsistency of as test file in .zip file
			FileInputStream fileIn = new FileInputStream(mZipFile);
			ZipInputStream zipIn = new ZipInputStream(fileIn);
			ZipEntry entry = null;
			while ((entry = zipIn.getNextEntry()) != null) {
			    String name = entry.getName();
			    
			    if ((!name.endsWith(testEntryName))
			        && !name.endsWith(testEntryName2)) continue;
			    System.out.println("Testing Results. Zip Entry=" + name);
			    long total_time = System.currentTimeMillis(); 
			    String[] lines
			        = extractLines(zipIn, mTestCharSet,mTestTokenSet, null, null, mCharEncoding);
			    //////////////////////
			    File tfile = new File(name.endsWith(testEntryName)? testEntryName: testEntryName2);
			    if(!tfile.exists()){
			    	Files.writeStringToFile(Strings.concatenate(lines, "\n"),tfile,mCharEncoding);
			    }
			    int charNumber =0;
			    for(int i=0;i < lines.length; i++){
			    	charNumber += lines[i].length();
			    }
			    /////////////////////

			    System.out.println("    Found " + lines.length + " test sentences.");
			    long time = System.currentTimeMillis();
			    for (int i = 0; i < lines.length; ++i)
			        test(lines[i]);
			    long endtime = System.currentTimeMillis();
			    System.out.println("total_aver: " + charNumber/ ((endtime - total_time)/1000) + " char/s");
			    System.out.println("aver: " + charNumber/ ((endtime - time)/1000) + " char/s");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    
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
        
        Character[] chars = mTrainingCharSet.toArray(new Character[0]);
        double tweight = 0; 
        double weight =0; int number =0;
        for(int i =0; i < chars.length; i++){
        	double value = lm.log2Prob(" " + chars[i] + " ");
        	tweight += value;
        	if(wordDict.phraseEntries("" + chars[i]).length > 0){
        		weight += value;
        		number++;
        	}
        }
        System.out.println("average prob of being single character word: " + tweight/chars.length + ", " + chars.length);
        System.out.println("average prob of all existing single character word: " + weight/number + ", "+ number);
    }





	static void addTokChars(Set<Character> charSet,
            Set<String> tokSet, FHashTrieDictionary<String> wordDict, NGramProcessLM lm, String line) {
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
			if(wordDict != null && Utility.getCharType(tok.charAt(0)) == CharType.HANZI){
				DictionaryEntry<String> entry = wordDict.firstPhraseEntries(tok.toCharArray());
				if(entry == null){
					entry = new DictionaryEntry<String>(tok, "" +WordType.STRING, 1, 1);
					wordDict.addEntry(entry);
					if(tok.length() >1)
//					lm.train(" " + tok + " ", 10);
					lm.train(tok, 30);
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
    
	static String[] extractLines(InputStream in, Set<Character> charSet, Set<String> tokenSet,
			FHashTrieDictionary<String> wordDict, NGramProcessLM lm, String encoding) throws IOException {

		ArrayList<String> lineList = new ArrayList<String>();
		InputStreamReader reader = new InputStreamReader(in, encoding);
		BufferedReader bufReader = new BufferedReader(reader);
		String refLine;
		while ((refLine = bufReader.readLine()) != null) {
			String trimmedLine = refLine.trim() + " ";
			String normalizedLine = trimmedLine.replaceAll("\\s+", " ");
			lineList.add(normalizedLine);
//			System.out.println(trimmedLine);
			addTokChars(charSet,tokenSet,wordDict, lm, normalizedLine);
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

    
    public static void main(String[] args) {
        try {
        	String ngram = "2";
        	String lambda = "2";
        	String[] para = new String[]{"/win/workspace/OpenSource/LingPipe/lingpipe-3.9.0/demos/data/backoff2003", 
        			"pku",  ngram, lambda, "1"};
//        	para = new String[]{"/win/workspace/OpenSource/LingPipe/lingpipe-3.9.0/demos/data/backoff2003", 
//        			"msr",  "4", "5", "10"};
//        	para = new String[]{"/win/workspace/OpenSource/LingPipe/lingpipe-3.9.0/demos/data/backoff2003", 
//        			"cityu",  "4", "5", "10"};
//        	para = new String[]{"/win/workspace/OpenSource/LingPipe/lingpipe-3.9.0/demos/data/backoff2003", 
//        			"as",  "4", "5", "10"};
            new BackoffTest2005(para).run();
        } catch (Throwable t) {
            System.out.println("EXCEPTION IN RUN:");
            t.printStackTrace(System.out);
        }
    }
}
