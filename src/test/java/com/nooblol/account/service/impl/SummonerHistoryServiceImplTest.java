package com.nooblol.account.service.impl;

import static org.mockito.BDDMockito.given;

import com.nooblol.account.dto.SummonerHistoryDto;
import com.nooblol.account.service.SummonerHistoryService;
import com.nooblol.global.dto.ResponseDto;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

class SummonerHistoryServiceImplTest {

  SummonerHistoryService testSummonerHistoryService;
  String testSummonerId = "deB6s71eX3jENhwvTGVzJwlBbE--Gjf8MIm74yZvJI7kMDE";

  ResponseDto okResponseDto;
  ResponseDto notFoundResponseDto;

  List<SummonerHistoryDto> mockTestList;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
    this.testSummonerHistoryService = Mockito.mock(SummonerHistoryService.class);

    SummonerHistoryDto mockSample1 = new SummonerHistoryDto();
    mockSample1.setLeagueId("05fb99f4-e149-3133-a78e-821597582f9d");
    mockSample1.setQueueType("RANKED_SOLO_5x5");
    mockSample1.setTier("CHALLENGER");
    mockSample1.setRank("I");
    mockSample1.setSummonerId("deB6s71eX3jENhwvTGVzJwlBbE--Gjf8MIm74yZvJI7kMDE");
    mockSample1.setSummonerName("몽키데이");
    mockSample1.setLeaguePoints(858);
    mockSample1.setWins(416);
    mockSample1.setLosses(362);

    SummonerHistoryDto mockSAmple2 = new SummonerHistoryDto();
    mockSAmple2.setLeagueId("5e3ea9f9-c6d1-43ff-ac77-9fcf5a451840");
    mockSAmple2.setQueueType("RANKED_FLEX_SR");
    mockSAmple2.setTier("DIAMOND");
    mockSAmple2.setRank("I");
    mockSAmple2.setSummonerId("deB6s71eX3jENhwvTGVzJwlBbE--Gjf8MIm74yZvJI7kMDE");
    mockSAmple2.setSummonerName("몽키데이");
    mockSAmple2.setLeaguePoints(50);
    mockSAmple2.setWins(56);
    mockSAmple2.setLosses(50);

    this.mockTestList = new ArrayList<>();
    mockTestList.add(mockSample1);
    mockTestList.add(mockSAmple2);

    okResponseDto = new ResponseDto(HttpStatus.OK.value(), mockTestList);
    notFoundResponseDto = new ResponseDto(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND);
  }

  @Test
  @DisplayName("Riot서버에 존재하는 ID 조회시 OK수신 확인")
  void 사용자랭크데이터_수신_OK_확인() {
    given(testSummonerHistoryService.getSummonerHistoryInfo(testSummonerId, false))
        .willReturn(okResponseDto);

    ResponseDto resultData =
        testSummonerHistoryService.getSummonerHistoryInfo(testSummonerId, false);

    assertThat(resultData.getResultCode()).isEqualTo(HttpStatus.OK.value());
  }

  @Test
  @DisplayName("Riot서버에 존재하지 않는 ID 조회시 NOT_FOUND 수신 확인")
  void 사용자랭크데이터_수신_NOT_FOUND_확인() {
    String notFoundTestSummonerId = "abcdefghhsdnc";
    given(testSummonerHistoryService.getSummonerHistoryInfo(notFoundTestSummonerId, false))
        .willReturn(notFoundResponseDto);

    ResponseDto resultData =
        testSummonerHistoryService.getSummonerHistoryInfo(notFoundTestSummonerId, false);

    assertThat(resultData.getResultCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
  }


  @Test
  @DisplayName("Account ID 파라미터 Null인경우 Exception테스트")
  void 사용자랭크데이터조회_파라미터_Null입력시_Exception() {
    given(testSummonerHistoryService.getSummonerHistoryInfo(null, true))
        .willThrow(new IllegalArgumentException("summonerId가 입력되지 않았습니다."));

    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      testSummonerHistoryService.getSummonerHistoryInfo(null, true);
    });
  }

}