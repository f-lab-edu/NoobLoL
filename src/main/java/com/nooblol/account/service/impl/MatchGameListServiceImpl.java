package com.nooblol.account.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nooblol.account.service.MatchGameListService;
import com.nooblol.global.config.RiotConfiguration;
import com.nooblol.global.dto.ResponseDto;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
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
import org.springframework.web.client.RestTemplate;

/**
 * https://developer.riotgames.com/apis#match-v5/GET_getMatchIdsByPUUID
 * /lol/match/v5/matches/by-puuid/{puuid}/ids API사용
 */

@Service
@RequiredArgsConstructor
public class MatchGameListServiceImpl implements MatchGameListService {

  private final Logger log = LoggerFactory.getLogger(getClass());
  private final RiotConfiguration riotConfiguration;
  private final ObjectMapper objectMapper;
  private final RestTemplate restTemplate;
  private final HttpHeaders initRiotHeader;

  @Override
  public ResponseDto getMatchListId(String puuid) {
    if (StringUtils.isBlank(puuid)) {
      throw new IllegalArgumentException("PuuId가 입력되지 않았습니다.");
    }
    return getMatchListIdProcessByRiot(puuid);
  }

  /**
   * Riot에서 Puuid를 기반으로 MatchId List를 가져오는 총괄적인 역할을 한다 Riot과 통신이 실패하는 경우에는 무조건 Not_Found를 Return
   */
  @Override
  public ResponseDto getMatchListIdProcessByRiot(String puuid) {
    String uri = getMakeUri(puuid);
    if (!StringUtils.isBlank(uri)) {
      ResponseEntity response = responseResult(uri);
      if (response != null) {
        return makeResponseToList(response);
      }
    }

    return new ResponseDto(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND);
  }

  private ResponseDto makeResponseToList(ResponseEntity response) {
    HttpStatus sameStatus = HttpStatus.valueOf(response.getStatusCode().value());
    if (sameStatus == HttpStatus.OK) {
      return new ResponseDto(sameStatus.value(), getResponseBodyToList(response));
    }
    if (sameStatus != null) {
      return new ResponseDto(sameStatus.value(), sameStatus);
    }
    return new ResponseDto(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND);
  }

  /**
   * MatchId는 가장 최근 MatchId가 최상단에 오며, 순차적으로 가져와야 하여 ArrayList를 반환한다
   *
   * @param response
   * @return
   */
  private ArrayList<String> getResponseBodyToList(ResponseEntity response) {
    try {
      String body = response.getBody().toString();
      return objectMapper.readValue(body, new TypeReference<ArrayList<String>>() {
      });
    } catch (JsonProcessingException e) {
      log.error("Json Parse Error : " + e.getMessage());
      return null;
    }
  }

  @Override
  public String getApiReplaceByPuuid(String puuid) {
    if (StringUtils.isBlank(puuid)) {
      return null;
    }
    return riotConfiguration.getMatchListSearchByPuuid().replaceAll("\\{puuid\\}", puuid);
  }

  /**
   * Start = 최근 몇경기부터 가져올지에 대한 항목 Count = 몇경기의 MatchId를 가져올지 조절, Max = 100
   *
   * @param puuid
   * @return
   */
  private String getMakeUri(String puuid) {
    String strUri = riotConfiguration.getMatchDomain() + getApiReplaceByPuuid(puuid);
    URI uri;
    try {
      uri = new URIBuilder(strUri)
          .addParameter("start", String.valueOf(0))
          .addParameter("count", String.valueOf(50))
          .build();
      return uri.toString();
    } catch (URISyntaxException e) {
      log.error("URI Build Error : " + e.getMessage());
    }
    return null;
  }

  private ResponseEntity responseResult(String uri) {
    try {
      return restTemplate.exchange(
          uri, HttpMethod.GET, new HttpEntity<String>(initRiotHeader), String.class
      );
    } catch (Exception e) { //예외 상황이 발생한 경우 null을 Return하여 Not_Found를 타도록 함
      log.error("Riot Connect Error : " + e.getMessage());
      return null;
    }
  }
}
