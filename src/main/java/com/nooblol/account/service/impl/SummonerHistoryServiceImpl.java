package com.nooblol.account.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nooblol.account.dto.SummonerHistoryDto;
import com.nooblol.account.mapper.SummonerHistoryMapper;
import com.nooblol.account.service.SummonerHistoryService;
import com.nooblol.global.config.RiotConfiguration;
import com.nooblol.global.dto.ResponseDto;
import com.nooblol.global.utils.CommonUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SummonerHistoryServiceImpl implements SummonerHistoryService {

  private final Logger log = LoggerFactory.getLogger(getClass());

  private final RiotConfiguration riotConfiguration;
  private final SummonerHistoryMapper summonerHistoryMapper;

  private final ObjectMapper objectMapper;

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
        riotConfiguration.getDomain() + riotConfiguration.getSummonerHistorySearchBySummonerIdApi()
            + summonerId;

    try {
      HttpResponse response = getApiResponseData(url);
      rtnDto = makeResponseToDto(response);
    } catch (IOException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (CommonUtils.objectIsNull(rtnDto)) {
        rtnDto = new ResponseDto(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND);
      }
    }
    return rtnDto;
  }

  private HttpResponse getApiResponseData(String url) throws IOException {
    HttpClient client = HttpClientBuilder.create().build();
    HttpGet getRequest = new HttpGet(url);
    getRequest.addHeader("X-Riot-Token", riotConfiguration.getApiKey());
    return client.execute(getRequest);
  }

  private ResponseDto makeResponseToDto(HttpResponse response) throws IOException {
    HttpStatus sameStatus = HttpStatus.valueOf(response.getStatusLine().getStatusCode());
    if (sameStatus == HttpStatus.OK) {
      return new ResponseDto(response.getStatusLine().getStatusCode(),
          getResponseBodyToDto(response));
    }
    if (sameStatus != null) {
      return new ResponseDto(sameStatus.value(), sameStatus);
    }
    return new ResponseDto(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND);
  }

  private ArrayList<SummonerHistoryDto> getResponseBodyToDto(HttpResponse response)
      throws IOException {
    ResponseHandler<String> handler = new BasicResponseHandler();
    String body = handler.handleResponse(response);

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
    if (StringUtils.isBlank(summonerHistoryDto.getSummonerId()) ||
        StringUtils.isBlank(summonerHistoryDto.getSummonerId())) {
      throw new IllegalArgumentException("LeagueId or Summoner ID Is Null");
    }
    SummonerHistoryDto existDataByDB =
        summonerHistoryMapper.selectSummonerHistoryByLeagueAndId(summonerHistoryDto);

    if (CommonUtils.objectIsNotNull(existDataByDB)) {
      summonerHistoryMapper.updateSummonerHistory(summonerHistoryDto);
    } else {
      summonerHistoryMapper.insertSummonerHistory(summonerHistoryDto);
    }
  }
}
