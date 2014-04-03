package org.dutir.parser;

import org.dutir.document.CNKIItem;
import org.dutir.document.mesh.Des;

import com.aliasi.corpus.Handler;

public interface MeSHHandle extends Handler{
	public  void handle(Des des);
}
