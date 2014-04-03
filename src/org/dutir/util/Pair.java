package org.dutir.util;

import java.io.IOException;
import java.io.Serializable;

public class Pair<F , S> implements Serializable {

	private static final long serialVersionUID = 1L;

	public F first = null;

	public S second = null;

	public Pair() {
		
	}

	public Pair(F _first, S _second) {
		first = _first;
		second = _second;
	}

	public boolean equals(Pair<F, S> p) {
		if ( p.first.equals(this.first) && p.second.equals(this.second) )
			return true;
		else
			return false;
	}
	
	
	public String toString()
	{
		return first.toString() + ":" + second.toString();
	}
	
	
	public static void main(String args[]) throws IOException
	{
		String a = "a";
		String b = "a";
		String c = "a";
		String d = "a";
		Pair<String, String> pa = new Pair<String, String>(a,b);
		Pair<String, String> pb = new Pair<String, String>(c,d);
		if(pa.equals(pb))
		{
			System.out.println("equals");
		}else{
			System.out.println("not equals");
		}
		System.in.read();
	}
}
