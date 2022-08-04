package com.nooblol.account.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import com.nooblol.account.dto.SummonerHistoryDto;
import com.nooblol.account.service.SummonerHistoryService;
import com.nooblol.global.dto.ResponseDto;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("local")
class SummonerHistoryServiceImplTest {

  @Autowired
  SummonerHistoryService summonerHistoryService;

  @Test
  @DisplayName("Riot에서 바로 데이터가 받아지는지 확인")
  void 사용자랭크데이터_수신여부확인() {
    String summonerId = "deB6s71eX3jENhwvTGVzJwlBbE--Gjf8MIm74yZvJI7kMDE";
    ResponseDto dto = summonerHistoryService.getSummonerHistoryInfo(summonerId, false);

    assertThat(dto.getResultCode()).isEqualTo(HttpStatus.OK.value());
    assertThat((List<SummonerHistoryDto>) dto.getResult()).filteredOn(
        o -> o.getSummonerId().equals(summonerId));
  }

  @Test
  @DisplayName("Sync가 true여도 DB데이터 없이 정상수신 되는지 확인")
  void 사용자랭크데이터_수신여부확인2() {
    String summonerId = "deB6s71eX3jENhwvTGVzJwlBbE--Gjf8MIm74yZvJI7kMDE";
    ResponseDto dto = summonerHistoryService.getSummonerHistoryInfo(summonerId, true);

    assertThat(dto.getResultCode()).isEqualTo(HttpStatus.OK.value());
    assertThat((List<SummonerHistoryDto>) dto.getResult()).filteredOn(
        o -> o.getSummonerId().equals(summonerId));
  }
  
}