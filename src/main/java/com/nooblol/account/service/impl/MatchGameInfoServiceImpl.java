package com.nooblol.account.service.impl;

import com.nooblol.account.dto.match.MatchDto;
import com.nooblol.account.dto.match.MatchGameInfoDto;
import com.nooblol.account.dto.match.MatchGameSimpleDto;
import com.nooblol.account.dto.match.SyncResultDto;
import com.nooblol.account.mapper.MatchGameInfoMapper;
import com.nooblol.account.mapper.MatchGameAddInfoMapper;
import com.nooblol.account.service.MatchGameInfoService;
import com.nooblol.account.service.MatchGameListService;
import com.nooblol.global.config.RiotConfiguration;
import com.nooblol.global.dto.ResponseDto;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

/**
 * Puuid를 기반으로 Riot서버에서 최근 50게임의 MatchId List를 받아와서
 * <p>
 * DB에 존재하지 않는 게임만 데이터를 다시 받아와 DB에 삽입한다.
 * <p>
 */

@Service
@RequiredArgsConstructor
@Transactional
public class MatchGameInfoServiceImpl implements MatchGameInfoService {

  private final Logger log = LoggerFactory.getLogger(getClass());
  private final RiotConfiguration riotConfiguration;
  private final RestTemplate restTemplate;
  private final MatchGameListService matchGameListService;
  private final HttpHeaders initRiotHeader;

  private final MatchGameInfoMapper matchGameInfoMapper;
  private final MatchGameAddInfoMapper matchGameAddInfoMapper;

  /**
   * DB의 게임 전적에 대하여 바로 Return 을 진행 하나, 데이터가 존재하지 않는 경우에는 동기화를 한 이후에 조회를 진행함)
   */

  @Override
  public ResponseDto getMatchInfoListByPuuid(String puuid, int pageNum, int limitNum)
      throws Exception {
    List<MatchGameSimpleDto> matchResult = selectMatchSimpleListByPuuidInDB(puuid, pageNum,
        limitNum);

    if (!ObjectUtils.isEmpty(matchResult)) {
      return new ResponseDto(HttpStatus.OK.value(), matchResult);
    }

    return syncRiotToDbByPuuidAfterGetMatchSimpleList(puuid, pageNum, limitNum);
  }

  @Override
  @Transactional(readOnly = true)
  public List<MatchGameSimpleDto> selectMatchSimpleListByPuuidInDB(String puuid, int pageNum,
      int limitNum) {
    Map<String, Object> searchParam = new HashMap<>();
    searchParam.put("puuid", puuid);
    searchParam.put("pageNum", pageNum);
    searchParam.put("limitNum", limitNum);

    List<MatchGameSimpleDto> selectMatchSimpleList =
        matchGameAddInfoMapper.selectMatchSimpleList(searchParam);

    if (ObjectUtils.isEmpty(selectMatchSimpleList)) {
      return selectMatchSimpleList;
    }

    selectMatchSimpleList.stream().forEach(matchGameSimpleDto -> {
      String matchId = matchGameSimpleDto.getMatchId();
      List<MatchGameSimpleDto> participantsList =
          matchGameAddInfoMapper.selectMatchSimpleParticipantsList(matchId);
      matchGameSimpleDto.setParticipants(participantsList);
    });

    return selectMatchSimpleList;
  }

  // Sync를 진행한 이후 게임 결과에 대한 Return
  @Override
  public ResponseDto syncRiotToDbByPuuidAfterGetMatchSimpleList(String puuid, int pageNum,
      int limitNum) throws Exception {
    ResponseDto syncResult = syncRiotToDbDataProcess(puuid);
    if (syncResult.getResultCode() == HttpStatus.OK.value()) {
      return new ResponseDto(HttpStatus.OK.value(),
          selectMatchSimpleListByPuuidInDB(puuid, pageNum, limitNum));
    }
    return syncResult;
  }

