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

package org.dutir.util.dict;


import gnu.trove.TIntIntHashMap;
import gnu.trove.TIntObjectHashMap;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;

import org.dutir.util.AbstractExternalizable;
import org.dutir.util.Compilable;
import org.dutir.util.Iterators;

/**
 * A <code>TrieDictionary</code> stores a dictionary using a character
 * trie structure.  This requires a constant amount of space for each
 * entry and each prefix of an entry's string.  Lookups take an amount
 * of time proportional to the length of the string being looked up,
 * with each character requiring a lookup in a map.  The lookup is
 * done with binary search in this implementation in time proportional
 * to the log of the number of characters, for a total lookup time
 * of <code><b>O</b>(n log c)</code> where <code>n</code> is the
 * number of characters in the string being looked up and <code>c</code>
 * is the number of charactes.
 *
 * <P>Tries are a popular data structure; see the <a
 * href="http://en.wikipedia.org/wiki/Trie">Wikipedia Trie</a> topic for
 * examples and references.  Tries are also used in the language model
 * classes {@link com.aliasi.lm.TrieCharSeqCounter} and {@link
 * com.aliasi.lm.TrieIntSeqCounter} and the compiled forms of all of
 * the language models.
 *
 * <h4>Compilation and Serialization</h4>
 *
 * The trie dictionary implements both the Java {@link Serializable}
 * and LingPipe {@link Compilable} interfaces to write the contents
 * of a trie dictionary to an object output.  Both approaches produce
 * the same result and the dictionary read back in will be an instance
 * of <code>TrieDictionary</code> and equivalent to the dictionary that
 * was serialized or compiled.
 *
 * @author Bob Carpenter
 * @version 4.0
 * @since   LingPipe2.1
 * @param <C> the type of object stored in this dictionary
 */
