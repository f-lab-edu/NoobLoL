package com.nooblol.global.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class ObjectMapperConfig {

  @Bean("objectMapper")
  public ObjectMapper objectMapper() {
    return Jackson2ObjectMapperBuilder
        .json()
        .featuresToDisable(SerializationFeature.FAIL_ON_EMPTY_BEANS) //변수가 없어도 상관이 없도록 설.
        .featuresToDisable(
            SerializationFeature.WRITE_DATES_AS_TIMESTAMPS) //Time을 JavaTimeModule(ISO형식 자동변환하도록)
        .modules(new JavaTimeModule())
        .build();
  }
}
