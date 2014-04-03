/**
 * Copyright 2005 The Apache Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dutir.util;

import java.io.IOException;
import java.io.DataInput;
import java.io.DataOutput;
import java.util.Arrays;
import java.security.*;

/** A Writable for MD5 hash values.
 *
 * @author Doug Cutting
 */
public class Digester {
	public static final int MD5_LEN = 16;
	private static final MessageDigest DIGESTER;
	static {
		try {
			DIGESTER = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	private byte[] digest;

	/** Constructs an MD5Hash. */
	//  public MD5Hash() {
	//    this.digest = new byte[MD5_LEN];
	//  }
	//
	//  /** Constructs an MD5Hash from a hex string. */
	//  public MD5Hash(String hex) {
	//    setDigest(hex);
	//  }
	/** Constructs an MD5Hash with a specified value. */
	//  public MD5Hash(byte[] digest) {
	//    if (digest.length != MD5_LEN)
	//      throw new IllegalArgumentException("Wrong length: " + digest.length);
	//    this.digest = digest;
	//  }

	/** Returns the digest bytes. */
	public byte[] getDigest() {
		return digest;
	}

	public static String digest(String text) {
		return new String(digest(text.getBytes()));
	}

	public static String digestBytes(byte bytes[]) {
		return new String(digest(bytes));
	}
	
	public static byte[] digest(byte bytes[]) {
		return DIGESTER.digest(bytes);
	}

	/** Construct a half-sized version of this MD5.  Fits in a long **/
	public long halfDigest() {
		long value = 0;
		for (int i = 0; i < 8; i++)
			value |= ((digest[i] & 0xffL) << (8 * (7 - i)));
		return value;
	}

	/** Returns a hash code value for this object.*/
	public int hashCode() {
		return // xor four ints
		(digest[0] | (digest[1] << 8) | (digest[2] << 16) | (digest[3] << 24))
				^ (digest[4] | (digest[5] << 8) | (digest[6] << 16) | (digest[7] << 24))
				^ (digest[8] | (digest[9] << 8) | (digest[10] << 16) | (digest[11] << 24))
				^ (digest[12] | (digest[13] << 8) | (digest[14] << 16) | (digest[15] << 24));
	}

	private static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	/** Returns a string representation of this object. */
	public String toString() {
		StringBuffer buf = new StringBuffer(MD5_LEN * 2);
		for (int i = 0; i < MD5_LEN; i++) {
			int b = digest[i];
			buf.append(HEX_DIGITS[(b >> 4) & 0xf]);
			buf.append(HEX_DIGITS[b & 0xf]);
		}
		return buf.toString();
	}

	/** Sets the digest value from a hex string. */
	public void setDigest(String hex) {
		if (hex.length() != MD5_LEN * 2)
			throw new IllegalArgumentException("Wrong length: " + hex.length());
		byte[] digest = new byte[MD5_LEN];
		for (int i = 0; i < MD5_LEN; i++) {
			int j = i << 1;
			digest[i] = (byte) (charToNibble(hex.charAt(j)) << 4 | charToNibble(hex
					.charAt(j + 1)));
		}
		this.digest = digest;
	}

	private static final int charToNibble(char c) {
		if (c >= '0' && c <= '9') {
			return c - '0';
		} else if (c >= 'a' && c <= 'f') {
			return 0xa + (c - 'a');
		} else if (c >= 'A' && c <= 'F') {
			return 0xA + (c - 'A');
		} else {
			throw new RuntimeException("Not a hex character: " + c);
		}
	}

}
