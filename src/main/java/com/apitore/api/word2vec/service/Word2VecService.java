package com.apitore.api.word2vec.service;


import java.util.ArrayList;
import java.util.List;

import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


/**
 * @author Keigo Hattori
 */
@Service
public class Word2VecService {

  @Autowired
  @Qualifier("wordVector")
  WordVectors vec;


  public List<String> wordsNearest(String word, int num) {
    return new ArrayList<String>(vec.wordsNearest(word, num));
  }

  public List<String> wordsNearest(INDArray word, int num) {
    return new ArrayList<String>(vec.wordsNearest(word, num));
  }

  public List<String> wordsNearest(List<String> positives, List<String> negatives, int num) {
    return new ArrayList<String>(vec.wordsNearest(positives, negatives, num));
  }

  public double[] getWordVectorMatrixNormalized(String word) {
    return vec.getWordVectorMatrixNormalized(word).dup().data().asDouble();
  }

  public double similarity(String word1, String word2) {
    return vec.similarity(word1, word2);
  }

  public boolean hasWord(String word) {
    return vec.hasWord(word);
  }

  public int layerSize() {
    return vec.lookupTable().layerSize();
  }

}
