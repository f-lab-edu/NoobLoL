package com.nooblol.global.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DisplayName("yml파일의 Riot Constant Value 확인")
@ExtendWith(SpringExtension.class)
@EnableConfigurationProperties(value = RiotConfiguration.class)
@TestPropertySource("classpath:constants.yml")
class RiotConfigurationTest {

  @Autowired
  private RiotConfiguration riotConfiguration;

  @Test
  @DisplayName("RIOT APIKEY  Value Null Check")
  void confirm_Riot_ApiKey_Null_Check(@Value("${riot.apiKey}") String apiValue) {
    assertThat(apiValue).isNotEmpty().isEqualTo(riotConfiguration.getApiKey());
  }

  @Test
  @DisplayName("소환사계정정보 도메인 Value Null Check")
  void confirm_SummonerDomain_Null_Check(@Value("${riot.summoner-domain}") String apiValue) {
    assertThat(apiValue).isNotEmpty().isEqualTo(riotConfiguration.getSummonerDomain());
  }

  @Test
  @DisplayName("AccountId로 계정정보 조회 가능한 API Value Null Check")
  void confirm_SummonerNameSearchByIdApi_Null_Check(
      @Value("${riot.summoner-name-search-by-id-api}") String apiValue
  ) {
    assertThat(apiValue).isNotEmpty().isEqualTo(riotConfiguration.getSummonerNameSearchByIdApi());
  }

  @Test
  @DisplayName("SummonerName으로 계정정보 조회 가능한 API Value Null Check")
  void confirm_SummonerNameSearchByNameApi_Null_Check(
      @Value("${riot.summoner-name-search-by-name-api}") String apiValue) {
    assertThat(apiValue).isNotEmpty().isEqualTo(riotConfiguration.getSummonerNameSearchByNameApi());
  }


  @Test
  @DisplayName("MatchGame정보 조회가능한 도메인 Value Null Check")
  void confirm_MatchDomain_Null_Check(@Value("${riot.match-domain}") String apiValue
  ) {
    assertThat(apiValue).isNotEmpty().isEqualTo(riotConfiguration.getMatchDomain());
  }

  @Test
  @DisplayName("Match List Id를 Puuid로 획득 가능한 API Value Null Check")
  void confirm_MatchListSearchByPuuid_Null_Check(
      @Value("${riot.match-list-search-by-puuid}") String apiValue
  ) {
    assertThat(apiValue).isNotEmpty().isEqualTo(riotConfiguration.getMatchListSearchByPuuid());
  }

  @Test
  @DisplayName("MatchId를 통해 상세한 게임 내용에 대해 획득 가능한 API Value Null Check")
  void confirm_MatchGameInfoByMatchId_Null_Check(
      @Value("${riot.match-game-info-by-match-id}") String apiValue
  ) {
    assertThat(apiValue).isNotEmpty().isEqualTo(riotConfiguration.getMatchGameInfoByMatchId());
  }
}