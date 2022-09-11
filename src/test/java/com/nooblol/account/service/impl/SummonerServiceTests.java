package com.nooblol.account.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.nooblol.account.dto.summoner.SummonerDto;
import com.nooblol.account.service.SummonerService;
import com.nooblol.global.dto.ResponseDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;


public class SummonerServiceTests {

  SummonerService testSummonerService;


  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
    testSummonerService = Mockito.mock(SummonerService.class);
  }

  @Test
  @DisplayName("존재하는 소환사명를 조회시 OK 수신")
  void getSummonerAccountInfo_IsOk() {
    String testSummonerName = "눕는게일상";

    SummonerDto user = new SummonerDto();
    user.setId("zJM0b_kEhZhHKhq6qsL8f4nusWE-IEegWdeqqK3LA3CrnA");
    user.setAccountId("-CZ1UdXj_30sT4ZOb1PDJfiCCZvYTRnYRQncqn8LUBE");
    user.setPuuid("KCSH-FOif2FOuoIFTkXclVK__08YQq8d4H7t96SNpLOVWUU8VDFA_2byLFMGlV_L3jZ0p_cRj-TYUg");
    user.setName("눕는게일상");
    user.setProfileIconId(520);
    user.setRevisionDate(1659980271000L);
    user.setSummonerLevel(223);

    ResponseDto mockDto = new ResponseDto(HttpStatus.OK.value(), user);

    given(testSummonerService.getSummonerAccointInfo(testSummonerName)).willReturn(mockDto);

    ResponseDto resultData = testSummonerService.getSummonerAccointInfo(testSummonerName);
    assertThat(resultData.getResultCode()).isEqualTo(HttpStatus.OK.value());
  }

  @Test
  @DisplayName("존재하지 않는 소환사명 조회시 Not Found 수신")
  void getSummonerAccountInfo_NotFound() {
    String testSummonerName = "테스트를 위해 생길수가 없는 아이디";
    ResponseDto mockDto = new ResponseDto(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND);

    given(testSummonerService.getSummonerAccointInfo(testSummonerName)).willReturn(mockDto);

    ResponseDto resultData = testSummonerService.getSummonerAccointInfo(testSummonerName);
    assertThat(resultData.getResultCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
  }

  @Test
  @DisplayName("소환사명이 공백인 경우 Exception테스트")
  void getSummonerAccountInfo_SummonerNameIsNull() {
    given(testSummonerService.getSummonerAccointInfo(""))
        .willThrow(new IllegalArgumentException("소환사명이 입력되지 않았습니다"));

    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      testSummonerService.getSummonerAccointInfo("");
    });
  }

}
