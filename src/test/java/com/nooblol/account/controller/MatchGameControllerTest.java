package com.nooblol.account.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nooblol.account.dto.match.MatchGameBansDto;
import com.nooblol.account.dto.match.MatchGameParticipantsDto;
import com.nooblol.account.dto.match.MatchGameSimpleDto;
import com.nooblol.account.dto.match.MatchUseRuneDto;
import com.nooblol.account.service.MatchGameAddInfoService;
import com.nooblol.account.service.MatchGameInfoService;
import com.nooblol.global.dto.ResponseDto;
import com.nooblol.global.utils.DocumentSnippetsUtils;
import com.nooblol.global.utils.ResponseEnum;
import com.nooblol.global.utils.RestDocConfiguration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@WebMvcTest(MatchGameController.class)
@Import(RestDocConfiguration.class)
@AutoConfigureRestDocs
class MatchGameControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockBean
  MatchGameInfoService matchGameInfoService;

  @MockBean
  MatchGameAddInfoService matchGameAddInfoService;

  @Test
  @DisplayName("puuid를 통하여 사용자의 최근 전적을 조회시, 실제 존재하는 Puuid인 경우 전적데이터를 획득한다")
  void selectMatchList_WhenIsExistsPuuid_ThenReturnMatchList() throws Exception {
    //given
    String puuid = "Sample-Puuid";
    String sync = String.valueOf(false);
    int pageNum = 1;
    int limitNum = 30;

    MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
    requestParams.add("puuid", puuid);
    requestParams.add("sync", sync);
    requestParams.add("page", String.valueOf(pageNum));
    requestParams.add("limit", String.valueOf(limitNum));

    ResponseDto responseDto = ResponseEnum.getResponseOkDto(getMatchList(puuid));
    //mock
    when(
        matchGameInfoService.syncRiotToDbByPuuidAfterGetMatchSimpleList(
            puuid, pageNum * limitNum, limitNum
        )
    ).thenReturn(responseDto);

    //when & then
    mockMvc.perform(
            MockMvcRequestBuilders
                .get("/match/list")
                .params(requestParams)
        ).andExpect(jsonPath("$.resultCode", Is.is(HttpStatus.OK.value())))
        .andDo(
            document(
                "match/list",
                requestParameters(
                    parameterWithName("puuid").description("조회를 희망하는 사용자의 puuid"),
                    parameterWithName("sync").description("Riot서버와의 동기화 여부"),
                    parameterWithName("page").description("조회를 희망하는 페이지(default = 0)"),
                    parameterWithName("limit").description("한번에 조회할 건수 (default = 30건)")
                ),
                DocumentSnippetsUtils.responseHeaders_ContentTypeApplicationJsonValue(),
                responseFields(
                    fieldWithPath("resultCode").type(JsonFieldType.NUMBER)
                        .description("실행 결과의 상태값"),
                    fieldWithPath("result[]").type(JsonFieldType.ARRAY).description("최근 전적 리스트"),
                    fieldWithPath("result[].puuid").type(JsonFieldType.STRING)
                        .description("사용자 PUUID"),
                    fieldWithPath("result[].matchId").type(JsonFieldType.STRING)
                        .description("진행된 matchId"),
                    fieldWithPath("result[].summonerId").type(JsonFieldType.STRING)
                        .description("조회한 사용자 ID"),
                    fieldWithPath("result[].summonerName").type(JsonFieldType.STRING)
                        .description("조회한 사용자의 소환사명"),
                    fieldWithPath("result[].championName").type(JsonFieldType.STRING)
                        .description("해당 게임에서 사용한 챔피언 명"),
                    fieldWithPath("result[].championId").type(JsonFieldType.NUMBER)
                        .description("해당 게임에서 사용한 챔피언의 ID"),
                    fieldWithPath("result[].role").type(JsonFieldType.STRING)
                        .description("해당 게임에서 맡은 역할"),
                    fieldWithPath("result[].lane").type(JsonFieldType.STRING)
                        .description("해당 게임에서 진행(매칭)된 라인"),
                    fieldWithPath("result[].teamId").type(JsonFieldType.NUMBER)
                        .description("게임을 진행한 진형"),
                    fieldWithPath("result[].teamPosition").type(JsonFieldType.STRING)
                        .description("팀에서 맡은 포지션"),
                    fieldWithPath("result[].win").type(JsonFieldType.BOOLEAN).description("승리 여부"),
                    fieldWithPath("result[].kills").type(JsonFieldType.NUMBER).description("킬 수"),
                    fieldWithPath("result[].deaths").type(JsonFieldType.NUMBER)
                        .description("죽은 횟 수"),
                    fieldWithPath("result[].assists").type(JsonFieldType.NUMBER)
                        .description("어시스트 수"),
                    fieldWithPath("result[].summoner1Id").type(JsonFieldType.NUMBER)
                        .description("해당게임에서 사용한 소환사 스펠"),
                    fieldWithPath("result[].summoner2Id").type(JsonFieldType.NUMBER)
                        .description("해당게임에서 사용한 소환사 스펠"),
                    fieldWithPath("result[].item0").type(JsonFieldType.NUMBER)
                        .description("해당 게임에서 구매한 Item Id"),
                    fieldWithPath("result[].item1").type(JsonFieldType.NUMBER)
                        .description("해당 게임에서 구매한 Item Id"),
                    fieldWithPath("result[].item2").type(JsonFieldType.NUMBER)
                        .description("해당 게임에서 구매한 Item Id"),
                    fieldWithPath("result[].item3").type(JsonFieldType.NUMBER)
                        .description("해당 게임에서 구매한 Item Id"),
                    fieldWithPath("result[].item4").type(JsonFieldType.NUMBER)
                        .description("해당 게임에서 구매한 Item Id"),
                    fieldWithPath("result[].item5").type(JsonFieldType.NUMBER)
                        .description("해당 게임에서 구매한 Item Id"),
                    fieldWithPath("result[].item6").type(JsonFieldType.NUMBER)
                        .description("해당 게임에서 구매한 Item Id"),
                    fieldWithPath("result[].gameCreation").type(JsonFieldType.NUMBER)
                        .description("게임이 생성된 시간"),
                    fieldWithPath("result[].gameDuration").type(JsonFieldType.NUMBER)
                        .description("게임이 진행된 시간"),
                    fieldWithPath("result[].queueId").type(JsonFieldType.NUMBER)
                        .description("해당 게임이 진행된 모드 타입ID"),
                    fieldWithPath("result[].gameMode").type(JsonFieldType.STRING)
                        .description("해당 게임이 진행된 모드"),
                    fieldWithPath("result[].participants[]").type(JsonFieldType.ARRAY)
                        .description("해당 게임에 본인을 포함한 참가자 정보"),
                    fieldWithPath("result[].participants[].puuid").type(JsonFieldType.STRING)
                        .description("게임 참가자 PUUID"),
                    fieldWithPath("result[].participants[].matchId").type(JsonFieldType.STRING)
                        .description("참가게임 MatchId"),
                    fieldWithPath("result[].participants[].summonerId").type(JsonFieldType.STRING)
                        .description("참가자 ID"),
                    fieldWithPath("result[].participants[].summonerName").type(JsonFieldType.STRING)
                        .description("참가자 소환사 명"),
                    fieldWithPath("result[].participants[].championName").type(JsonFieldType.STRING)
                        .description("사용한 Champion Name"),
                    fieldWithPath("result[].participants[].championId").type(JsonFieldType.NUMBER)
                        .description("사용한 Champion Id"),
                    fieldWithPath("result[].participants[].teamId").type(JsonFieldType.NUMBER)
                        .description("소속된 진영"),
                    fieldWithPath("result[].participants[].win").type(JsonFieldType.BOOLEAN)
                        .description("승리 여부"),
                    fieldWithPath("result[].participants[].kills").ignored(),
                    fieldWithPath("result[].participants[].deaths").ignored(),
                    fieldWithPath("result[].participants[].assists").ignored(),
                    fieldWithPath("result[].participants[].summoner1Id").ignored(),
                    fieldWithPath("result[].participants[].summoner2Id").ignored(),
                    fieldWithPath("result[].participants[].item0").ignored(),
                    fieldWithPath("result[].participants[].item1").ignored(),
                    fieldWithPath("result[].participants[].item2").ignored(),
                    fieldWithPath("result[].participants[].item3").ignored(),
                    fieldWithPath("result[].participants[].item4").ignored(),
                    fieldWithPath("result[].participants[].item5").ignored(),
                    fieldWithPath("result[].participants[].item6").ignored(),
                    fieldWithPath("result[].participants[].gameCreation").ignored(),
                    fieldWithPath("result[].participants[].gameDuration").ignored(),
                    fieldWithPath("result[].participants[].queueId").ignored()
                )
            )
        );
  }

  @Test
  @DisplayName("MatchId를 파라미터로 제공해서 해당 게임의 챔피언 벤 리스트를 조회시, 해당 MatchId가 DB에 존재하는 경우 리스트를 획득한다.")
  void getMatchBanList_WhenIsExistsMatchIdInDB_ThenReturnBanList() throws Exception {
    //given
    String matchId = "SampleMatchId";

    //mock
    when(matchGameAddInfoService.getMatchBanList(matchId)).thenReturn(getBanList());

    //when & then
    mockMvc.perform(MockMvcRequestBuilders
            .get("/match/ban")
            .param("matchId", matchId)
        )
        .andExpect(jsonPath("$.resultCode", Is.is(HttpStatus.OK.value())))
        .andDo(
            document(
                "match/ban",
                requestParameters(
                    parameterWithName("matchId").description("게임이 진행된 ID")
                ),
                DocumentSnippetsUtils.responseHeaders_ContentTypeApplicationJsonValue(),
                responseFields(
                    fieldWithPath("resultCode").type(JsonFieldType.NUMBER)
                        .description("실행 결과의 상태값"),
                    fieldWithPath("result[]").type(JsonFieldType.ARRAY)
                        .description("해당 게임의 BanList"),
                    fieldWithPath("result[].championId").type(JsonFieldType.NUMBER)
                        .description("Ban이 된 ChampionId"),
                    fieldWithPath("result[].pickTurn").type(JsonFieldType.NUMBER)
                        .description("순서")
                )
            )
        );

  }

  @Test
  @DisplayName("게임 참가자 조회시, 제공받은 MatchId에 대한 참가자 데이터가 존재하는 경우 해당 게임의 참가자 정보를 Return한다")
  void getMatchAllParticipants_WhenIsExistsMatchIdInDB_ThenReturnParticipantsList()
      throws Exception {
    //given
    String matchId = "Sample-MatchId";

    //mock
    when(matchGameAddInfoService.getMatchAllParticipantsList(matchId)).thenReturn(
        getMatchDetailParticipantsInfoList());

    //when & then
    mockMvc.perform(
            MockMvcRequestBuilders
                .get("/match/participants")
                .param("matchId", matchId)
        ).andExpect(jsonPath("$.resultCode", Is.is(HttpStatus.OK.value())))
        .andDo(
            document(
                "match/participants",
                requestParameters(
                    parameterWithName("matchId").description("게임이 진행된 ID")
                ),
                DocumentSnippetsUtils.responseHeaders_ContentTypeApplicationJsonValue(),
                responseFields(
                    fieldWithPath("resultCode").type(JsonFieldType.NUMBER)
                        .description("실행 결과의 상태값"),
                    fieldWithPath("result[]").type(JsonFieldType.ARRAY)
                        .description("해당 게임의 참가자 리스트"),
                    fieldWithPath("result[].puuid").type(JsonFieldType.STRING)
                        .description("사용자 PUUID"),
                    fieldWithPath("result[].summonerId").type(JsonFieldType.STRING)
                        .description("조회한 사용자 ID"),
                    fieldWithPath("result[].summonerName").type(JsonFieldType.STRING)
                        .description("조회한 사용자의 소환사명"),
                    fieldWithPath("result[].championName").type(JsonFieldType.STRING)
                        .description("해당 게임에서 사용한 챔피언 명"),
                    fieldWithPath("result[].championId").type(JsonFieldType.NUMBER)
                        .description("해당 게임에서 사용한 챔피언의 ID"),
                    fieldWithPath("result[].championLevel").type(JsonFieldType.NUMBER)
                        .description("해당 게임에서 달성한 챔피언 레벨"),
                    fieldWithPath("result[].role").type(JsonFieldType.STRING)
                        .description("해당 게임에서 맡은 역할"),
                    fieldWithPath("result[].lane").type(JsonFieldType.STRING)
                        .description("해당 게임에서 진행(매칭)된 라인"),
                    fieldWithPath("result[].teamId").type(JsonFieldType.NUMBER)
                        .description("게임을 진행한 진형"),
                    fieldWithPath("result[].teamPosition").type(JsonFieldType.STRING)
                        .description("팀에서 맡은 포지션"),
                    fieldWithPath("result[].win").type(JsonFieldType.BOOLEAN).description("승리 여부"),
                    fieldWithPath("result[].kills").type(JsonFieldType.NUMBER).description("킬 수"),
                    fieldWithPath("result[].deaths").type(JsonFieldType.NUMBER)
                        .description("죽은 횟 수"),
                    fieldWithPath("result[].assists").type(JsonFieldType.NUMBER)
                        .description("어시스트 수"),
                    fieldWithPath("result[].summoner1Id").type(JsonFieldType.NUMBER)
                        .description("해당게임에서 사용한 소환사 스펠"),
                    fieldWithPath("result[].summoner2Id").type(JsonFieldType.NUMBER)
                        .description("해당게임에서 사용한 소환사 스펠"),
                    fieldWithPath("result[].summoner1Casts").type(JsonFieldType.NUMBER)
                        .description("해당게임에서 사용한 소환사 스펠 사용 횟수"),
                    fieldWithPath("result[].summoner2Casts").type(JsonFieldType.NUMBER)
                        .description("해당게임에서 사용한 소환사 스펠 사용 횟수"),
                    fieldWithPath("result[].item0").type(JsonFieldType.NUMBER)
                        .description("해당 게임에서 구매한 Item Id"),
                    fieldWithPath("result[].item1").type(JsonFieldType.NUMBER)
                        .description("해당 게임에서 구매한 Item Id"),
                    fieldWithPath("result[].item2").type(JsonFieldType.NUMBER)
                        .description("해당 게임에서 구매한 Item Id"),
                    fieldWithPath("result[].item3").type(JsonFieldType.NUMBER)
                        .description("해당 게임에서 구매한 Item Id"),
                    fieldWithPath("result[].item4").type(JsonFieldType.NUMBER)
                        .description("해당 게임에서 구매한 Item Id"),
                    fieldWithPath("result[].item5").type(JsonFieldType.NUMBER)
                        .description("해당 게임에서 구매한 Item Id"),
                    fieldWithPath("result[].item6").type(JsonFieldType.NUMBER)
                        .description("해당 게임에서 구매한 Item Id")
                )
            )
        );
  }

  @Test
  @DisplayName("해당 게임에 조회하고자 하는 사용자가 존재하는 경우 해당 판에서 사용한 룬정보를 획득한다")
  void getMatchUseRune_WhenIsExistsMatch_ThenReturnUseRuneInfo() throws Exception {
    //given
    String matchId = "KR_6064599598";
    String puuid = "Test_Puuid";
    List<MatchUseRuneDto> mockReturnList = getMatchUseRunList(puuid, matchId);

    ResponseDto mockReturnDto = ResponseEnum.getResponseOkDto(mockReturnList);

    //mock
    when(matchGameAddInfoService.getMatchUseRunList(matchId, puuid)).thenReturn(mockReturnList);

    mockMvc.perform(
            MockMvcRequestBuilders
                .get("/match/rune")
                .param("matchId", matchId)
                .param("puuid", puuid)
        )
        .andExpect(jsonPath("$.resultCode", Is.is(HttpStatus.OK.value())))
        .andDo(
            document(
                "match/rune",
                requestParameters(
                    parameterWithName("matchId").description("게임이 진행된 ID"),
                    parameterWithName("puuid").description("matchId에서 진행된 게임중 조회하고자 하는 사용자의 puuid")
                ),
                DocumentSnippetsUtils.responseHeaders_ContentTypeApplicationJsonValue(),
                responseFields(
                    fieldWithPath("resultCode").type(JsonFieldType.NUMBER)
                        .description("실행 결과의 상태값"),
                    fieldWithPath("result[]").type(JsonFieldType.ARRAY).description("사용자 정보 리스트"),
                    fieldWithPath("result[].puuid").type(JsonFieldType.STRING)
                        .description("사용자 puuid"),
                    fieldWithPath("result[].matchId").type(JsonFieldType.STRING)
                        .description("진행된 matchId"),
                    fieldWithPath("result[].type").type(JsonFieldType.STRING)
                        .description("선택된 Rune Type(스탯, 메인룬, 서브룬)"),
                    fieldWithPath("result[].sortNo").type(JsonFieldType.NUMBER)
                        .description("룬의 입력 순서"),
                    fieldWithPath("result[].perk").type(JsonFieldType.NUMBER)
                        .description("사용된 룬 ID")
                )
            )
        );
  }

  private List<MatchGameBansDto> getBanList() {
    List<MatchGameBansDto> banList = new ArrayList<>();
    banList.add(new MatchGameBansDto(238, 1));
    banList.add(new MatchGameBansDto(30, 2));
    banList.add(new MatchGameBansDto(32, 3));
    banList.add(new MatchGameBansDto(43, 4));
    banList.add(new MatchGameBansDto(114, 5));
    banList.add(new MatchGameBansDto(203, 6));
    banList.add(new MatchGameBansDto(120, 7));
    banList.add(new MatchGameBansDto(266, 8));
    banList.add(new MatchGameBansDto(68, 9));
    banList.add(new MatchGameBansDto(57, 10));

    return banList;
  }

  private List<MatchGameSimpleDto> getMatchList(String puuid) {
    List<MatchGameSimpleDto> matchList = new ArrayList<>();
    matchList.add(
        new MatchGameSimpleDto().builder()
            .puuid(puuid)
            .matchId("1")
            .role("CARRY").lane("BOTTOM").teamPosition("BOTTOM")
            .summonerId("Sample-Summoner-Id-By-" + puuid)
            .summonerName("Sample-Summoner-Name-" + puuid)
            .championId(37).championName("Samle-Champion-Name")
            .teamId(100).win(true)
            .kills(11).deaths(5).assists(26)
            .summoner1Id(4).summoner2Id(7)
            .item0(3157).item1(6653).item2(3020).item3(4637).item4(1056).item5(3116).item6(3363)
            .gameCreation(1664379174394L).gameDuration(2272)
            .queueId(430).gameMode("CLASSIC")
            .participants(getSimpleParticipantsList(puuid))
            .build()
    );

    matchList.add(
        new MatchGameSimpleDto().builder()
            .puuid(puuid)
            .matchId("2")
            .role("CARRY").lane("BOTTOM").teamPosition("BOTTOM")
            .summonerId("Sample-Summoner-Id-By-" + puuid)
            .summonerName("Sample-Summoner-Name-" + puuid)
            .championId(37).championName("Samle-Champion-Name")
            .teamId(100).win(true)
            .kills(11).deaths(5).assists(26)
            .summoner1Id(4).summoner2Id(7)
            .item0(3157).item1(6653).item2(3020).item3(4637).item4(1056).item5(3116).item6(3363)
            .gameCreation(1664379174394L).gameDuration(2272)
            .queueId(430).gameMode("CLASSIC")
            .participants(getSimpleParticipantsList(puuid))
            .build()
    );

    return matchList;
  }

  private List<MatchGameSimpleDto> getSimpleParticipantsList(String puuid) {
    List<MatchGameSimpleDto> participantsList = new ArrayList<>();

    participantsList.add(getSimpleParticipant(puuid, 100));
    for (int i = 1; i < 10; i++) {
      participantsList.add(getSimpleParticipant("Puuid" + i, i < 5 ? 100 : 200));
    }

    return participantsList;
  }

  private MatchGameSimpleDto getSimpleParticipant(String puuid, int teamId) {
    return new MatchGameSimpleDto().builder()
        .puuid(puuid)
        .matchId("sample-MatchId")
        .summonerId("Sample-Summoner-Id-By-" + puuid)
        .summonerName("Sample-Summoner-Name-" + puuid)
        .championId(37)
        .championName("Samle-Champion-Name")
        .teamId(teamId)
        .win(teamId == 100 ? true : false)
        .build();
  }

  private List<MatchUseRuneDto> getMatchUseRunList(String puuid, String matchId) {
    ArrayList<MatchUseRuneDto> mockReturnList = new ArrayList<>();
    mockReturnList.add(
        new MatchUseRuneDto().builder()
            .puuid(puuid).type("stat_offense").sortNo(1).matchId(matchId).perk(5003)
            .build());
    mockReturnList.add(
        new MatchUseRuneDto().builder()
            .puuid(puuid).type("stat_flex").sortNo(2).matchId(matchId).perk(5008)
            .build()
    );
    mockReturnList.add(
        new MatchUseRuneDto().builder()
            .puuid(puuid).type("stat_defense").sortNo(3).matchId(matchId).perk(5008)
            .build()
    );

    mockReturnList.add(
        new MatchUseRuneDto().builder()
            .puuid(puuid).type("primaryStyle_8400").sortNo(1).matchId(matchId).perk(8437)
            .build()
    );
    mockReturnList.add(
        new MatchUseRuneDto().builder()
            .puuid(puuid).type("primaryStyle_8400").sortNo(2).matchId(matchId).perk(8446)
            .build()
    );
    mockReturnList.add(
        new MatchUseRuneDto().builder()
            .puuid(puuid).type("primaryStyle_8400").sortNo(3).matchId(matchId).perk(8473)
            .build()
    );
    mockReturnList.add(
        new MatchUseRuneDto().builder()
            .puuid(puuid).type("primaryStyle_8400").sortNo(4).matchId(matchId).perk(8451)
            .build()
    );
    mockReturnList.add(
        new MatchUseRuneDto().builder()
            .puuid(puuid).type("subStyle_8300").sortNo(1).matchId(matchId).perk(8345)
            .build()
    );
    mockReturnList.add(
        new MatchUseRuneDto().builder()
            .puuid(puuid).type("subStyle_8300").sortNo(2).matchId(matchId).perk(8352)
            .build()
    );

    return mockReturnList;
  }

  private List<MatchGameParticipantsDto> getMatchDetailParticipantsInfoList() {
    List<MatchGameParticipantsDto> participantsList = new ArrayList<MatchGameParticipantsDto>();

    for (int i = 1; i <= 10; i++) {
      participantsList.add(getMatchDetailParticipantInfo(i));
    }

    return participantsList;
  }

  private MatchGameParticipantsDto getMatchDetailParticipantInfo(int i) {
    return MatchGameParticipantsDto.builder()
        .puuid("Sample Puuid " + i)
        .summonerId("Sample SummonerId " + i).summonerName("Sample Summoner Name " + i)
        .championId(122).championName("Darius").championLevel(0)
        .role("SUPPOERT").lane("NONE")
        .teamId(i <= 5 ? 100 : 200).teamPosition("TOP")
        .win(i <= 5 ? true : false)
        .kills(2).deaths(4).assists(0)
        .summoner1Id(6).summoner1Casts(3)
        .summoner2Id(8).summoner2Casts(7)
        .item0(1284).item1(6631).item2(2022).item3(4958).item4(2948).item5(8289).item6(7746)
        .build();
  }


}