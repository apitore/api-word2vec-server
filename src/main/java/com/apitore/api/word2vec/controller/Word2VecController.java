package com.apitore.api.word2vec.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.ops.transforms.Transforms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.apitore.api.word2vec.service.Word2VecService;
import com.apitore.banana.response.word2vec.AnalogyResponseEntity;
import com.apitore.banana.response.word2vec.VectorDistanceResponseEntity;
import com.apitore.banana.response.word2vec.DistanceEntity;
import com.apitore.banana.response.word2vec.DistanceResponseEntity;
import com.apitore.banana.response.word2vec.SimilarityResponseEntity;
import com.apitore.banana.response.word2vec.WordVectorResponseEntity;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;


/**
 * @author Keigo Hattori
 */
@RestController
@RequestMapping(value = "/word2vec-neologd-jawiki")
public class Word2VecController {

  private final Logger LOG = Logger.getLogger(Word2VecController.class);

  @Autowired
  Word2VecService word2VecService;

  final String NOTES_BASE = "Word2Vec JaWikipedia 2016-9-15 dump.<BR />"
      + "Response<BR />"
      + "&nbsp; Github: <a href=\"https://github.com/keigohtr/apitore-response-parent/tree/master/word2vec-response\">word2vec-response</a><BR />";

  final String NOTES_DISTANCE = NOTES_BASE
      + "&nbsp; Class: com.apitore.banana.response.word2vec.DistanceResponseEntity<BR />";

  final String NOTES_ANALOGY = NOTES_BASE
      + "&nbsp; Class: com.apitore.banana.response.word2vec.AnalogyResponseEntity<BR />";

  final String NOTES_SIMILARITY = NOTES_BASE
      + "&nbsp; Class: com.apitore.banana.response.word2vec.SimilarityResponseEntity<BR />";

  final String NOTES_WORDVECTOR = NOTES_BASE
      + "&nbsp; Class: com.apitore.banana.response.word2vec.WordVectorResponseEntity<BR />";

  /**
   * 実態
   *
   * @param word
   * @param num
   * @return
   */
  @RequestMapping(value="/open/distance", method=RequestMethod.GET)
  @ApiIgnore
  public ResponseEntity<DistanceResponseEntity> distance(
      @RequestParam("word") String word,
      @RequestParam(name="num", required=false, defaultValue="1")
      int num
      ) {

    DistanceResponseEntity model = new DistanceResponseEntity();
    Long startTime = System.currentTimeMillis();

    if (num<0)
      num=1;
    else if (num>10)
      num=10;

    word = word.toLowerCase();
    List<DistanceEntity> distances=new ArrayList<DistanceEntity>();
    if (word2VecService.hasWord(word)) {
      List<String> words = word2VecService.wordsNearest(word, num);
      for (String str: words) {
        double cos_distance = word2VecService.similarity(word, str);
        DistanceEntity entity = new DistanceEntity();
        entity.setWord(str);
        entity.setDistance(cos_distance);
        distances.add(entity);
      }
      model.setLog("Success.");
    } else {
      LOG.error(word);
      model.setLog("No such word.");
    }
    model.setInput(word);
    model.setNum(String.valueOf(num));
    model.setDistances(distances);
    Long endTime = System.currentTimeMillis();
    Long processTime = endTime-startTime;
    model.setStartTime(startTime.toString());
    model.setEndTime(endTime.toString());
    model.setProcessTime(processTime.toString());
    return new ResponseEntity<DistanceResponseEntity>(model,HttpStatus.OK);
  }

  /**
   * 公開用API
   * Dummyメソッド
   *
   * @param access_token
   * @param word
   * @param num
   * @return
   */
  @RequestMapping(value = {"/distance"}, produces=MediaType.APPLICATION_JSON_VALUE, method=RequestMethod.GET)
  @ApiOperation(value="Word2Vec distance", notes=NOTES_DISTANCE)
  public DistanceResponseEntity distance(
      @ApiParam(value = "Access Token", required = true)
      @RequestParam("access_token")  String access_token,
      @ApiParam(value = "word", required = true)
      @RequestParam("word")       String word,
      @ApiParam(value = "num [max 10, default 1]", required=false, defaultValue="1")
      @RequestParam(name="num", required=false, defaultValue="1")
      int num)
  {
    return new DistanceResponseEntity();
  }

