package com.nooblol.global.utils;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

import org.springframework.boot.test.autoconfigure.restdocs.RestDocsMockMvcConfigurationCustomizer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;


/*
Reference : https://techblog.woowahan.com/2597/
 */
@TestConfiguration
public class RestDocConfiguration {

  @Bean
  public RestDocsMockMvcConfigurationCustomizer restDocsMockMvcConfigurationCustomizer() {
    return configurer -> configurer.operationPreprocessors()
        .withRequestDefaults(prettyPrint())
        .withResponseDefaults(prettyPrint());
  }
}
