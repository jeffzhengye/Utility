/**
 * 
 */
package org.dutir.classify;

import java.io.Serializable;
import java.util.ArrayList;
import java.lang.*;

import org.dutir.math.matrix.Vector;
import org.dutir.util.NumConversions;
import org.dutir.util.Pair;




/**
 * It is similar to that in the paper of "a system to mining Large-scale Bilingual
 * Dictionary����" --Jianfeng Gao,etc.
 * @author yezheng
 *
 */

public class PerceptronRanker<E extends PerceptronRankerHandle> implements Serializable{
	final int maxNumFeature ;
	double[]  r;
	double d =0.5; //learning ratio
	
	
	public PerceptronRanker(int numOfFeature){
		this.maxNumFeature = numOfFeature;
		this.r = new double[numOfFeature];
		// initial the coefficient
		r[0] =1;
		java.util.Arrays.fill(r, 1, numOfFeature, 0);
	}
	
	public PerceptronRanker(int numOfFeature,double ratio){
		this(numOfFeature);
		this.d = ratio;
	}
	
	public void setRatio(int ratio){
		this.d = ratio;
	}
	
	public double rank(Vector vector){
		int len = vector.numDimensions();
		if(len != maxNumFeature){
			System.err.println("Error: the vector is different from the training data");
			throw new IllegalArgumentException();
		}
		double returnValue =0;
		for(int i=0; i< len; i++){
			returnValue += r[i] * vector.value(i);
		}
		return returnValue;
	}
	
	public void train(E e){
		ArrayList<Pair<Vector, Vector>> pairList = e.getCompareVectors();
		Vector big =null;
		Vector small = null;
		double tempr[] = new double[maxNumFeature];
		for(int i=0, n= pairList.size(); i< n; i++)
		{
			Pair<Vector, Vector> pair = pairList.get(i);
			big = pair.first;
			small = pair.second;
			double b_score = rank(big);
			double s_socre = rank(small);
			// if b_score < s_socre, then adjust the weight 
			if(b_score < s_socre){
				System.arraycopy(r, 0, tempr, 0, maxNumFeature);
				for(int k=0; k <maxNumFeature; k++){
					tempr[k] = r[k] + d * (big.value(k) - small.value(k));
				}
				System.arraycopy(tempr, 0, r, 0, maxNumFeature);
				normalize(r);
			}
		}
	}
	
	private void normalize(double r[]){
		double sum = 0;
		for(int i=0; i < r.length; i++){
			sum += r[i]*r[i];
		}
		sum = java.lang.Math.sqrt(sum);
		for(int i=0; i < r.length; i++){
			r[i] = r[i]/sum;
		}
	}

	public void train(E[] e, int num){
		for(int i=0;i < num; i++){
			for(int j=0; j < e.length; j++){
				train(e[j]);
			}
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}

}