  // TODO: 2022/08/17 현재는 동기화시에 고정적으로 최근 50경기로 지정하였으나, 최초 사용자들의 경우에는 모든데이터를 가져올 방법을 새롭게 마련해야 함.
  @Override
  public ResponseDto syncRiotToDbDataProcess(String puuid) throws Exception {
    ResponseDto getMatchListData = matchGameListService.getMatchListId(puuid);

    if (getMatchListData.getResultCode() != HttpStatus.OK.value()) {
      return getMatchListData;
    }

    List<String> riotMatchIdList = (ArrayList<String>) getMatchListData.getResult();
    if (ObjectUtils.isEmpty(riotMatchIdList)) {
      return haveNoSyncDataReturnOk();
    }

    //존재하지 않는 매치ID리스트 획득
    List<String> notExistsMatchList = getNotExistMatchList(riotMatchIdList);
    List<MatchDto> inputMatchList = new ArrayList<>();

    if (ObjectUtils.isEmpty(notExistsMatchList)) {
      return haveNoSyncDataReturnOk();
    }

    //MatchId를 기반으로 Riot에서 서버에서 상세 데이터를 받아 List에 추가
    notExistsMatchList.stream().forEach(matchId -> {
      Optional riotData = Optional.ofNullable(getMatchDataByRiot(matchId));
      riotData.ifPresent(matchDto -> {
        setMatchIdInData((MatchDto) matchDto);
        inputMatchList.add((MatchDto) matchDto);
      });
    });

    int totalSize = inputMatchList.size();
    int successCount = 0;

    /**
     * 한꺼번에 riot과 통신작업을 진행한 이후 받아온 데이터를 일괄적으로 Insert
     * inputMatchList를 Lambda로 사용하게 될 경우 익명 클래스에서 Exception을 처리하기 위해서 Try~Catch문을 사용해야 한다.
     * Try~Catch문을 사용하면서 Exception이 증발하여 Rollback이 되지않는 이슈가 존재하여 for문으로 수정.
     */
    for (MatchDto dto : inputMatchList) {
      if (insertMatchDataByDB(dto)) {
        successCount++;
      }
    }
    log.info(
        "DB Insert Success PuuId : " + puuid + ", totalCount : " + totalSize + ", successCount : "
            + successCount);
    SyncResultDto rtnData = new SyncResultDto(totalSize, successCount);
    return new ResponseDto(HttpStatus.OK.value(), rtnData);
  }

  /**
   * Riot에서 받아온 Data를 DB에 Insert하기 전에 필요한 하위 Dto의 value 세팅
   *
   * @param riotData
   */
  private void setMatchIdInData(MatchDto riotData) {
    String dataMatchId = riotData.getMetadata().getMatchId();
    String dataVersion = riotData.getMetadata().getDataVersion();

    riotData.getInfo().setMatchId(dataMatchId);
    riotData.getInfo().setDataVersion(dataVersion);
    riotData.getInfo().getTeams().stream().forEach(teamDto -> {
      teamDto.setMatchId(dataMatchId);
    });
  }

  @Override
  @Transactional(readOnly = true)
  public List<String> getNotExistMatchList(List<String> matchIdList) {
    StringBuilder matchIdListBuilder = new StringBuilder();

    //matchIdList가 존재하지 않는 경우에는 이미 Return처리가 되기 때문에 실행되는 경우 무조건 element가 존재한다
    matchIdList.stream().forEach(matchId -> {
      matchIdListBuilder.append("'" + matchId + "',");
    });
    matchIdListBuilder.deleteCharAt(matchIdListBuilder.length() - 1);

    List<String> existsMatchIdList =
        matchGameInfoMapper.existsMatchIdListByMatch(matchIdListBuilder.toString());

    return matchIdList.stream()
        .filter(matchId -> !existsMatchIdList.contains(matchId))
        .collect(Collectors.toList());
  }

