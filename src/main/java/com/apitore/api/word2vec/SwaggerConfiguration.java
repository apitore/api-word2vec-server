package com.apitore.api.word2vec;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static springfox.documentation.builders.PathSelectors.*;

import java.util.Date;

import static com.google.common.base.Predicates.*;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


/**
 * @author Keigo Hattori
 */
@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

  final String PATH = "com.apitore.api.word2vec.controller";

  @SuppressWarnings("unchecked")
  @Bean
  public Docket word2vecAPI() {
    return new Docket(DocumentationType.SWAGGER_2)
        .groupName("word2vec")
        .select()
        .apis(RequestHandlerSelectors.basePackage(PATH))
        .paths(or(
            regex(".*/analogy.*"),
            regex(".*/distance.*"),
            regex(".*/vec_distance.*"),
            regex(".*/similarity.*"),
            regex(".*/wordvector.*")
            ))
        .build()
        .apiInfo(
            new ApiInfoBuilder()
            .title("Word2Vec APIs")
            .description("Word2Vec.")
            .version("0.0.1")
            .build()
            )
        .directModelSubstitute(Date.class, Long.class);
  }

  @SuppressWarnings("unchecked")
  @Bean
  public Docket synonymAPI() {
    return new Docket(DocumentationType.SWAGGER_2)
        .groupName("synonym")
        .select()
        .apis(RequestHandlerSelectors.basePackage(PATH))
        .paths(or(
            regex(".*/distance.*")
            ))
        .build()
        .apiInfo(
            new ApiInfoBuilder()
            .title("Synonym APIs")
            .description("Return synonymous words. (equal to \"word2vec distance\")")
            .version("0.0.1")
            .build()
            )
        .directModelSubstitute(Date.class, Long.class);
  }

  @SuppressWarnings("unchecked")
  @Bean
  public Docket vectorAPI() {
    return new Docket(DocumentationType.SWAGGER_2)
        .groupName("vector")
        .select()
        .apis(RequestHandlerSelectors.basePackage(PATH))
        .paths(or(
            regex(".*/wordvector.*")
            ))
        .build()
        .apiInfo(
            new ApiInfoBuilder()
            .title("Words to Vectors APIs")
            .description("Word to vectors.")
            .version("0.0.1")
            .build()
            )
        .directModelSubstitute(Date.class, Long.class);
  }

}