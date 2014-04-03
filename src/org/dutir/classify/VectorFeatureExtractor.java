/**
 * 
 */
package org.dutir.classify;

import java.util.HashMap;
import java.util.Map;

import org.dutir.math.matrix.AbstractVector;
import org.dutir.util.FeatureExtractor;


/**
 * @author yezheng
 *
 */
public class VectorFeatureExtractor implements FeatureExtractor<AbstractVector>{

	/* (non-Javadoc)
	 * @see com.aliasi.util.FeatureExtractor#features(java.lang.Object)
	 */
	public Map<String,? extends Number> features(AbstractVector vector) {
		// TODO Auto-generated method stub
		Map<String,Double> map = new HashMap<String, Double>();
		for(int i=0, n = vector.numDimensions(); i < n; i++){
			map.put(""+ i, new Double(vector.value(i)));
			
		}
		return map;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
