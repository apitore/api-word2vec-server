package com.apitore.api.word2vec;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;


/**
 * @author Keigo Hattori
 */
@EnableFeignClients
@EnableDiscoveryClient
@EnableCircuitBreaker
@EnableZuulProxy
@SpringBootApplication
public class Word2VecAppMain {

  public static void main(String[] args) {
    SpringApplication.run(Word2VecAppMain.class, args);
  }

  @Bean(name="wordVector")
  public WordVectors wordVector() throws IOException {
    InputStream stream = new FileInputStream("jawiki-corpus.model");
    WordVectors vec = WordVectorSerializer.loadTxtVectors(stream, true);
    stream.close();
    return vec;
  }

}