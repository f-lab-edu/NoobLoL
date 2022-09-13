package com.nooblol.account.service.impl;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.*;

import com.nooblol.account.dto.match.MatchGameBansDto;
import com.nooblol.account.dto.match.MatchGameParticipantsDto;
import com.nooblol.account.dto.match.MatchUseRuneDto;
import com.nooblol.account.mapper.MatchGameAddInfoMapper;
import com.nooblol.account.service.MatchGameAddInfoService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

class MatchGameAddInfoServiceImplTest {

  @Mock
  private MatchGameAddInfoMapper matchGameAddInfoMapper;

  private MatchGameAddInfoService matchGameAddInfoService;

  public MatchGameAddInfoServiceImplTest() {
    matchGameAddInfoMapper = Mockito.mock(MatchGameAddInfoMapper.class);
    this.matchGameAddInfoService = new MatchGameAddInfoServiceImpl(matchGameAddInfoMapper);
  }

  @Test
  @DisplayName("모든 참가자 정보 조회시 DB에 존재하는 MatchId인 경우, 참가자 정보에 대한 List를 획득한다")
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

    List<MatchGameParticipantsDto> result =
        matchGameAddInfoService.getMatchAllParticipantsList(matchId);

    assertThat(result.size() > 0).isTrue();
    assertEquals(mockReturnList, result);
  }

  @Test
  @DisplayName("모든 참가자 정보 조회시 DB에 존재하지 않는 MatchId인 경우 빈 List를 획득 한다.")
  void matchAllParticipantsList_ReturnNotFound() {
    ArrayList<MatchGameParticipantsDto> mockReturnList = new ArrayList<>();
    String matchId = "KR_6064599598";

    given(matchGameAddInfoMapper.selectMatchAllParticipantsListByMatchId(matchId))
        .willReturn(mockReturnList);

    List<MatchGameParticipantsDto> result =
        matchGameAddInfoService.getMatchAllParticipantsList(matchId);

    assertEquals(result.size(), 0);
  }

  @Test
  @DisplayName("게임의 벤이된 챔피언리스트 조회시 DB에 존재하는 MatchId인 경우 벤한 챔피언의 List를 획득 한다.")
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

    List<MatchGameBansDto> result = matchGameAddInfoService.getMatchBanList(matchId);

    assertEquals(result, mockReturnList);
  }

  @Test
  @DisplayName("게임의 벤이된 챔피언리스트 조회시 DB에 존재하지 않는 MatchId인 경우 빈 List를 획득 한다.")
  void matchBanList_ReturnNotFound() {
    ArrayList<MatchGameBansDto> mockReturnList = new ArrayList<>();
    String matchId = "KR_6064599598";

    given(matchGameAddInfoMapper.selectMatchGameBanList(matchId))
        .willReturn(mockReturnList);

    List<MatchGameBansDto> result = matchGameAddInfoService.getMatchBanList(matchId);
    assertEquals(result.size(), 0);
  }

  @Test
  @DisplayName("게임에서 사용한 특정 유저의 룬정보 조회시 DB에 존재하는 MatchId, Puuid인 경우 사용한 룬정보 List를 획득 한다.")
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

    given(matchGameAddInfoMapper.selectMatchGameUseRunes(puuid, matchId))
        .willReturn(mockReturnList);

    List<MatchUseRuneDto> result = matchGameAddInfoService.getMatchUseRunList(matchId, puuid);
    assertEquals(result, mockReturnList);
  }

  @Test
  @DisplayName("게임에서 사용한 특정 유저의 룬정보 조회시 DB에 존재하지 않는 MatchId, Puuid인 경우 빈 리스트를  획득 한다.")
  void getMatchUseRuneList_ReturnNotFound() {
    ArrayList<MatchUseRuneDto> mockReturnList = new ArrayList<>();
    String matchId = "KR_6064599598";
    String puuid = "Test_Puuid";

    given(matchGameAddInfoMapper.selectMatchGameUseRunes(puuid, matchId))
        .willReturn(mockReturnList);

    List<MatchUseRuneDto> result = matchGameAddInfoService.getMatchUseRunList(matchId, puuid);

    assertEquals(result, mockReturnList);
  }
}