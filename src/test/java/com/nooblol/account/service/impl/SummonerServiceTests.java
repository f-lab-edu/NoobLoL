package com.nooblol.account.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.nooblol.account.dto.SummonerDto;
import com.nooblol.account.mapper.SummonerMapper;
import com.nooblol.account.service.SummonerService;
import com.nooblol.global.dto.ResponseDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

@SpringBootTest
public class SummonerServiceTests {

  @Autowired
  SummonerService summonerService;

  @Autowired
  SummonerMapper summonerMapper;

  @Test
  @DisplayName("존재하는 소환사명 테스트")
  void getSummonerAccountInfo_IsOk() {
    ResponseDto riotServerData = summonerService.selectSummonerAccountByRiot("눕는게 일상");
    assertThat(riotServerData.getResultCode()).isEqualTo(HttpStatus.OK.value());
  }

  @Test
  @DisplayName("존재하지 않는 소환사명 - Not Found")
  void getSummonerAccountInfo_NotFound() {
    ResponseDto data = summonerService.selectSummonerAccountByRiot("테스트를 위해 생길수가 없는 아이디");
    assertThat(data.getResultCode()).isNotNull().isEqualTo(HttpStatus.NOT_FOUND.value());
  }

  @Test
  @DisplayName("소환사명이 공백인 경우 Exception테스트")
  void getSummonerAccountInfo_SummonerNameIsNull() {
    try {
      summonerService.getSummonerAccointInfo("");
    } catch (IllegalArgumentException e) {
      Assertions.assertEquals("소환사명이 입력되지 않았습니다", e.getMessage());
    }
  }
}
