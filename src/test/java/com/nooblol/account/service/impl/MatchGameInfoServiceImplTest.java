package com.nooblol.account.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.when;


import com.nooblol.account.dto.match.MatchDto;
import com.nooblol.account.dto.match.MatchGameInfoDto;
import com.nooblol.account.dto.match.MatchGameParticipantsDto;
import com.nooblol.account.dto.match.MatchGameRunesDto;
import com.nooblol.account.dto.match.MatchGameSimpleDto;
import com.nooblol.account.dto.match.MatchMetaDataDto;
import com.nooblol.account.dto.match.RuneStatsDto;
import com.nooblol.account.dto.match.RuneStyleDto;
import com.nooblol.account.dto.match.RuneStyleSelectionDto;
import com.nooblol.account.mapper.MatchGameInfoMapper;
import com.nooblol.account.mapper.MatchGameAddInfoMapper;
import com.nooblol.account.service.MatchGameListService;
import com.nooblol.global.config.RiotConfiguration;
import com.nooblol.global.dto.ResponseDto;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;


@ExtendWith(MockitoExtension.class)
class MatchGameInfoServiceImplTest {

  private final Logger log = LoggerFactory.getLogger(getClass());

  @InjectMocks
  private MatchGameInfoServiceImpl matchGameInfoService;

  @Mock
  private MatchGameInfoMapper matchGameInfoMapper;

  @Mock
  private MatchGameAddInfoMapper matchGameAddInfoMapper;

  @Mock
  private RiotConfiguration riotConfiguration;

  @Mock
  private MatchGameListService matchGameListService;

  String responseNotFoundPuuid = "abcdefgh";
  String responseOkPuuid = "KCSH-FOif2FOuoIFTkXclVK__08YQq8d4H7t96SNpLOVWUU8VDFA_2byLFMGlV_L3jZ0p_cRj-TYUg";
  String responseOkMatchId = "KR_6077331700";

  @Test
  @DisplayName("matchId 전달시 RIOT API의 {matchId}가 정상 치환이 되고 URI주소의 획득이 가능하다")
  void confirm_getMakeUri() {
    String equalString =
        "https://asia.api.riotgames.com/" + "lol/match/v5/matches/" + responseOkMatchId;

    when(riotConfiguration.getMatchDomain()).thenReturn("https://asia.api.riotgames.com/");
    when(riotConfiguration.getMatchGameInfoByMatchId()).thenReturn(
        "lol/match/v5/matches/{matchId}");

    assertEquals(matchGameInfoService.getMakeUri(responseOkMatchId), equalString);
  }

  @Test
  @DisplayName("insertMatchDataByDb메소드의 파라미터를 Null로 전달시 Return값이 false임을 확인한다")
  void confirm_InsertMatchDataByDB_ReturnFalseTest() {
    MatchDto dto = null;
    try {
      assertEquals(matchGameInfoService.insertMatchDataByDB(dto), false);
    } catch (Exception e) {
      log.error(e.getMessage());
    }
  }

  @Test
  @DisplayName("insertMatchDataByDb메소드에 MatchDto 파라미터 전달시 return값이 true임을 확인한다")
  void confirm_InsertMatchDataByDB_ReturnTrueTest() {
    MatchDto dto = new MatchDto();
    dto.setMetadata(new MatchMetaDataDto());
    dto.getMetadata().setMatchId("a");
    dto.getMetadata().setDataVersion("22");

    dto.setInfo(new MatchGameInfoDto());
    dto.getInfo().setMatchId("a");

    MatchGameParticipantsDto participant = new MatchGameParticipantsDto();
    participant.setPuuid(responseOkPuuid);

    MatchGameRunesDto rune = new MatchGameRunesDto();
    rune.setStatPerks(new RuneStatsDto());
    rune.getStatPerks().setDefense(5);
    rune.getStatPerks().setOffense(1);
    rune.getStatPerks().setFlex(8);

    RuneStyleDto runeStyle = new RuneStyleDto();
    runeStyle.setDescription("primaryStyle");
    runeStyle.setStyle(8100);

    RuneStyleSelectionDto selction = new RuneStyleSelectionDto();
    selction.setPerk(8135);

    List<RuneStyleSelectionDto> selectionlist = new ArrayList<>();
    selectionlist.add(selction);

    runeStyle.setSelections(selectionlist);

    List<RuneStyleDto> styleList = new ArrayList<>();
    styleList.add(runeStyle);

    rune.setStyles(styleList);

    participant.setPerks(rune);

    List<MatchGameParticipantsDto> participants = new ArrayList<>();
    participants.add(participant);

    dto.getInfo().setParticipants(participants);

    try {
      assertEquals(matchGameInfoService.insertMatchDataByDB(dto), true);
    } catch (Exception e) {
      log.error(e.getMessage());
    }
  }

