package com.nooblol.account.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nooblol.account.dto.summoner.SummonerDto;
import com.nooblol.account.dto.summoner.SummonerHistoryDto;
import com.nooblol.account.service.SummonerHistoryService;
import com.nooblol.account.service.SummonerService;
import com.nooblol.global.dto.ResponseDto;
import com.nooblol.global.utils.DocumentSnippetsUtils;
import com.nooblol.global.utils.ResponseEnum;
import com.nooblol.global.utils.RestDocConfiguration;
import java.util.ArrayList;
import java.util.List;
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

@WebMvcTest(SummonerController.class)
@Import(RestDocConfiguration.class)
@AutoConfigureRestDocs
class SummonerControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockBean
  SummonerService summonerService;

  @MockBean
  SummonerHistoryService summonerHistoryService;

  @Test
  @DisplayName("실제 존재하는 소환사 정보를 전달 전달시, OK상태값과 해당 소환사의 정보를 획득한다.")
  void searchSummonerByName_WhenIsExistsSummoner_ThenReturnOkAndSummonerInfo() throws Exception {
    //given
    String requestSummonerName = "Sample-Riot-Summoner-Name";

    SummonerDto mockReturnDto = new SummonerDto();
    mockReturnDto.setId("Sample-Riot-Get-UID");
    mockReturnDto.setAccountId("Sample-Riot-AccountId");
    mockReturnDto.setPuuid("Sample-Riot-Puuid");
    mockReturnDto.setName(requestSummonerName);
    mockReturnDto.setProfileIconId(520);
    mockReturnDto.setRevisionDate(1659980271000L);
    mockReturnDto.setSummonerLevel(223);

    ResponseDto returnDto = ResponseEnum.OK.getResponse();
    returnDto.setResult(mockReturnDto);

    //mock
    when(summonerService.getSummonerAccointInfo(requestSummonerName)).thenReturn(returnDto);

    //when & then
    mockMvc.perform(
            MockMvcRequestBuilders
                .get("/summoner/search/name")
                .param("summonerName", requestSummonerName)
        ).andExpect(jsonPath("$.resultCode", Is.is(HttpStatus.OK.value())))
        .andDo(
            document(
                "summoner/search/name",
                requestParameters(
                    parameterWithName("summonerName").description("조회를 하고자 하는 소환사 명")
                ),
                DocumentSnippetsUtils.responseHeaders_ContentTypeApplicationJsonValue(),
                responseFields(
                    fieldWithPath("resultCode").type(JsonFieldType.NUMBER)
                        .description("실행 결과의 상태값"),
                    fieldWithPath("result").type(JsonFieldType.OBJECT).description("조회한 소환사 정보"),
                    fieldWithPath("result.id").type(JsonFieldType.STRING)
                        .description("소환사 User ID"),
                    fieldWithPath("result.accountId").type(JsonFieldType.STRING)
                        .description("소환사 계정 ID"),
                    fieldWithPath("result.puuid").type(JsonFieldType.STRING).description("PUUID"),
                    fieldWithPath("result.name").type(JsonFieldType.STRING).description("소환사명"),
                    fieldWithPath("result.profileIconId").type(JsonFieldType.NUMBER)
                        .description("현재 사용중인 프로필 아이콘 ID"),
                    fieldWithPath("result.revisionDate").type(JsonFieldType.NUMBER)
                        .description("소환사의 정보가 마지막으로 변경된 시간"),
                    fieldWithPath("result.summonerLevel").type(JsonFieldType.NUMBER)
                        .description("소환사 Level")
                )
            )
        );
  }

  @Test
  @DisplayName("소환사의 간략화된 랭크정보를 조회시, 실제 존해자는 사용자인 경우 현재의 랭크정보를 List로 반환한다")
  void searchSummonerHistoryById_WhenISExistsSummoner_ThenReturnOkAndHistoryData()
      throws Exception {
    //given
    String requestSummonerId = "Sample-Riot-Get-UID";
    boolean requestSyncValue = false;

    String responseSummonerName = "Sample-Riot-Summoner-Name";

    SummonerHistoryDto mockSample1 = new SummonerHistoryDto();
    mockSample1.setLeagueId("05fb99f4-e149-3133-a78e-821597582f9d");
    mockSample1.setQueueType("RANKED_SOLO_5x5");
    mockSample1.setTier("CHALLENGER");
    mockSample1.setRank("I");
    mockSample1.setSummonerId(requestSummonerId);
    mockSample1.setSummonerName(responseSummonerName);
    mockSample1.setLeaguePoints(858);
    mockSample1.setWins(416);
    mockSample1.setLosses(362);

    SummonerHistoryDto mockSAmple2 = new SummonerHistoryDto();
    mockSAmple2.setLeagueId("5e3ea9f9-c6d1-43ff-ac77-9fcf5a451840");
    mockSAmple2.setQueueType("RANKED_FLEX_SR");
    mockSAmple2.setTier("DIAMOND");
    mockSAmple2.setRank("I");
    mockSAmple2.setSummonerId(requestSummonerId);
    mockSAmple2.setSummonerName(responseSummonerName);
    mockSAmple2.setLeaguePoints(50);
    mockSAmple2.setWins(56);
    mockSAmple2.setLosses(50);

    List<SummonerHistoryDto> mockTestList = new ArrayList<>();
    mockTestList.add(mockSample1);
    mockTestList.add(mockSAmple2);

    ResponseDto okResponseDto = ResponseEnum.getResponseOkDto(mockTestList);

    //mock
    when(summonerHistoryService.getSummonerHistoryInfo(requestSummonerId,
        requestSyncValue)).thenReturn(okResponseDto);

    //when & then
    mockMvc.perform(
            MockMvcRequestBuilders
                .get("/summoner/history")
                .param("summonerId", requestSummonerId)
                .param("sync", String.valueOf(requestSyncValue))
        ).andExpect(jsonPath("$.resultCode", Is.is(HttpStatus.OK.value())))
        .andDo(
            document(
                "summoner/search/history",
                requestParameters(
                    parameterWithName("summonerId").description("데이터를 가져올 사용자 ID"),
                    parameterWithName("sync").description("DB의 데이터를 가져올지, Riot서버에서 데이터를 가져올지 구분값")
                ),
                DocumentSnippetsUtils.responseHeaders_ContentTypeApplicationJsonValue(),
                responseFields(
                    fieldWithPath("resultCode").type(JsonFieldType.NUMBER)
                        .description("실행 결과의 상태값"),
                    fieldWithPath("result[]").type(JsonFieldType.ARRAY)
                        .description("조회한 소환사의 간략한 랭크정보 리스트"),
                    fieldWithPath("result[].leagueId").type(JsonFieldType.STRING)
                        .description("해당 랭크의 고유 ID"),
                    fieldWithPath("result[].summonerId").type(JsonFieldType.STRING)
                        .description("소환사 UID"),
                    fieldWithPath("result[].summonerName").type(JsonFieldType.STRING)
                        .description("소환사 명"),
                    fieldWithPath("result[].queueType").type(JsonFieldType.STRING)
                        .description("랭크 종류, 솔로랭크 자유랭크"),
                    fieldWithPath("result[].tier").type(JsonFieldType.STRING)
                        .description("현재 랭크 티어"),
                    fieldWithPath("result[].rank").type(JsonFieldType.STRING)
                        .description("현재 랭크 티어의 단계"),
                    fieldWithPath("result[].leaguePoints").type(JsonFieldType.NUMBER)
                        .description("랭크타입의 랭크 점수"),
                    fieldWithPath("result[].wins").type(JsonFieldType.NUMBER).description("승리 횟수"),
                    fieldWithPath("result[].losses").type(JsonFieldType.NUMBER).description("패배 횟수")

                )
            )
        );
  }
}