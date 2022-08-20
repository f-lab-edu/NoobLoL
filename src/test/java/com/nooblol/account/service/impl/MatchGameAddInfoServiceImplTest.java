package com.nooblol.account.service.impl;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import com.nooblol.account.dto.match.MatchGameBansDto;
import com.nooblol.account.dto.match.MatchGameParticipantsDto;
import com.nooblol.account.dto.match.MatchUseRuneDto;
import com.nooblol.account.mapper.MatchGameAddInfoMapper;
import com.nooblol.account.service.MatchGameAddInfoService;
import com.nooblol.global.dto.ResponseDto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;

class MatchGameAddInfoServiceImplTest {

  @Mock
  private MatchGameAddInfoMapper matchGameAddInfoMapper;

  private MatchGameAddInfoService matchGameAddInfoService;

  public MatchGameAddInfoServiceImplTest() {
    matchGameAddInfoMapper = Mockito.mock(MatchGameAddInfoMapper.class);
    this.matchGameAddInfoService = new MatchGameAddInfoServiceImpl(matchGameAddInfoMapper);
  }


  @Test
  @DisplayName("모든 참가자 정보 조회시 MatchId를 Null로 조회할 경우 Exception을 획득 한다.")
  void matchAllParticipantsList_ExceptionTest() {
    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      String matchId = null;
      matchGameAddInfoService.getMatchAllParticipantsList(matchId);
    });

    assertEquals("getMatchAllParticipantsList(String) : MatchId가 입력되지 않았습니다.",
        exception.getMessage());
  }

  @Test
  @DisplayName("모든 참가자 정보 조회시 DB에 존재하는 MatchId인 경우 OK상태값을 획득 한다.")
  void matchAllParticipantsList_ListReturnOkTest() {
    ArrayList<MatchGameParticipantsDto> mockReturnList = new ArrayList<>();
    String matchId = "KR_6064599598";

    MatchGameParticipantsDto part1 = new MatchGameParticipantsDto();
    part1.setSummonerName("샢믈1");
    MatchGameParticipantsDto part2 = new MatchGameParticipantsDto();
    part2.setSummonerName("샢믈2");
    MatchGameParticipantsDto part3 = new MatchGameParticipantsDto();
    part3.setSummonerName("샢믈3");
    MatchGameParticipantsDto part4 = new MatchGameParticipantsDto();
    part4.setSummonerName("샢믈4");
    MatchGameParticipantsDto part5 = new MatchGameParticipantsDto();
    part5.setSummonerName("샢믈5");

    mockReturnList.add(part1);
    mockReturnList.add(part2);
    mockReturnList.add(part3);
    mockReturnList.add(part4);
    mockReturnList.add(part5);

    when(matchGameAddInfoMapper.selectMatchAllParticipantsListByMatchId(matchId))
        .thenReturn(mockReturnList);

    ResponseDto result = matchGameAddInfoService.getMatchAllParticipantsList(matchId);
    List<MatchGameParticipantsDto> resultList = (List<MatchGameParticipantsDto>) result.getResult();
    assertThat(result.getResultCode()).isEqualTo(HttpStatus.OK.value());
    assertThat(resultList.size() > 0).isTrue();
  }

  @Test
  @DisplayName("모든 참가자 정보 조회시 DB에 존재하지 않는 MatchId인 경우 NotFound를 획득 한다.")
  void matchAllParticipantsList_ReturnNotFound() {
    ArrayList<MatchGameParticipantsDto> mockReturnList = new ArrayList<>();
    String matchId = "KR_6064599598";

    given(matchGameAddInfoMapper.selectMatchAllParticipantsListByMatchId(matchId))
        .willReturn(mockReturnList);

    ResponseDto result = matchGameAddInfoService.getMatchAllParticipantsList(matchId);
    assertThat(result.getResultCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
  }


  @Test
  @DisplayName("게임의 벤이된 챔피언리스트 조회시 MatchId를 Null로 조회할 경우 Exception을 획득 한다.")
  void matchBanList_ExceptionTest() {
    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      String matchId = null;
      matchGameAddInfoService.getMatchBanList(matchId);
    });

    assertEquals("getMatchBanList(String) : MatchId가 입력되지 않았습니다.", exception.getMessage());
  }

  @Test
  @DisplayName("게임의 벤이된 챔피언리스트 조회시 DB에 존재하는 MatchId인 경우 OK상태값을 획득 한다.")
  void matchBanList_ListReturnOkTest() {
    ArrayList<MatchGameBansDto> mockReturnList = new ArrayList<>();
    String matchId = "KR_6064599598";

    MatchGameBansDto ban1 = new MatchGameBansDto();
    ban1.setChampionId(22);
    MatchGameBansDto ban2 = new MatchGameBansDto();
    ban2.setChampionId(23);
    MatchGameBansDto ban3 = new MatchGameBansDto();
    ban3.setChampionId(24);
    MatchGameBansDto ban4 = new MatchGameBansDto();
    ban4.setChampionId(25);
    MatchGameBansDto ban5 = new MatchGameBansDto();
    ban5.setChampionId(26);

    mockReturnList.add(ban1);
    mockReturnList.add(ban2);
    mockReturnList.add(ban3);
    mockReturnList.add(ban4);
    mockReturnList.add(ban5);

    given(matchGameAddInfoMapper.selectMatchGameBanList(matchId))
        .willReturn(mockReturnList);

    ResponseDto result = matchGameAddInfoService.getMatchBanList(matchId);
    List<MatchGameBansDto> resultList = (List<MatchGameBansDto>) result.getResult();
    assertThat(result.getResultCode()).isEqualTo(HttpStatus.OK.value());
    assertThat(resultList.size() > 0).isTrue();
  }

  @Test
  @DisplayName("게임의 벤이된 챔피언리스트 조회시 DB에 존재하지 않는 MatchId인 경우 NotFound를 획득 한다.")
  void matchBanList_ReturnNotFound() {
    ArrayList<MatchGameBansDto> mockReturnList = new ArrayList<>();
    String matchId = "KR_6064599598";

    given(matchGameAddInfoMapper.selectMatchGameBanList(matchId))
        .willReturn(mockReturnList);

    ResponseDto result = matchGameAddInfoService.getMatchBanList(matchId);
    assertThat(result.getResultCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
  }


  @Test
  @DisplayName("게임에서 사용한 특정 유저의 룬정보 조회시 MatchId가 Null로 조회할 경우 Exception을 획득 한다.")
  void getMatchUseRuneList_MatchIdNullExceptionTest() {
    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      String matchId = null;
      String puuid = "a";
      matchGameAddInfoService.getMatchUseRunList(matchId, puuid);
    });

    assertEquals("getMatchUseRunList : MatchId가 입력되지 않았습니다.", exception.getMessage());
  }

  @Test
  @DisplayName("게임에서 사용한 특정 유저의 룬정보 조회시 MatchId와 Puuid를 Null로 조회할 경우 MatchId Exception을 획득 한다.")
  void getMatchUseRuneList_MatchIdNullExceptionTest2() {
    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      String matchId = null;
      String puuid = null;
      matchGameAddInfoService.getMatchUseRunList(matchId, puuid);
    });

    assertEquals("getMatchUseRunList : MatchId가 입력되지 않았습니다.", exception.getMessage());
  }

  @Test
  @DisplayName("게임에서 사용한 특정 유저의 룬정보 조회시 Puuid가 Null로 조회할 경우 Puuid의 Exception을 획득 한다.")
  void getMatchUseRuneList_PuuIdNullExceptionTest() {
    Exception exception = assertThrows(IllegalArgumentException.class, () -> {
      String matchId = "sample";
      String puuid = null;
      matchGameAddInfoService.getMatchUseRunList(matchId, puuid);
    });

    assertEquals("getMatchUseRunList : puuid가 입력되지 않았습니다.", exception.getMessage());
  }

  @Test
  @DisplayName("게임에서 사용한 특정 유저의 룬정보 조회시 DB에 존재하는 MatchId, Puuid인 경우 OK상태값을 획득 한다.")
  void getMatchUseRuneList_ListReturnOkTest() {
    ArrayList<MatchUseRuneDto> mockReturnList = new ArrayList<>();
    String matchId = "KR_6064599598";
    String puuid = "Test_Puuid";

    MatchUseRuneDto rune1 = new MatchUseRuneDto();
    MatchUseRuneDto rune2 = new MatchUseRuneDto();
    MatchUseRuneDto rune3 = new MatchUseRuneDto();
    MatchUseRuneDto rune4 = new MatchUseRuneDto();
    MatchUseRuneDto rune5 = new MatchUseRuneDto();

    rune1.setMatchId(matchId);
    rune2.setMatchId(matchId);
    rune3.setMatchId(matchId);
    rune4.setMatchId(matchId);
    rune5.setMatchId(matchId);

    mockReturnList.add(rune1);
    mockReturnList.add(rune2);
    mockReturnList.add(rune3);
    mockReturnList.add(rune4);
    mockReturnList.add(rune5);

    Map<String, String> paramMap = new HashMap<>();
    paramMap.put("matchId", matchId);
    paramMap.put("puuid", puuid);
    given(matchGameAddInfoMapper.selectMatchGameUseRunes(paramMap))
        .willReturn(mockReturnList);

    ResponseDto result = matchGameAddInfoService.getMatchUseRunList(matchId, puuid);
    List<MatchGameBansDto> resultList = (List<MatchGameBansDto>) result.getResult();
    assertThat(result.getResultCode()).isEqualTo(HttpStatus.OK.value());
    assertThat(resultList.size() > 0).isTrue();
  }

  @Test
  @DisplayName("게임에서 사용한 특정 유저의 룬정보 조회시 DB에 존재하지 않는 MatchId, Puuid인 경우 NotFound를 획득 한다.")
  void getMatchUseRuneList_ReturnNotFound() {
    ArrayList<MatchGameBansDto> mockReturnList = new ArrayList<>();
    String matchId = "KR_6064599598";

    given(matchGameAddInfoMapper.selectMatchGameBanList(matchId))
        .willReturn(mockReturnList);

    ResponseDto result = matchGameAddInfoService.getMatchBanList(matchId);
    assertThat(result.getResultCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
  }
}