  /**
   * MatchId를 기반으로 Riot과 통신, 받아온 데이터를 MatchDto르 변환
   *
   * @param matchId
   * @return
   */
  @Override
  public MatchDto getMatchDataByRiot(String matchId) {
    String uri = getMakeUri(matchId);

    ResponseEntity response = responseResult(uri);
    if (response != null) {
      return (MatchDto) response.getBody();
    }
    return null;
  }

  private ResponseEntity responseResult(String uri) {
    try {
      return restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<String>(initRiotHeader),
          MatchDto.class);
    } catch (Exception e) {
      log.error("Riot Connect Error : " + e.getMessage());
      return null;
    }
  }

  @Override
  public String getMakeUri(String matchId) {
    String strUri = riotConfiguration.getMatchDomain() + getApiReplaceByMatchId(matchId);
    URI uri;
    try {
      uri = new URIBuilder(strUri)
          .build();
      return uri.toString();
    } catch (URISyntaxException e) {
      log.error("URI Build Error : " + e.getMessage());
    }
    return null;
  }

  @Override
  public String getApiReplaceByMatchId(String matchId) {
    if (StringUtils.isBlank(matchId)) {
      return null;
    }
    return riotConfiguration.getMatchGameInfoByMatchId().replaceAll("\\{matchId\\}", matchId);
  }

  /**
   * 해당 트랜잭션은 모두 같이 처리되거나, 혹은 한개라도 실패할 경우 모두 Rollback이 되고 다음으로 Match를 Insert하는 것으로 넘어가게 된다.
   *
   * @param matchDto
   * @return
   * @throws Exception
   */
  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public boolean insertMatchDataByDB(MatchDto matchDto) throws Exception {
    if (matchDto.getInfo() == null) {
      log.error("[InsertMatchData Fail] : MatchDto Is Null");
      return false;
    }
    MatchGameInfoDto infoDto = matchDto.getInfo();

    try {
      matchGameInfoMapper.insertMatchGameInfo(infoDto);
      matchGameInfoMapper.insertMatchGameBans(infoDto.getTeams());
      matchGameInfoMapper.insertMatchGameParticipants(infoDto);
      matchGameInfoMapper.insertMatchGameUseStatRunes(getStatRunseMap(infoDto));

      matchGameInfoMapper.insertMatchGameUseStyleRunes(getStyleRunesMap(infoDto));
    } catch (Exception e) {
      return false;
    }
    return true;
  }

  private ResponseDto haveNoSyncDataReturnOk() {
    SyncResultDto rtnData = new SyncResultDto(0, 0);
    return new ResponseDto(HttpStatus.OK.value(), rtnData);
  }

  /**
   * MATCH_GAME_RUNES테이블로 사용한 스탯 룬을 Insert를 하기 위하여 필요한 ParameterMap Return
   *
   * @param infoDto
   * @return
   */
  private Map<String, Object> getStatRunseMap(MatchGameInfoDto infoDto) {
    Map<String, Object> runesMap = new HashMap<>();
    runesMap.put("offenseType", "stat_offense");
    runesMap.put("offenseSortNo", 1);

    runesMap.put("flexType", "stat_flex");
    runesMap.put("flexSortNo", 2);

    runesMap.put("defenseType", "stat_defense");
    runesMap.put("defenseSortNo", 3);

    runesMap.put("list", infoDto);
    return runesMap;
  }

  /**
   * MATCH_GAME_RUNES테이블로 사용한 스타일룬을 Insert를 하기 위하여 필요한 ParameterMap Return
   *
   * @param infoDto
   * @return
   */
  private Map<String, Object> getStyleRunesMap(MatchGameInfoDto infoDto) {
    Map<String, Object> styleRunesMap = new HashMap();
    styleRunesMap.put("matchId", infoDto.getMatchId());
    styleRunesMap.put("participants", infoDto.getParticipants());

    return styleRunesMap;
  }
}
