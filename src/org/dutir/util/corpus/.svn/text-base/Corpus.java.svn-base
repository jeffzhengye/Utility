/*
 * LingPipe v. 3.5
 * Copyright (C) 2003-2008 Alias-i
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

package org.dutir.util.corpus;

import java.io.IOException;

import org.apache.lucene.index.IndexWriter;
import org.dutir.lucene.parser.DocumentParser;

/**
 * The <code>Corpus</code> abstract class provides a basis for passing
 * training and testing data to data handlers.  The methods walk
 * handlers over the training and/or test data, depending on which of
 * the methods is called.
 *
 * @author Bob Carpenter
 * @version 3.0
 * @since LingPipe2.3
 */
public class Corpus<P extends DocumentParser> {

	IndexWriter writer = null;
    /**
     * Construct a corpus.
     */
    protected Corpus() { 
        /* only for protection */
    }

    /**
     * Visit the entire corpus, sending all extracted events to the
     * specified handler.
     *
     * <p>This is just a convenience method that is defined by:
     * <blockquote><pre>
     * visitCorpus(handler,handler);
     * </pre></blockquote>
     *
     * @param handler Handler for events extracted from the corpus.
     * @throws IOException If there is an underlying I/O error.
     */
    public void visitCorpus(DocumentParser parser) 
        throws IOException {

    }

    public void setIndexWriter(IndexWriter writer){
    	this.writer = writer;
    }

}
