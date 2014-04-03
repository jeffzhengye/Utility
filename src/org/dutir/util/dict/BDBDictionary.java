/**
 * 
 */
package org.dutir.util.dict;

import java.util.Iterator;


/**
 * use bekeley DB to maintain a big dictionary in a file, which can not be kept in memory
 * @author Yezheng
 *
 */
public class BDBDictionary<C> extends AbstractDictionary<C>{

	
	private BDBDictionary(){
		
	}
	
	public BDBDictionary<C> openOrCreate(String path){
		return null;
	}
	
	public BDBDictionary<C> open_readonly(String path){
		return null;
	}
	
    public DictionaryEntry<C>[] entries() {
    	System.out.println("It's highly recommentted not to use this method, as this dictionary may could not be loaded in memory");
        return null;
    }
    
	public void close(){
		
	}
	
	/* (non-Javadoc)
	 * @see java.util.AbstractCollection#iterator()
	 */
	@Override
	public Iterator iterator() {
		// TODO Auto-generated method stub
		
		return null;
	}
	
    public void addEntry(DictionaryEntry<C> entry) {
        
    }

    public Iterator<DictionaryEntry<C>> phraseEntryIt(String phrase) {
        return null;
    }
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