  /**
   * 実態
   *
   * @param vector
   * @param num
   * @return
   */
  @RequestMapping(value="/open/vec_distance", method=RequestMethod.GET)
  @ApiIgnore
  public ResponseEntity<VectorDistanceResponseEntity> vec_distance(
      @RequestParam("vector") double[] vector,
      @RequestParam(name="num", required=false, defaultValue="1")
      int num
      ) {

    VectorDistanceResponseEntity model = new VectorDistanceResponseEntity();
    Long startTime = System.currentTimeMillis();

    if (num<0)
      num=1;
    else if (num>10)
      num=10;

    List<DistanceEntity> distances=new ArrayList<DistanceEntity>();
    if (word2VecService.layerSize() == vector.length) {
      INDArray vec = Nd4j.create(vector);
      List<String> words = word2VecService.wordsNearest(vec, num);
      for (String str: words) {
        INDArray tmp = Nd4j.create(word2VecService.getWordVectorMatrixNormalized(str));
        double cos_distance = Transforms.cosineSim(vec, tmp);
        DistanceEntity entity = new DistanceEntity();
        entity.setWord(str);
        entity.setDistance(cos_distance);
        distances.add(entity);
      }
      model.setLog("Success.");
    } else {
      LOG.error("Invalid vector size.");
      model.setLog("Invalid vector size.");
    }
    model.setInput(vector);
    model.setNum(String.valueOf(num));
    model.setDistances(distances);
    Long endTime = System.currentTimeMillis();
    Long processTime = endTime-startTime;
    model.setStartTime(startTime.toString());
    model.setEndTime(endTime.toString());
    model.setProcessTime(processTime.toString());
    return new ResponseEntity<VectorDistanceResponseEntity>(model,HttpStatus.OK);
  }

  /**
   * 公開用API
   * Dummyメソッド
   *
   * @param access_token
   * @param vector
   * @param num
   * @return
   */
  @RequestMapping(value = {"/vec_distance"}, produces=MediaType.APPLICATION_JSON_VALUE, method=RequestMethod.GET)
  @ApiOperation(value="Word2Vec distance (Vector version)", notes=NOTES_DISTANCE)
  public VectorDistanceResponseEntity vec_distance(
      @ApiParam(value = "Access Token", required = true)
      @RequestParam("access_token")  String access_token,
      @ApiParam(value = "vector [length 200]", required = true)
      @RequestParam("vector")       double[] vector,
      @ApiParam(value = "num [max 10, default 1]", required=false, defaultValue="1")
      @RequestParam(name="num", required=false, defaultValue="1")
      int num)
  {
    return new VectorDistanceResponseEntity();
  }

  /**
   * 実態
   *
   * @param positives
   * @param negatives
   * @param num
   * @return
   */
  @RequestMapping(value="/open/analogy", method=RequestMethod.GET)
  @ApiIgnore
  public ResponseEntity<AnalogyResponseEntity> analogy(
      @RequestParam(name="positives", required=true)
      String positives,
      @RequestParam(name="negatives", required=false, defaultValue="")
      String negatives,
      @RequestParam(name="num", required=false, defaultValue="1")
      int num
      ) {

    AnalogyResponseEntity model = new AnalogyResponseEntity();
    Long startTime = System.currentTimeMillis();

    if (num<0)
      num=1;
    else if (num>10)
      num=10;

    List<String> pos = Arrays.asList(positives.toLowerCase().split("[ 　]"));
    List<String> neg = new ArrayList<String>();
    if (!negatives.isEmpty())
      neg = Arrays.asList(negatives.toLowerCase().split("[ 　]"));

    StringBuffer logb = new StringBuffer();
    logb.append("No such words:");
    List<String> tmp = new ArrayList<String>(pos);
    for (String str: tmp) {
      if (!word2VecService.hasWord(str)) {
        LOG.error(str);
        pos.remove(str);
        logb.append(str+" ");
      }
    }
    tmp = new ArrayList<String>(neg);
    for (String str: tmp) {
      if (!word2VecService.hasWord(str)) {
        LOG.error(str);
        neg.remove(str);
        logb.append(str+" ");
      }
    }

    List<String> analogies=new ArrayList<String>();
    if (!pos.isEmpty()) {
      analogies = word2VecService.wordsNearest(pos, neg, num);
      logb.append(", Success.");
    } else {
      logb.append(", Failure.");
    }
    model.setPositives(positives);
    model.setNegatives(negatives);
    model.setNum(String.valueOf(num));
    model.setAnalogies(analogies);
    Long endTime = System.currentTimeMillis();
    Long processTime = endTime-startTime;
    model.setStartTime(startTime.toString());
    model.setEndTime(endTime.toString());
    model.setProcessTime(processTime.toString());
    model.setLog(logb.toString());
    return new ResponseEntity<AnalogyResponseEntity>(model,HttpStatus.OK);
  }

