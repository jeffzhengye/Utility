package org.dutir.chinesesegment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.dutir.util.ObjectSerializer;
import org.dutir.util.dict.DictionaryEntry;
import org.dutir.util.dict.FHashTrieDictionary;

import com.aliasi.lm.CompiledNGramProcessLM;
import com.aliasi.lm.NGramProcessLM;
import com.aliasi.lm.TrieCharSeqCounter;
import com.aliasi.util.AbstractExternalizable;

public class DictionaryManager {
	static FHashTrieDictionary<String> wordDict = null;
	private static NGramProcessLM lm = null;
	private static CompiledNGramProcessLM clm = null;
	private static String path1 = "segdata/wordDict.data";
	private static String path2 = "segdata/lm.data";
	private static String path3 = "segdata/clm.data";
	
	static int maxNGram = 4;
	static int numChars = 10;
	static double lambdaFactor = 0.1;

	
	
	public static void setup(FHashTrieDictionary<String> _wordDict, NGramProcessLM _lm){
		wordDict = _wordDict;
		lm = _lm;
	}
	
	public static FHashTrieDictionary<String> getCoreDictionary() {
		if(wordDict ==null) wordDict = (FHashTrieDictionary<String>) ObjectSerializer.readObj(path1);
		return wordDict;
	}
	
	public static CompiledNGramProcessLM getCNGramProcessLM() {
		if(clm ==null)
			try {
				File file = new File(path2);
				if(file.exists()){
					ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
					clm =(CompiledNGramProcessLM) AbstractExternalizable.readObject(file);
					ois.close();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		return clm;
	}

	public static NGramProcessLM getNGramProcessLM() {
		if(lm ==null)
			try {
				lm = NGramProcessLM.readFrom(new FileInputStream(path2));
			}  catch (Exception e) {
				e.printStackTrace();
			}
		return lm;
	}
	
	public static void save(){
		try {
			ObjectSerializer.writerObj(path1, wordDict);
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path2));
			lm.compileTo(oos);
			oos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void train(String path, String corpusName,
			String mCharEncoding) {
		wordDict = new FHashTrieDictionary<String>();
		TrieCharSeqCounter counter = new TrieCharSeqCounter(numChars);
		lm = new NGramProcessLM(maxNGram, numChars, counter);

		File trainingFile = new File(path, corpusName + "_training.zip");

		try {
			System.out.println("Training Zip File=" + trainingFile);
			FileInputStream fileIn = new FileInputStream(trainingFile);
			ZipInputStream zipIn = new ZipInputStream(fileIn);
			ZipEntry entry = null;
			while ((entry = zipIn.getNextEntry()) != null) {
				String name = entry.getName();
				String[] lines = extractLines(zipIn,wordDict, mCharEncoding);
				
				for (int i = 0; i < lines.length; ++i)
				{
					lm.train(lines[i]);
					if(i % 100 == 0 ) 
						{System.out.println("process line: " + i + ", " + lines[i]);}
				}
					
			}
			save();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	static String[] extractLines(InputStream in, 
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
			addTokChars(wordDict, normalizedLine);
		}
		return lineList.toArray(new String[0]);
	}

	static void addTokChars(FHashTrieDictionary<String> wordDict, String line) {
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
//			tokSet.add(tok);
			DictionaryEntry<String> entry = wordDict.firstPhraseEntries(tok.toCharArray());
			if(entry == null){
				entry = new DictionaryEntry<String>(tok, "" +WordType.STRING, 1, 1);
				wordDict.addEntry(entry);
			}else{
				entry.setCount(entry.count() + 1);
			}
//			for (int j = 0; j < tok.length(); ++j) {
////				charSet.add(Character.valueOf(tok.charAt(j)));
//			}
		}
	}

	public static void main(String args[]) {
		String path = "/win/workspace/OpenSource/LingPipe/lingpipe-3.9.0/demos/data/backoff2003";
		String cname = "cityu";
		train(path, cname, "Big5_HKSCS");
	}
}
