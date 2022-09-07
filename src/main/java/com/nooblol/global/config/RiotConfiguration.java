package com.nooblol.global.config;

import com.nooblol.global.utils.YamlLoadFactory;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;

@Configuration
@PropertySource(value = "classpath:constants.yml", factory = YamlLoadFactory.class)
@ConfigurationProperties(prefix = "riot")
@Getter
@Setter
public class RiotConfiguration {

  private String apiKey;

  private String summonerDomain;

  private String summonerNameSearchByIdApi;

  private String summonerNameSearchByNameApi;

  private String summonerHistorySearchBySummonerIdApi;

  private String matchDomain;

  private String matchListSearchByPuuid;

  private String matchGameInfoByMatchId;

  @Bean
  public HttpHeaders initRiotHeader() {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("X-Riot-Token", getApiKey());

    return httpHeaders;
  }
}