  /**
   * 公開用API
   * Dummyメソッド
   *
   * @param access_token
   * @param positives
   * @param negatives
   * @param num
   * @return
   */
  @RequestMapping(value = {"/analogy"}, produces=MediaType.APPLICATION_JSON_VALUE, method=RequestMethod.GET)
  @ApiOperation(value="Word2Vec analogy", notes=NOTES_ANALOGY)
  public AnalogyResponseEntity analogy(
      @ApiParam(value = "Access Token", required = true)
      @RequestParam("access_token")  String access_token,
      @ApiParam(value = "positive1 positive2 ...[space separated strings]", required=true)
      @RequestParam(name="positives", required=true)
      String positives,
      @ApiParam(value = "negative1 negative2 ...[space separated strings]", required=false)
      @RequestParam(name="negatives", required=false, defaultValue="")
      String negatives,
      @ApiParam(value = "num [max 10, default 1]", required=false, defaultValue="1")
      @RequestParam(name="num", required=false, defaultValue="1")
      int num)
  {
    return new AnalogyResponseEntity();
  }

  /**
   * 実態
   *
   * @param word1
   * @param word2
   * @return
   */
  @RequestMapping(value="/open/similarity", method=RequestMethod.GET)
  @ApiIgnore
  public ResponseEntity<SimilarityResponseEntity> similarity(
      @RequestParam(name="word1", required=true)
      String word1,
      @RequestParam(name="word2", required=true)
      String word2
      ) {

    SimilarityResponseEntity model = new SimilarityResponseEntity();
    Long startTime = System.currentTimeMillis();

    word1 = word1.toLowerCase();
    word2 = word2.toLowerCase();
    String log;
    if (word2VecService.hasWord(word1) && word2VecService.hasWord(word2)) {
      double similarity = word2VecService.similarity(word1, word2);
      model.setSimilarity(similarity);
      log = "Success.";
    } else {
      LOG.error(word1+" & "+word2);
      log = "No such word.";
    }

    model.setWord1(word1);
    model.setWord2(word2);
    Long endTime = System.currentTimeMillis();
    Long processTime = endTime-startTime;
    model.setStartTime(startTime.toString());
    model.setEndTime(endTime.toString());
    model.setProcessTime(processTime.toString());
    model.setLog(log);
    return new ResponseEntity<SimilarityResponseEntity>(model,HttpStatus.OK);
  }

  /**
   * 公開用API
   * Dummyメソッド
   *
   * @param access_token
   * @param word1
   * @param word2
   * @return
   */
  @RequestMapping(value = {"/similarity"}, produces=MediaType.APPLICATION_JSON_VALUE, method=RequestMethod.GET)
  @ApiOperation(value="Word2Vec similarity", notes=NOTES_SIMILARITY)
  public SimilarityResponseEntity similarity(
      @ApiParam(value = "Access Token", required = true)
      @RequestParam("access_token")  String access_token,
      @ApiParam(value = "word1", required = true)
      @RequestParam(name="word1", required=true)
      String word1,
      @ApiParam(value = "word2", required = true)
      @RequestParam(name="word2", required=true)
      String word2)
  {
    return new SimilarityResponseEntity();
  }

  /**
   * 実態
   *
   * @param word
   * @return
   */
  @RequestMapping(value="/open/wordvector", method=RequestMethod.GET)
  @ApiIgnore
  public ResponseEntity<WordVectorResponseEntity> wordvector(
      @RequestParam(name="word", required=true)
      String word
      ) {

    WordVectorResponseEntity model = new WordVectorResponseEntity();
    Long startTime = System.currentTimeMillis();

    word = word.toLowerCase();
    String log;
    if (word2VecService.hasWord(word)) {
      double[] vector = word2VecService.getWordVectorMatrixNormalized(word);
      model.setVector(vector);
      log = "Success.";
    } else {
      LOG.error(word);
      log = "No such word.";
    }

    model.setWord(word);
    Long endTime = System.currentTimeMillis();
    Long processTime = endTime-startTime;
    model.setStartTime(startTime.toString());
    model.setEndTime(endTime.toString());
    model.setProcessTime(processTime.toString());
    model.setLog(log);
    return new ResponseEntity<WordVectorResponseEntity>(model,HttpStatus.OK);
  }

  /**
   * 公開用API
   * Dummyメソッド
   *
   * @param access_token
   * @param word
   * @return
   */
  @RequestMapping(value = {"/wordvector"}, produces=MediaType.APPLICATION_JSON_VALUE, method=RequestMethod.GET)
  @ApiOperation(value="Word2Vec wordvector", notes=NOTES_WORDVECTOR)
  public WordVectorResponseEntity wordvector(
      @ApiParam(value = "Access Token", required = true)
      @RequestParam("access_token")  String access_token,
      @ApiParam(value = "word", required = true)
      @RequestParam(name="word", required=true)
      String word)
  {
    return new WordVectorResponseEntity();
  }

}
