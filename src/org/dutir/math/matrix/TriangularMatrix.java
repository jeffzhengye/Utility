/**
 * 
 */
package org.dutir.math.matrix;

import java.util.Arrays;

/**
 * TriangularMatrix has n*n matrix. In this implementation, 
 * we only store lower triangular part in order to save space. 
 * @author zheng
 * 
 */

public class TriangularMatrix extends AbstractMatrix {

	private final double[] mValues;
	private final int mDimension;
	private static final boolean IGNORE = true;

	public TriangularMatrix(int dimension) {
		mDimension = dimension;
		mValues = zeroValues(dimension);
	}

	private static double[] zeroValues(int dimension) {
		double[] result = new double[dimension * (dimension +1)/2];
			Arrays.fill(result, 0.0);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dutir.math.matrix.AbstractMatrix#numColumns()
	 */
	@Override
	public int numColumns() {
		return mDimension;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dutir.math.matrix.AbstractMatrix#numRows()
	 */
	@Override
	public int numRows() {
		return mDimension;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.dutir.math.matrix.AbstractMatrix#value(int, int)
	 */
	@Override
	public double value(int row, int column) {
		if(row < column ){
			int tmp = row;
			row = column; 
			column = tmp;
		}
		return mValues[row * (row +1)/2 + column];
	}

    public void setValue(int row, int column, double value) {
		if(row < column ){
			int tmp = row;
			row = column; 
			column = tmp;
		}
    	mValues[row * (row +1)/2 + column] = value;
    }
	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

}
