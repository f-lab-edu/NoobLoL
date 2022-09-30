package com.nooblol.account.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nooblol.account.dto.summoner.SummonerHistoryDto;
import com.nooblol.account.mapper.SummonerHistoryMapper;
import com.nooblol.account.service.SummonerHistoryService;
import com.nooblol.global.config.RiotConfiguration;
import com.nooblol.global.dto.ResponseDto;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class SummonerHistoryServiceImpl implements SummonerHistoryService {

  private final Logger log = LoggerFactory.getLogger(getClass());

  private final RiotConfiguration riotConfiguration;
  private final SummonerHistoryMapper summonerHistoryMapper;

  private final ObjectMapper objectMapper;
  private final RestTemplate restTemplate;
  private final HttpHeaders initRiotHeader;

  @Override
  public ResponseDto getSummonerHistoryInfo(String summonerId, boolean sync) {
    if (StringUtils.isBlank(summonerId)) {
      throw new IllegalArgumentException("summonerId가 입력되지 않았습니다.");
    }
    return summonerHistoryProcess(summonerId, sync);
  }

  ResponseDto summonerHistoryProcess(String summonerId, boolean sync) {
    ResponseDto responseDto = null;
    if (sync) {
      List<SummonerHistoryDto> dbSummonerHistoryList =
          summonerHistoryMapper.selectSummonerHistoryById(summonerId);
      if (!dbSummonerHistoryList.isEmpty()) {
        return new ResponseDto(HttpStatus.OK.value(), dbSummonerHistoryList);
      }
    }

    responseDto = selSummonerHistoryByRiot(summonerId);
    summonerHistoryDBProcess(responseDto);
    return responseDto;
  }

  public ResponseDto selSummonerHistoryByRiot(String summonerId) {
    ResponseDto rtnDto = null;
    String url =
        riotConfiguration.getSummonerDomain()
            + riotConfiguration.getSummonerHistorySearchBySummonerIdApi()
            + summonerId;
    try {
      ResponseEntity response = getApiResponseData(url);
      rtnDto = makeResponseToDto(response);
    } catch (IOException e) {
      log.error(e.getMessage());
    } catch (Exception e) {
      log.error(e.getMessage());
    } finally {
      if (ObjectUtils.isEmpty(rtnDto)) {
        rtnDto = new ResponseDto(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND);
      }
    }
    return rtnDto;
  }

  private ResponseEntity getApiResponseData(String url) throws IOException {
    return restTemplate.exchange(
        url, HttpMethod.GET, new HttpEntity<String>(initRiotHeader), String.class
    );
  }

  private ResponseDto makeResponseToDto(ResponseEntity response) throws IOException {
    HttpStatus sameStatus = HttpStatus.valueOf(response.getStatusCode().value());
    if (sameStatus == HttpStatus.OK) {
      return new ResponseDto(sameStatus.value(), getResponseBodyToDto(response));
    }
    if (sameStatus != null) {
      return new ResponseDto(sameStatus.value(), sameStatus);
    }
    return new ResponseDto(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND);
  }

  private ArrayList<SummonerHistoryDto> getResponseBodyToDto(ResponseEntity response)
      throws IOException {
    String body = response.getBody().toString();

    return objectMapper.readValue(body, new TypeReference<ArrayList<SummonerHistoryDto>>() {
    });
  }

  private void summonerHistoryDBProcess(ResponseDto responseDto) {
    ArrayList<SummonerHistoryDto> summonerHistoryList =
        (ArrayList<SummonerHistoryDto>) responseDto.getResult();

    if (!summonerHistoryList.isEmpty()) {
      summonerHistoryList.forEach(
          summonerHistoryDto -> {
            summonerHistoryDBHandle(summonerHistoryDto);
          }
      );
    }
  }

  private void summonerHistoryDBHandle(SummonerHistoryDto summonerHistoryDto) {
    String leagueId = summonerHistoryDto.getLeagueId();
    String summonerId = summonerHistoryDto.getSummonerId();

    if (StringUtils.isBlank(leagueId) || StringUtils.isBlank(summonerId)) {
      throw new IllegalArgumentException("LeagueId or Summoner ID Is Null");
    }
    SummonerHistoryDto existDataByDB =
        summonerHistoryMapper.selectSummonerHistoryByLeagueAndId(leagueId, summonerId);

    if (!ObjectUtils.isEmpty(existDataByDB)) {
      summonerHistoryMapper.updateSummonerHistory(summonerHistoryDto);
    } else {
      summonerHistoryMapper.insertSummonerHistory(summonerHistoryDto);
    }
  }
}
