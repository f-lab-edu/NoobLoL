package com.nooblol.global.config;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * 추가적인 설정 필요시 https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/client/HttpComponentsClientHttpRequestFactory.html
 * https://www.javadoc.io/doc/org.apache.httpcomponents/httpclient/4.4/org/apache/http/impl/client/HttpClientBuilder.html
 */
@Configuration
public class RestTemplateConfig {

  @Bean
  public HttpClient httpClient() {
    return HttpClientBuilder.create()
        .setMaxConnTotal(100)   //Connection Pool 갯수
        .setMaxConnPerRoute(10) //IP, Port쌍으로 묶어 연결 제한 갯수
        .build();
  }

  @Bean
  public HttpComponentsClientHttpRequestFactory requestFactory(HttpClient httpClient) {
    HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
    requestFactory.setReadTimeout(5000);
    requestFactory.setConnectTimeout(3000);
    requestFactory.setHttpClient(httpClient);
    return requestFactory;
  }

  @Bean
  public RestTemplate restTemplate(HttpComponentsClientHttpRequestFactory requestFactory) {
    return new RestTemplate(requestFactory);
  }
  
}
