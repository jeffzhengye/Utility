///**
// * 
// */
//package org.dutir.classify;
//
//import java.util.ArrayList;
//import java.util.Map;
//
//import org.dutir.util.Pair;
//
//import com.aliasi.classify.Classification;
//import com.aliasi.classify.DynamicLMClassifier;
//import com.aliasi.classify.JointClassification;
//import com.aliasi.corpus.ClassificationHandler;
//import com.aliasi.corpus.Corpus;
//import com.aliasi.lm.LanguageModel;
//import com.aliasi.lm.NGramBoundaryLM;
//import com.aliasi.lm.TokenizedLM;
//import com.aliasi.lm.UniformBoundaryLM;
//import com.aliasi.tokenizer.TokenizerFactory;
//import com.aliasi.util.FeatureExtractor;
//
///**
// * 
// * NClassifier deals with features that have numeric value. 
// * @author zheng
// * 
// */
//public class NaiveBayesClassifier<E> extends NClassifier<E> {
//	DynamicLMClassifier<TokenizedLM> dlm;
//	ArrayList<Pair<E, Classification>> trainSet = new ArrayList<Pair<E,Classification>>();
//	
//	@Override
//	public void train() {
//		Pair<E, Classification> pair = null;
//		for(int i=0, n = trainSet.size(); i < n; i++){
//			pair = trainSet.get(i);
//			dlm.train( pair.second.bestCategory(), E2String(pair.first));
//		}
//	}
//
//	@Override
//	public void train(FeatureExtractor<? super E> featureExtractor,
//			Corpus<ClassificationHandler<E, Classification>> corpus) {
//		
//	}
//
//	@Override
//	public JointClassification classify(E input) {
//		return dlm.classify(E2String(input));
//	}
//
//	@Override
//	public void handle(E input, Classification classification) {
//		// TODO Auto-generated method stub
//		trainSet.add(new Pair<E, Classification>(input, classification));
//	}
//
//	/**
//	 * in order to keep a normalized interface, we compromise with some efficiency. 
//	 * 
//	 * @param input
//	 * @return
//	 */
//	private String E2String(E input){
//		StringBuilder buf = new StringBuilder();
//		if(input instanceof Map){
//			Map<String,? extends Number> map = (Map<String,? extends Number>)input;
//			String keys[] = map.keySet().toArray(new String[0]);
//			for(int i =0; i < keys.length; i++){
//				int len = map.get(keys[i]).intValue();
//				for(int j=0; j < len; j++)buf.append(keys[i] + " ");
//			}
//			
//		}else if(input instanceof String){
//			return (String) input;
//		}
//		return  buf.toString();
//	}
//	
//	public NaiveBayesClassifier(String[] categories,
//			TokenizerFactory tokenizerFactory) {
//		this(categories, tokenizerFactory, 0);
//	}
//
//	/**
//	 * Construct a naive Bayes classifier with the specified categories,
//	 * tokenizer factory and level of character n-gram for smoothing token
//	 * estimates. If the character n-gram is less than one, a uniform model will
//	 * be used.
//	 * 
//	 * <P>
//	 * There is no limit on the number of observed characters other than
//	 * {@link Character#MAX_VALUE}.
//	 * 
//	 * @param categories
//	 *            Categories into which to classify text.
//	 * @param tokenizerFactory
//	 *            Text tokenizer.
//	 * @param charSmoothingNGram
//	 *            Order of character n-gram used to smooth token estimates.
//	 * @throws IllegalArgumentException
//	 *             If there are not at least two categories.
//	 */
//	public NaiveBayesClassifier(String[] categories,
//			TokenizerFactory tokenizerFactory, int charSmoothingNGram) {
//		this(categories, tokenizerFactory, charSmoothingNGram,
//				Character.MAX_VALUE - 1);
//	}
//
//	/**
//	 * Construct a naive Bayes classifier with the specified categories,
//	 * tokenizer factory and level of character n-gram for smoothing token
//	 * estimates, along with a specification of the total number of characters
//	 * in test and training instances. If the character n-gram is less than one,
//	 * a uniform model will be used.
//	 * 
//	 * <P>
//	 * As noted in the class documentation above, setting the max observed
//	 * characters parameter to one effectively eliminates estimates of the
//	 * string of an unknown token.
//	 * 
//	 * @param categories
//	 *            Categories into which to classify text.
//	 * @param tokenizerFactory
//	 *            Text tokenizer.
//	 * @param charSmoothingNGram
//	 *            Order of character n-gram used to smooth token estimates.
//	 * @param maxObservedChars
//	 *            The maximum number of characters found in the text of training
//	 *            and test sets.
//	 * @throws IllegalArgumentException
//	 *             If there are not at least two categories or if the number of
//	 *             observed characters is less than 1 or more than the total
//	 *             number of characters.
//	 */
//	public NaiveBayesClassifier(String[] categories,
//			TokenizerFactory tokenizerFactory, int charSmoothingNGram,
//			int maxObservedChars) {
//		dlm = new DynamicLMClassifier<TokenizedLM>(categories, naiveBayesLMs(categories.length, tokenizerFactory,
//				charSmoothingNGram, maxObservedChars));
//	}
//
//	// construct the LMs for categories
//	private static TokenizedLM[] naiveBayesLMs(int length,
//			TokenizerFactory tokenizerFactory, int charSmoothingNGram,
//			int maxObservedChars) {
//
//		TokenizedLM[] lms = new TokenizedLM[length];
//		for (int i = 0; i < lms.length; ++i) {
//			LanguageModel.Sequence charLM;
//			if (charSmoothingNGram < 1)
//				charLM = new UniformBoundaryLM(maxObservedChars);
//			else
//				charLM = new NGramBoundaryLM(charSmoothingNGram,
//						maxObservedChars);
//			lms[i] = new TokenizedLM(tokenizerFactory, 1, charLM,
//					UniformBoundaryLM.ZERO_LM, 1);
//
//		}
//		return lms;
//	}
//
//	/**
//	 * @param args
//	 */
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//
//	}
//
//}
