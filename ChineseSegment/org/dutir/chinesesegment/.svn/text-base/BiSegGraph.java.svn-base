/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dutir.chinesesegment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aliasi.lm.CompiledNGramProcessLM;

/**
 * Graph representing possible token pairs at each start offset in the sentence.
 * <p>
 * For each start offset, a list of possible token pairs is stored.
 * </p>
 * <p><font color="#FF0000">
 * WARNING: The status of the analyzers/smartcn <b>analysis.cn.smart</b> package is experimental. 
 * The APIs and file formats introduced here might change in the future and will not be 
 * supported anymore in such a case.</font>
 * </p>
 */
public class BiSegGraph {

	/**
	 * 
	 */
  private Map<Integer,ArrayList<SegTokenPair>> tokenPairListTable = new HashMap<Integer,ArrayList<SegTokenPair>>();

  private List<SegToken> segTokenList;

//  private static BigramDictionary bigramDict = BigramDictionary.getInstance();
//  private static NGramProcessLM lm =DictionaryManager.getNGramProcessLM();
  static CompiledNGramProcessLM clm = DictionaryManager.getCNGramProcessLM();
  
  public BiSegGraph(SegGraph segGraph) {
    segTokenList = segGraph.makeIndex();
    generateBiSegGraph(segGraph);
  }

  /*
   * Generate a BiSegGraph based upon a SegGraph
   */
  private void generateBiSegGraph(SegGraph segGraph) {
    double smooth = 0.1;
    double wordPairFreq = 0;
    int maxStart = segGraph.getMaxStart();
    double oneWordFreq, weight =0, tinyDouble = 1.0 / Utility.MAX_FREQUENCE;
    
    int next;
    char[] idBuffer;
    // get the list of tokens ordered and indexed
    segTokenList = segGraph.makeIndex();
    // Because the beginning position of startToken is -1, therefore startToken can be obtained when key = -1
    int key = -1;
    List<SegToken> nextTokens = null;
    while (key < maxStart) {
      if (segGraph.isStartExist(key)) {

        List<SegToken> tokenList = segGraph.getStartList(key);

        // Calculate all tokens for a given key.
        for (SegToken t1 : tokenList) {
          oneWordFreq = t1.weight;
          next = t1.endOffset;
          nextTokens = null;
          // Find the next corresponding Token.
          // For example: "Sunny seashore", the present Token is "sunny", next one should be "sea" or "seashore".
          // If we cannot find the next Token, then go to the end and repeat the same cycle.
          while (next <= maxStart) {
            // Because the beginning position of endToken is sentenceLen, so equal to sentenceLen can find endToken.
            if (segGraph.isStartExist(next)) {
              nextTokens = segGraph.getStartList(next);
              break;
            }
            next++;
          }
          if (nextTokens == null) {
            break;
          }
          for (SegToken t2 : nextTokens) {
        	  
        	  if(t1.wordType == WordType.CHINESE_WORD && t2.wordType == WordType.CHINESE_WORD){
        		  idBuffer = new char[t2.charArray.length + 1];
                  idBuffer[0] = ' ';

                  System.arraycopy(t2.charArray, 0, idBuffer,
                      1, t2.charArray.length);
            

                  SegTokenPair tokenPair = new SegTokenPair(idBuffer, t1.index,
                      t2.index, weight);
                  this.addSegTokenPair(tokenPair);
        	  }else{
        		  idBuffer = new char[t2.charArray.length];

                  System.arraycopy(t2.charArray, 0, idBuffer,
                      0, t2.charArray.length);
            

                  SegTokenPair tokenPair = new SegTokenPair(idBuffer, t1.index,
                      t2.index, weight);
                  this.addSegTokenPair(tokenPair);
        	  }
        	
          }
        }
      }
      key++;
    }

  }

  /**
   * Returns true if their is a list of token pairs at this offset (index of the second token)
   * 
   * @param to index of the second token in the token pair
   * @return true if a token pair exists
   */
  public boolean isToExist(int to) {
    return tokenPairListTable.get(Integer.valueOf(to)) != null;
  }

  /**
   * Return a {@link List} of all token pairs at this offset (index of the second token)
   * 
   * @param to index of the second token in the token pair
   * @return {@link List} of token pairs.
   */
  public List<SegTokenPair> getToList(int to) {
    return tokenPairListTable.get(to);
  }

  /**
   * Add a {@link SegTokenPair}
   * 
   * @param tokenPair {@link SegTokenPair}
   */
  public void addSegTokenPair(SegTokenPair tokenPair) {
    int to = tokenPair.to;
    if (!isToExist(to)) {
      ArrayList<SegTokenPair> newlist = new ArrayList<SegTokenPair>();
      newlist.add(tokenPair);
      tokenPairListTable.put(to, newlist);
    } else {
      List<SegTokenPair> tokenPairList = tokenPairListTable.get(to);
      tokenPairList.add(tokenPair);
    }
  }

  /**
   * Get the number of {@link SegTokenPair} entries in the table.
   * @return number of {@link SegTokenPair} entries
   */
  public int getToCount() {
    return tokenPairListTable.size();
  }

  /**
   * Find the shortest path with the Viterbi algorithm.
   * @return {@link List}
   */
  public List<SegToken> getShortPath() {
    int current;
    int nodeCount = getToCount();
    List<PathNode> path = new ArrayList<PathNode>();
    PathNode zeroPath = new PathNode();
    zeroPath.weight = 0;
    zeroPath.preNode = 0;
    zeroPath.lmIndex =0;
    path.add(zeroPath);
    
    for (current = 1; current <= nodeCount; current++) {
      double weight;
      List<SegTokenPair> edges = getToList(current);

      double minWeight = Double.MAX_VALUE;
      SegTokenPair minEdge = null;
      
      int next =0;
      for (SegTokenPair edge : edges) {
//        weight = edge.weight;
        PathNode preNode = path.get(edge.from);
        weight =0; 
        int nextIndex = preNode.lmIndex;
        /////////////////////////////////////////////////////////////
        for(int i=0; i < edge.charArray.length; i++){
        	double ts = - clm.log2Estimate(nextIndex, edge.charArray[i]);
        	nextIndex = clm.nextContext(preNode.lmIndex, edge.charArray[i]);
            weight += ts;
        }
        
        /////////////////////////////////////////////////////////////
        if (preNode.weight + weight < minWeight) {
          minWeight = preNode.weight + weight;
          minEdge = edge;
          next = nextIndex;
        }
      }
      PathNode newNode = new PathNode();
      newNode.weight = minWeight;
      newNode.preNode = minEdge.from;
      newNode.lmIndex = next;
      
      path.add(newNode);
    }

    // Calculate PathNodes
    int preNode, lastNode;
    lastNode = path.size() - 1;
    current = lastNode;
    List<Integer> rpath = new ArrayList<Integer>();
    List<SegToken> resultPath = new ArrayList<SegToken>();

    rpath.add(current);
    while (current != 0) {
      PathNode currentPathNode = path.get(current);
      preNode = currentPathNode.preNode;
      rpath.add(Integer.valueOf(preNode));
      current = preNode;
    }
    for (int j = rpath.size() - 1; j >= 0; j--) {
      Integer idInteger = (Integer) rpath.get(j);
      int id = idInteger.intValue();
      SegToken t = segTokenList.get(id);
      resultPath.add(t);
    }
    return resultPath;

  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    Collection<ArrayList<SegTokenPair>>  values = tokenPairListTable.values();
    for (ArrayList<SegTokenPair> segList : values) {
      for (SegTokenPair pair : segList) {
        sb.append(pair + "\n");
      }
    }
    return sb.toString();
  }

}
