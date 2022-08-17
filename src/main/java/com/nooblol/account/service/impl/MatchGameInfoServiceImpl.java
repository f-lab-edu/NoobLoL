package com.nooblol.account.service.impl;

import com.nooblol.account.dto.match.MatchDto;
import com.nooblol.account.dto.match.MatchGameInfoDto;
import com.nooblol.account.dto.match.SyncResultDto;
import com.nooblol.account.mapper.MatchGameInfoMapper;
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
import java.util.concurrent.atomic.AtomicInteger;
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
import org.springframework.web.client.RestTemplate;

/**
 * Puuid를 기반으로 Riot서버에서 최근 100게임의 MatchId List를 받아와서
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

  @Override
  public ResponseDto getMatchInfoListByPuuid(String puuid) throws Exception {
    if (StringUtils.isBlank(puuid)) {
      throw new IllegalArgumentException("PuuId가 입력되지 않았습니다.");
    }
    return syncRiotToDbDataProcess(puuid);
  }

  // TODO: 2022/08/17 현재는 동기화시에 고정적으로 최근 50경기로 지정하였으나, 최초 사용자들의 경우에는 모든데이터를 가져올 방법을 새롭게 마련해야 함.
  @Override
  public ResponseDto syncRiotToDbDataProcess(String puuid) throws Exception {
    ResponseDto getMatchListData = matchGameListService.getMatchListId(puuid);

    if (getMatchListData.getResultCode() == HttpStatus.OK.value()) {
      List<String> riotMatchIdList = (ArrayList<String>) getMatchListData.getResult();

      //존재하지 않는 매치ID리스트 획득
      List<String> notExistsMatchList = getNotExistMatchList(riotMatchIdList);
      List<MatchDto> inputMatchList = new ArrayList<>();

      //MatchId를 기반으로 Riot에서 서버에서 상세 데이터를 받아 List에 추가
      notExistsMatchList.stream().forEach(matchId -> {
        MatchDto riotData = getMatchDataByRiot(matchId);
        setMatchIdInData(riotData);
        inputMatchList.add(riotData);
      });

      int totalSize = inputMatchList.size();
      AtomicInteger successCount = new AtomicInteger();
      successCount.set(0);

      /**
       * 한꺼번에 riot과 통신작업을 진행한 이후 받아온 데이터를 일괄적으로 Insert
       * Lambda를 사용하게 될 경우 익명 클래스에서 Exception을 처리하기 위해서 Try~Catch문을 사용해야 한다.
       * Try~Catch문을 사용하면서 Exception이 증발하여 Rollback이 되지않는 이슈가 존재하여 for문으로 수정.
       */
      for (MatchDto dto : inputMatchList) {
        if (insertMatchDataByDB(dto)) {
          successCount.getAndIncrement();
        }
      }
      SyncResultDto rtnData = new SyncResultDto(totalSize, successCount.get());
      return new ResponseDto(HttpStatus.OK.value(), rtnData);
    }
    return getMatchListData;
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
    List<String> existsMatchIdList = matchGameInfoMapper.existsMatchIdListByMatch(matchIdList);

    return matchIdList.stream()
        .filter(matchId -> !existsMatchIdList.contains(matchId)).collect(Collectors.toList());
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

  @Override
  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public boolean insertMatchDataByDB(MatchDto matchDto) throws Exception {
    MatchGameInfoDto infoDto = matchDto.getInfo();
    if (infoDto == null) {
      log.error("[InsertMatchData Fail] : MatchDto Is Null");
      return false;
    }
    matchGameInfoMapper.insertMatchGameInfo(infoDto);
    matchGameInfoMapper.insertMatchGameBans(infoDto.getTeams());
    matchGameInfoMapper.insertMatchGameParticipants(infoDto);
    matchGameInfoMapper.insertMatchGameUseStatRunes(infoDto);

    Map infoMap = new HashMap();
    infoMap.put("matchId", infoDto.getMatchId());
    infoMap.put("participants", infoDto.getParticipants());
    matchGameInfoMapper.insertMatchGameUseStyleRunes(infoMap);
    return true;
  }
}
