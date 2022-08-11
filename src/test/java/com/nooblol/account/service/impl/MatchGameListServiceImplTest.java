package com.nooblol.account.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import com.nooblol.account.service.MatchGameListService;
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

class MatchGameListServiceImplTest {

  MatchGameListService testMatchGameListService;

  String responseOkPuuid = "KCSH-FOif2FOuoIFTkXclVK__08YQq8d4H7t96SNpLOVWUU8VDFA_2byLFMGlV_L3jZ0p_cRj-TYUg";
  String responseNotFound = "abcdefgh";

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
    testMatchGameListService = Mockito.mock(MatchGameListService.class);
  }

  @Test
  @DisplayName("Puuid를 Null로 전달시 Exception 확인 테스트")
  void confirm_GivenPuuidNull_ThenIllegalArgumentException() {
    given(testMatchGameListService.getMatchListId(null)).willThrow(
        new IllegalArgumentException("PuuId가 입력되지 않았습니다."));

    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      testMatchGameListService.getMatchListId(null);
    });
  }

  @Test
  @DisplayName("Puuid를 존재하지 않는 Puuid 전달시 Not_Found 수신 테스트")
  void confirm_GivenPuuid_WhenSearchRiot_ThenNotFound() {
    ResponseDto mockDto = new ResponseDto(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND);

    given(testMatchGameListService.getMatchListId(responseNotFound)).willReturn(mockDto);

    ResponseDto resultData = testMatchGameListService.getMatchListId(responseNotFound);
    assertThat(resultData.getResultCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
  }

  @Test
  @DisplayName("Puuid를 존재하는 Puuid 전달시 OK 수신 테스트")
  void confirm_GivenPuuid_WhenSearchRiot_ThenOk() {
    List testMatchList = new ArrayList<String>();
    testMatchList.add("KR_6064599598");
    testMatchList.add("KR_6064566915");
    testMatchList.add("KR_6064574913");
    testMatchList.add("KR_6064552661");
    testMatchList.add("KR_6064419385");
    testMatchList.add("KR_6063040063");
    testMatchList.add("KR_6062828957");
    testMatchList.add("KR_6062864802");
    testMatchList.add("KR_6062823563");

    ResponseDto mockDto = new ResponseDto(HttpStatus.OK.value(), testMatchList);

    given(testMatchGameListService.getMatchListId(responseOkPuuid)).willReturn(mockDto);

    ResponseDto resultData = testMatchGameListService.getMatchListId(responseOkPuuid);
    assertThat(resultData.getResultCode()).isEqualTo(HttpStatus.OK.value());
  }
}