public class FHashTrieDictionary<C>
    extends AbstractDictionary<C>
    implements Serializable, Compilable{

    static final long serialVersionUID = -6772406715071883449L;
    TIntObjectHashMap<Node<C>> firstHZmap = new TIntObjectHashMap<Node<C>>();
    
    Node<C> mRootNode = new Node<C>();

    /**
     * Construct a trie-based dictionary.
     */
    public FHashTrieDictionary() {
        /* do ntohing */
    	
    }

    
    public DictionaryEntry<C>[] phraseEntries(String phrase) {
    	return phraseEntries(phrase.toCharArray());
    }

    public DictionaryEntry<C>[] phraseEntries(char phrases[]) {
        Node<C> node = firstHZmap.get(phrases[0]);
        if(node == null){
        	return Node.<C>emptyEntries();
        }
        
        for (int i = 1; i < phrases.length; ++i) {
            node = node.getDtr(phrases[i]);
            if (node == null) return Node.<C>emptyEntries();
        }
        return node.mEntries;
    }
    
    public DictionaryEntry<C> firstPhraseEntries(char phrases[]) {
        
        DictionaryEntry<C>[] entries = phraseEntries(phrases);
        return entries.length > 0 ? entries[0] : null;
    }

    /**
     * Equal entries will be ignored.
     */
    public void addEntry(DictionaryEntry<C> entry) {
        String phrase = entry.phrase();
        boolean has = firstHZmap.contains(phrase.charAt(0));
        Node<C> node = null;
        if(has){
        	node = firstHZmap.get(phrase.charAt(0));
        }else{
        	node = new Node<C>();
        	firstHZmap.put(phrase.charAt(0), node);
        }
        for (int i = 1; i < phrase.length(); ++i)
            node = node.getOrAddDtr(phrase.charAt(i));
        node.addEntry(entry);
    }

	public int getFrequency(char[] charArray) {
		DictionaryEntry<C> entry = firstPhraseEntries(charArray);
		if(entry != null) return entry.count();
		return 0;
	}
	
	/**
	 * 
	 * @param charArray
	 * @return
	 */
	public Node<C> getPrefixMatch(char[] charArray) {
        Node<C> node = firstHZmap.get(charArray[0]);
        if(node == null){
        	return null;
        }
        for (int i = 1; i < charArray.length; ++i) {
            node = node.getDtr(charArray[i]);
            if (node == null) return null;
        }
        return node;
	}
	
	
	public Node<C> getPrefixMatch(char achar, Node<C> foundNode) {
		Node<C> node = foundNode.getDtr(achar);
        if (node == null) return null;
		return node;
	}
	
	public Node<C> getPrefixMatch(char[] charArray, Node<C> foundNode) {
        Node<C> node = foundNode;
        if(node == null){
        	return null;
        }
        for (int i = 0; i < charArray.length; ++i) {
            node = node.getDtr(charArray[i]);
            if (node == null) return null;
        }
        return node;
	}
	
    @Override
    public Iterator<DictionaryEntry<C>> phraseEntryIt(String phrase) {
        return Iterators.<DictionaryEntry<C>>array(phraseEntries(phrase));
    }
    
    public String getStatistics(){
    	int numOfFirst = this.firstHZmap.size();
    	int numOfPhrases = this.size();
    	return "numOfFirstCharater: " + numOfFirst + ", numOfPhrases: " + numOfPhrases;
    }
    /**
     * Returns an iterator over all of the dictionary entries
     * for this dictioniary.  This is the implementation of the iterator
     * view of this dictionary as a collection (set of entries).
     *
     * @return An iterator over all of the dictionary entries for this
     * dictioniary.
     */

    private Object writeReplace() {
        return new Externalizer<C>(this);
    }

    /**
     * Compile the entries in this dictionary to the specified object output.
     *
     * @param out Object output to which to write the dictionary.
     * @throws IOException If there is an underlying I/O error during
     * the write.
     */
    public void compileTo(ObjectOutput out) throws IOException {
        out.writeObject(new Externalizer<C>(this));
    }

    private static class Externalizer<F> extends AbstractExternalizable {
        static final long serialVersionUID = -6351978792499636468L;
        private final FHashTrieDictionary<F> mDictionary;
        public Externalizer(FHashTrieDictionary<F> fHashTrieDictionary) {
            mDictionary = fHashTrieDictionary;
        }
        public Externalizer() {
            this(null);
        }
        
        public Object read(ObjectInput in) throws IOException, ClassNotFoundException {
        	FHashTrieDictionary<F> dict = new FHashTrieDictionary<F>();
            int numEntries = in.readInt();
            for (int i = 0; i < numEntries; ++i) {
                // required for readObject; safe with good serialization
                @SuppressWarnings("unchecked")
                DictionaryEntry<F> entry = (DictionaryEntry<F>) in.readObject();
                dict.addEntry(entry);
            }
            return dict;
        }
        @Override
        public void writeExternal(ObjectOutput out) throws IOException {
            int count = mDictionary.size();
            out.writeInt(count);
            for (DictionaryEntry<F> entry : mDictionary)
                entry.compileTo(out);
        }
    }
    

    private static class FTrieIterator<D>
        extends Iterators.Buffered<DictionaryEntry<D>> {
        LinkedList<Node<D>> mQueue = new LinkedList<Node<D>>();
        DictionaryEntry<D>[] mEntries;
        int mNextEntry = -1;
        FTrieIterator(TIntObjectHashMap<Node<D>> firstHZmap) {
            Node<D>[] nodes = new Node[0];
            nodes =  firstHZmap.getValues(nodes);
            for(int i=0; i < nodes.length; i++){
            	 mQueue.add(nodes[i]);
            }
           
        }
        @Override
        protected DictionaryEntry<D> bufferNext() {
            while (mEntries == null && !mQueue.isEmpty()) {
                Node<D> node = mQueue.removeFirst();
                addDtrs(node.mDtrNodes);
                if (node.mEntries.length > 0) {
                    mEntries = node.mEntries;
                    mNextEntry = 0;
                }
            }
            if (mEntries == null) return null;
            DictionaryEntry<D> result = mEntries[mNextEntry++];
            if (mNextEntry >= mEntries.length) mEntries = null;
            return result;
        }
        void addDtrs(Node<D>[] dtrs) {
            for (int i = dtrs.length; --i >= 0; ) {
                if (dtrs[i] == null) System.out.println("ADDING=" + i);
                mQueue.addFirst(dtrs[i]);
            }
        }
    }

    
	@Override
	public Iterator<DictionaryEntry<C>> iterator() {
		return new FTrieIterator<C>(this.firstHZmap);
	}


	public static void main(String args[]){
		FHashTrieDictionary<String> fhtd = new FHashTrieDictionary<String>();
		fhtd.addEntry(new DictionaryEntry<String>("yezheng", "en"));
		fhtd.addEntry(new DictionaryEntry<String>("叶", "zh"));
		fhtd.addEntry(new DictionaryEntry<String>("叶", "zhwen"));
		DictionaryEntry<String> enties[] = fhtd.phraseEntries("叶");
		Node<String> node = fhtd.getPrefixMatch("yezh".toCharArray());
		System.out.println(node);
		System.out.println(node.getEntries().length);
//		System.out.println(enties.length);
	}

	
}
