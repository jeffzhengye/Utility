package org.dutir.math.function;

public class SigmoidFunction {
	public static double sigmoidE(double x){
		return 1d/(1+Math.pow(Math.E, -x));
	}
	
	public static double sigmoid(double factor, double power){
		return 1d/(1+Math.pow(factor, power));
	}
	
	public static double inverseSigmoid(double factor, double power){
		return 1d+1d/Math.pow(factor, power);
	}
}