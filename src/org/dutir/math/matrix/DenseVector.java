/*
 * LingPipe v. 3.8
 * Copyright (C) 2003-2009 Alias-i
 *
 * This program is licensed under the Alias-i Royalty Free License
 * Version 1 WITHOUT ANY WARRANTY, without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the Alias-i
 * Royalty Free License Version 1 for more details.
 *
 * You should have received a copy of the Alias-i Royalty Free License
 * Version 1 along with this program; if not, visit
 * http://alias-i.com/lingpipe/licenses/lingpipe-license-1.txt or contact
 * Alias-i, Inc. at 181 North 11th Street, Suite 401, Brooklyn, NY 11211,
 * +1 (718) 290-9170.
 */

package org.dutir.math.matrix;

import java.util.Arrays;

/**
 * A <code>DenseVector</code> is a vector implementation suitable for
 * vectors with primarily non-zero values.  The dimensioanality of
 * a dense vector is set at construction time and immutable afterwards.
 * Values may be specified at construction time or given default values.
 * Values may be set later.
 *
 * <P><i>Implementation Note:</i> A dense vector represents the values
 * with an array of primitive double values and the label with an
 * array of objects.
 *
 * @author Bob Carpenter
 * @version 3.5.1
 * @since   LingPipe2.0
 */
public class DenseVector extends AbstractVector {

    static final boolean IGNORE = true; // ignore this value

    private final double[] mValues;

    /**
     * Construct a dense vector with the specified number of
     * dimensions.  All values will be set to <code>0.0</code>
     * initially.
     *
     * @param numDimensions The number of dimensions in this vector.
     * @throws IllegalArgumentException If the number of dimensions is
     * not positive.
     */
    public DenseVector(int numDimensions) {
        this(zeroValues(numDimensions),IGNORE);
        if (numDimensions < 1) {
            String msg = "Require positive number of dimensions."
                + " Found numDimensions=" + numDimensions;
            throw new IllegalArgumentException(msg);
        }
    }

    /**
     * Construct a dense vector with the specified values.  The
     * number of dimensions will be equal to the length of the
     * specified array of values.  The specified values are copied, so
     * subsequent changes to the specified values are not reflected
     * in this class.
     *
     * @param values Array of values for the vector.
     * @throws IllegalArgumentException If the specified values array
     * is zero length.
     */
    public DenseVector(double[] values) {
        this(copyValues(values),IGNORE);
        if (values.length < 1) {
            String msg = "Vectors must have positive length."
                + " Found length=" + values.length;
            throw new IllegalArgumentException(msg);
        }
    }

    /**
     * Constructs a deep copy of the specified vector.
     *
     * @param v Vector to copy.
     */
    public DenseVector(DenseVector v) {
        this(v.mValues);  // redundant work on test no big
    }

    DenseVector(double[] values, boolean ignore) {
        mValues = values;
    }


    @Override
    public double dotProduct(Vector v) {
        if (v instanceof SparseFloatVector)
            return v.dotProduct(this); // actually do sparse product on other side
        else
            return super.dotProduct(v);
    }

    /**
     * Sets the value of the specified dimension to the specified
     * value.
     *
     * @param dimension The specified dimension.
     * @param value The new value for the specified dimension.
     * @throws IndexOutOfBoundsException If the dimension is less than
     * 0 or greather than or equal to the number of dimensions of this
     * vector.
     */
    @Override
    public void setValue(int dimension, double value) {
        mValues[dimension] = value;
    }

    /**
     * Returns the number of dimensions for this dense vector.  The
     * dimensionality is set at construction time and is immutable.
     *
     * @return The number of dimensions of this vector.
     */
    @Override
    public int numDimensions() {
        return mValues.length;
    }

    /**
     * Adds the specified vector multiplied by the specified scalar to
     * this vector.
     *
     * <p><i>Implementation Note:</i> This class implements this
     * operationg sparsely if the specified argument vector is
     * an instance of {@link SparseFloatVector}.  Otherwise, it
     * visits every dimension of the specified vector and this
     * vector.
     *
     * @param scale The scalar multiplier for the added vector.
     * @param v Vector to scale and add to this vector.
     * @throws IllegalArgumentException If the specified vector is not
     * of the same dimensionality as this vector.
     */
    @Override
    public void increment(double scale, Vector v) {
        if (v instanceof SparseFloatVector) {
            int[] nonZeroDims = v.nonZeroDimensions();
            for (int i = 0; i < nonZeroDims.length; ++i) {
                int dim = nonZeroDims[i];
                mValues[dim] += scale * v.value(dim);
            }
        } else {
            for (int i = 0; i < mValues.length; ++i)
                mValues[i] += scale * v.value(i);
        }

    }

    @Override
    public Vector add(Vector v) {
        return Matrices.add(this,v);
    }

    /**
     * Returns the value of this dense vector for the specified
     * dimension.
     *
     * @param dimension Specified dimension.
     * @return The value of this vector for the specified dimension.
     * @throws IndexOutOfBoundsException If the dimension is less than
     * 0 or greather than or equal to the number of dimensions of this
     * vector.
     */
    @Override
    public double value(int dimension) {
        return mValues[dimension];
    }

    private static double[] zeroValues(int n) {
        double[] xs = new double[n];
        Arrays.fill(xs,0.0);
        return xs;
    }

    private static double[] copyValues(double[] values) {
        double[] xs = new double[values.length];
        System.arraycopy(values,0,xs,0,xs.length);
        return xs;
    }

    /**
     * Returns a string representation of the values
     * in this vector.
     *
     * @return A string representation of this vector.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mValues.length; ++i) {
            sb.append(" " + i + "=" + mValues[i]);
        }
        return sb.toString();
    }
}

