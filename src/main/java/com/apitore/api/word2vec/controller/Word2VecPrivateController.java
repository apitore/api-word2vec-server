package com.apitore.api.word2vec.controller;


import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.apitore.api.word2vec.service.Word2VecService;
import com.apitore.banana.request.word2vec.WordsRequestEntity;

import springfox.documentation.annotations.ApiIgnore;


/**
 * @author Keigo Hattori
 */
@RestController
@RequestMapping(value = "/word2vec-neologd-jawiki")
public class Word2VecPrivateController {

  @Autowired
  Word2VecService word2VecService;

  /**
   * hasword
   *
   * @param word
   * @return
   */
  @RequestMapping(value="/open/hasword", method=RequestMethod.GET)
  @ApiIgnore
  public ResponseEntity<Boolean> hasWord(
      @RequestParam(name="word", required=true)
      String word
      ) {

    word = word.toLowerCase();
    Boolean rtn = word2VecService.hasWord(word);
    return new ResponseEntity<Boolean>(rtn,HttpStatus.OK);
  }

  /**
   * layerSize
   *
   * @return
   */
  @RequestMapping(value="/open/layersize", method=RequestMethod.GET)
  @ApiIgnore
  public ResponseEntity<Integer> layerSize(
      ) {

    Integer rtn = word2VecService.layerSize();
    return new ResponseEntity<Integer>(rtn,HttpStatus.OK);
  }

  /**
   * getWordVectorMatrix
   *
   * @param word
   * @return
   */
  @RequestMapping(value="/open/wordvectormatrix", method=RequestMethod.GET)
  @ApiIgnore
  public ResponseEntity<double[]> getWordVectorMatrix(
      @RequestParam(name="word", required=true)
      String word
      ) {

    word = word.toLowerCase();
    double[] rtn = word2VecService.getWordVectorMatrixNormalized(word);
    return new ResponseEntity<double[]>(rtn,HttpStatus.OK);
  }

  /**
   * getWordVectorMatrix
   *
   * @param word
   * @return
   */
  @RequestMapping(value="/open/wordvectormatrix", method=RequestMethod.POST)
  @ApiIgnore
  public ResponseEntity<Map<String,double[]>> getWordVectorMatrix(
      @RequestBody
      WordsRequestEntity req
      ) {
    Map<String,double[]> rtn = new HashMap<String,double[]>();
    for (String word: req.getWords()) {
      word = word.toLowerCase();
      if (word2VecService.hasWord(word)) {
        double[] vec = word2VecService.getWordVectorMatrixNormalized(word);
        rtn.put(word, vec);
      }
    }
    return new ResponseEntity<Map<String,double[]>>(rtn,HttpStatus.OK);
  }

}