  @Test
  @DisplayName("MatchId 중복 조회시 DB에는 데이터가 존재하지 않는 MatchId의 리스트를 반환 확인 테스트")
  void confirm_getNotExistsMatchList_Return_MatchIdList() {
    List<String> mockRiotMatchIdList = new ArrayList<>();

    mockRiotMatchIdList.add("KR_5806219000");
    mockRiotMatchIdList.add("KR_5806223992");
    mockRiotMatchIdList.add("KR_5807531455");

    //중복
    mockRiotMatchIdList.add("KR_5807537888");
    mockRiotMatchIdList.add("KR_5807702658");

    ArrayList<String> mockReturnMatchIdList = new ArrayList<>();
    mockReturnMatchIdList.add("KR_5807537888");
    mockReturnMatchIdList.add("KR_5807702658");

    StringBuilder matchIdListBuilder = new StringBuilder();

    mockRiotMatchIdList.stream().forEach(matchId -> {
      matchIdListBuilder.append("'" + matchId + "',");
    });

    matchIdListBuilder.deleteCharAt(matchIdListBuilder.length() - 1);

    when(matchGameInfoMapper.existsMatchIdListByMatch(matchIdListBuilder.toString()))
        .thenReturn(mockReturnMatchIdList);

    List<String> notExistsList = matchGameInfoService.getNotExistMatchList(mockRiotMatchIdList);

    Assertions.assertThat(notExistsList)
        .contains("KR_5806219000", "KR_5806223992", "KR_5807531455");
  }

  @Test
  @DisplayName("Riot 서버에 존재하지 않는 puuid를 발송하는 경우 NotFound를 반환 받는다.")
  void getReturn_ResponseNotFound() {
    ResponseDto notFound = new ResponseDto(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND);
    when(matchGameListService.getMatchListId(responseNotFoundPuuid)).thenReturn(notFound);

    ResponseDto resultResponse = null;
    try {
      resultResponse = matchGameInfoService.getMatchInfoListByPuuid(responseNotFoundPuuid, 0, 30);
    } catch (Exception e) {
      log.error(e.getMessage());
    }

    assertEquals(notFound.getResultCode(), resultResponse.getResultCode());
  }

  @Test
  @DisplayName("DB에 게임 전적데이터가 존재하는 상황에서 puuid 기대값으로 전적데이터가 List로 나온다")
  void getMatchSimpleListReturnByDBTest() throws Exception {
    List<MatchGameSimpleDto> mockReturnList = new ArrayList<>();
    MatchGameSimpleDto mockSample1 = new MatchGameSimpleDto();
    mockSample1.setPuuid(responseOkPuuid);

    MatchGameSimpleDto mockSample2 = new MatchGameSimpleDto();
    mockSample2.setPuuid(responseOkPuuid);

    mockReturnList.add(mockSample1);
    mockReturnList.add(mockSample2);

    Map<String, Object> searchParam = new HashMap<>();
    searchParam.put("puuid", responseOkPuuid);
    searchParam.put("pageNum", 0);
    searchParam.put("limitNum", 30);

    when(matchGameAddInfoMapper.selectMatchSimpleList(searchParam)).thenReturn(
        (ArrayList<MatchGameSimpleDto>) mockReturnList);

    List<MatchGameSimpleDto> returnList = (List<MatchGameSimpleDto>) matchGameInfoService.getMatchInfoListByPuuid(
        responseOkPuuid, 0, 30).getResult();

    assertEquals(returnList, mockReturnList);
  }

}