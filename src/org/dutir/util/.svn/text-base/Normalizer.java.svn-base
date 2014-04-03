/**
 * 
 */
package org.dutir.util;

/**
 * The class class implements various of normalization function 
 * 1. norm1
 * 2. norm2
 * @author YeZheng
 *
 */
public class Normalizer {

	/**
	 * 
	 * @param arrays
	 * @param max position of the maximum 
	 * @return
	 */
	public static double[] norm_0_1(double arrays[], int max){
		double maxV = arrays[max];
		for(int i=0; i < arrays.length; i++){
			arrays[i] = arrays[i]/maxV;
		}
		return arrays;
	}
	
	/**
	 * 
	 * @param arrays
	 * @param max position of the maximum 
	 * @return
	 */
	public static float[] norm_0_1(float arrays[], int max){
		float maxV = arrays[max];
		for(int i=0; i < arrays.length; i++){
			arrays[i] = arrays[i]/maxV;
		}
		return arrays;
	}
	/**
	 * 
	 * @param arrays is already sorted
	 * @return
	 */
	public static double[] norm_0_1(double arrays[]){
		double maxV = arrays[0];
		for(int i=1; i < arrays.length; i++){
			if(maxV < arrays[i]){
				maxV = arrays[i];
			}
		}
		
		for(int i=0; i < arrays.length; i++){
			arrays[i] = arrays[i]/maxV;
		}
		return arrays;
	}
	
	/**
	 * 
	 * @param arrays is already sorted
	 * @return
	 */
	public static float[] norm_0_1(float arrays[]){
		float maxV = arrays[0];
		for(int i=1; i < arrays.length; i++){
			if(maxV < arrays[i]){
				maxV = arrays[i];
			}
		}
		
		for(int i=0; i < arrays.length; i++){
			arrays[i] = arrays[i]/maxV;
		}
		return arrays;
	}
	
	public static double[] norm_MaxMin_0_1(double arrays[]){
		double maxV = arrays[0];
		double minV = arrays[0];
		for(int i=1; i < arrays.length; i++){
			if(maxV < arrays[i]){
				maxV = arrays[i];
			}else if(minV > arrays[i]){
				minV = arrays[i];
			}
		}
		double pV = maxV - minV;
		if (pV==0) return arrays;
		for(int i=0; i < arrays.length; i++){
			arrays[i] = (arrays[i]-minV)/pV;
		}
		return arrays;
	}
	
	public static float[] norm_MaxMin_0_1(float arrays[]){
		float maxV = arrays[0];
		float minV = arrays[0];
		for(int i=1; i < arrays.length; i++){
			if(maxV < arrays[i]){
				maxV = arrays[i];
			}else if(minV > arrays[i]){
				minV = arrays[i];
			}
		}
		float pV = maxV - minV;
		if (pV==0) return arrays;
		for(int i=0; i < arrays.length; i++){
			arrays[i] = (arrays[i]-minV)/pV;
		}
		return arrays;
	}
	
	public static double[] norm2(double arrays[]){
		double denominator =0;
		for(int i=0; i < arrays.length; i++){
//			arrays[i] = arrays[i]*arrays[i];
//			denominator += arrays[i];
			denominator += arrays[i]*arrays[i];
		}
		denominator = java.lang.Math.sqrt(denominator);
		for(int i=0; i < arrays.length; i++){
			arrays[i] = arrays[i]/denominator;
		}
		return arrays;
	}
	
	public static float[] norm2(float arrays[]){
		float denominator =0;
		for(int i=0; i < arrays.length; i++){
//			arrays[i] = arrays[i]*arrays[i];
//			denominator += arrays[i];
			denominator += arrays[i]*arrays[i];
		}
		denominator = (float)java.lang.Math.sqrt(denominator);
		for(int i=0; i < arrays.length; i++){
			arrays[i] = arrays[i]/denominator;
		}
		return arrays;
	}
	
	public static float[] normN(float arrays[], int N){
		float denominator =0;
		for(int i=0; i < arrays.length; i++){
			arrays[i] = (float)java.lang.Math.pow(arrays[i], N);
			denominator += arrays[i];
		}
//		denominator = (float)java.lang.Math.pow(denominator, 1/(double)N);
		for(int i=0; i < arrays.length; i++){
			arrays[i] = arrays[i]/denominator;
		}
		return arrays;
	}
	
	public static float[] normN(float arrays[], float fN){
		float denominator =0;
		for(int i=0; i < arrays.length; i++){
			arrays[i] = (float)java.lang.Math.pow(arrays[i], fN);
			denominator += arrays[i];
		}
//		denominator = (float)java.lang.Math.pow(denominator, 1/(double)fN);
		for(int i=0; i < arrays.length; i++){
			arrays[i] = arrays[i]/denominator;
		}
		return arrays;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
