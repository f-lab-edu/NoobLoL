package com.nooblol.global.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@DisplayName("yml파일의 Riot Constant Value 확인")
@TestPropertySource(value = {"classpath:constants.yml"})
@SpringBootTest
class RiotConfigurationTest {

  @Autowired
  RiotConfiguration riotConfiguration;

  @Test
  @DisplayName("RIOT APIKEY주입여부 확인")
  void confirmRiotApiKeyValueInject(@Value("${riot.apiKey}") String apiKey) {
    String configApiKey = riotConfiguration.getApiKey();

    assertThat(configApiKey)
        .isNotEmpty()
        .isEqualTo(apiKey);
  }

  @Test
  @DisplayName("소환사명 링크의 주입여부 확인")
  void confirmSearchNameUrlInject(
      @Value("${riot.summoner-name-search-api-url}") String searchNameUrl) {
    String getSearchNameUrl = riotConfiguration.getSummonerNameSearchApiUrl();

    assertThat(getSearchNameUrl)
        .isNotEmpty()
        .isEqualTo(searchNameUrl);
  }
}