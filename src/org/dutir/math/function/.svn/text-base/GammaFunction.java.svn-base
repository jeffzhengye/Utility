package org.dutir.math.function;

/**
 * Base class for implementations of the Gamma function. Use getGammaFunction()
 * to obtain an instance. The exact instance can be controlled by property
 * <tt>gamma.function</tt>
 * <p>
 * <b>Properties:</b>
 * </p>
 * <ul>
 * <li><tt>gamma.function</tt> - class name of the Gamma function
 * implementation. Defaults to use WikipediaLanczosGammaFunction
 * 
 * @since 3.0
 * @author Craig Macdonald
 */
public class GammaFunction {

	private static final double p0 = 1.000000000190015d;
	private static final double p1 = 76.18009172947146d;
	private static final double p2 = -86.50532032941677d;
	private static final double p3 = 24.01409824083091d;
	private static final double p4 = -1.231739572450155d;
	private static final double p5 = 1.208650973866179e-3d;
	private static final double p6 = -5.395239384953e-6d;

	public double compute(double x) {
		double result = (Math.sqrt(2 * Math.PI) / x)
				* (p0 + p1 / (x + 1) + p2 / (x + 2) + p3 / (x + 3) + p4
						/ (x + 4) + p5 / (x + 5) + p6 / (x + 6))
				* (Math.pow(x + 5.5, x + 0.5)) * (Math.pow(Math.E, -x - 5.5));
		if (Double.isNaN(result))
			System.out.println("found NaN");

		if (Double.isInfinite(result))
			System.out.println("found Infinite");

		return (result);
	}

	public double compute_log(double x) {
		double result = Math.log((Math.sqrt(2 * Math.PI) / x))
				+ Math.log(p0 + p1 / (x + 1) + p2 / (x + 2) + p3 / (x + 3) + p4
						/ (x + 4) + p5 / (x + 5) + p6 / (x + 6)) + (x + 0.5)
				* Math.log(x + 5.5) + (-x - 5.5) * Math.log(Math.E);
		if (Double.isNaN(result))
			System.out.println("found NaN:" + x);

		if (Double.isInfinite(result))
			System.out.println("found Infinite:" + x);

		return (result);
	}

	
	
	protected static final double REC_LOG_2 = 1.0d / Math.log(2.0d);
	protected static final GammaFunction gf = new GammaFunction();
	
	public static float score(int freq, int docLength) {

		float rscore = 1; // weightValue equals to the boost

		float matchingNGrams = freq;

		final int numberOfNGrams = 80;

		double score = 0.0d;

		// apply Norm2 to pf?
		double matchingNGramsNormalised = matchingNGrams;

		double background = numberOfNGrams; 

		double p = 1.0D / background;
		double q = 1.0d - p;
		score = -gf.compute_log(background + 1.0d) * REC_LOG_2
				+ gf.compute_log(matchingNGramsNormalised + 1.0d) * REC_LOG_2
				+ gf.compute_log(background - matchingNGramsNormalised + 1.0d)	* REC_LOG_2 
				- matchingNGramsNormalised * Math.log(p)* REC_LOG_2 
				- (background - matchingNGramsNormalised)* Math.log(q) * REC_LOG_2;
		score = score / (1.0d + matchingNGramsNormalised);
		
		return (float) score;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GammaFunction gFunction = new GammaFunction();
//		for (int i = 1; i < 100; i++) {
//			System.out
//					.println("gFunction(" + i + ") = " + gFunction.compute(i));
//		}
//
		for (int i = 1; i < 100; i++) {
			System.out.println(i + "! = " + gFunction.compute(i + 1));
		}

		System.out.println("3.5! = " + gFunction.compute(3));
		
		///////////////////////////////////////////////////////
		
//		for (int i = 1; i < 10; i++) {
//			System.out.println(i + "! = " + score(i + 1, 200));
//		}
		
	}

}