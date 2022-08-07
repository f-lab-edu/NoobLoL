package com.nooblol.account.service.impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.nooblol.account.dto.SummonerDto;
import com.nooblol.account.mapper.SummonerMapper;
import com.nooblol.account.service.SummonerService;
import com.nooblol.global.config.RiotConfiguration;
import com.nooblol.global.dto.ResponseDto;
import java.io.IOException;
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

import static com.nooblol.global.utils.CommonUtils.*;

@Service
@RequiredArgsConstructor
public class SummonerServiceImpl implements SummonerService {

  private final Logger log = LoggerFactory.getLogger(getClass());

  private final RiotConfiguration riotConfiguration;
  private final SummonerMapper summonerMapper;

  private final ObjectMapper objectMapper;

  @Override
  public ResponseDto getSummonerAccointInfo(String summonerName) {
    if (StringUtils.isBlank(summonerName)) {
      throw new IllegalArgumentException("소환사명이 입력되지 않았습니다");
    }
    return summonerAccountProcess(summonerName);
  }

  @Override
  public ResponseDto summonerAccountProcess(String summonerName) {
    ResponseDto responseDto = selectSummonerAccountByRiot(summonerName);
    summonerAccountDBProcess(responseDto);
    return responseDto;
  }

  @Override
  public void summonerAccountDBProcess(ResponseDto responseDto) {
    if (responseDto.getResultCode() == HttpStatus.OK.value()) {
      SummonerDto riotSearchData = (SummonerDto) responseDto.getResult();
      SummonerDto serviceDBData = selectSummonerAccountByDB(riotSearchData);

      if (objectIsNotNull(serviceDBData)) {
        boolean isSame = riotSearchData.equals(serviceDBData);
        if (!isSame) {
          summonerMapper.updateSummonerAccount(riotSearchData);
        }
      } else {
        summonerMapper.insertSummonerAccount(riotSearchData);
      }
    }
  }

  /**
   * 소환사명 검색 : https://kr.api.riotgames.com/lol/summoner/v4/summoners/by-name/
   *
   * @param summonerName
   * @return
   */
  @Override
  public ResponseDto selectSummonerAccountByRiot(String summonerName) {
    summonerName = summonerNameWhiteSpaceReplace(summonerName);
    String url = riotConfiguration.getDomain() + riotConfiguration.getSummonerNameSearchByNameApi()
        + summonerName;
    return responseResult(url, SummonerDto.class);
  }

  @Override
  public SummonerDto selectSummonerAccountByDB(SummonerDto summonerDto) {
    return summonerMapper.selectSummonerAccount(summonerDto);
  }

  /**
   * Http통신을 통해 response를 받은 데이터를 ResponseDto로 가공한다. resultClass 맞게 데이터를 result로 삽입하며, 통신오류 코드를 받은
   * 경우에는 해당 코드를 기반으로 HttpStatus의 상태값을 받으며 HttpStatus에 없는 값이거나 또는 Exception이 발생한 경우에는 NOT_FOUND로
   * ResponseDto를 반환받는다.
   *
   * @param url
   * @param resultClass
   * @param <T>
   * @return
   * @throws IOException
   */
  private <T> ResponseDto responseResult(String url, Class<T> resultClass) {
    ResponseDto rtnData = null;
    try {
      HttpClient client = HttpClientBuilder.create().build();
      HttpGet getRequest = new HttpGet(url);
      getRequest.addHeader("X-Riot-Token", riotConfiguration.getApiKey());
      HttpResponse response = client.execute(getRequest);
      rtnData = makeResponseDto(response, resultClass);
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (rtnData == null) {
        rtnData = new ResponseDto(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND);
      }
      return rtnData;
    }
  }

  private <T> T getResponseBody(HttpResponse response, Class<T> dto) throws IOException {
    ResponseHandler<String> handler = new BasicResponseHandler();
    String body = handler.handleResponse(response);

    return objectMapper.readValue(body, dto);
  }

  private <T> ResponseDto makeResponseDto(HttpResponse response, Class<T> resultClass)
      throws IOException {
    HttpStatus sameStatus = HttpStatus.valueOf(response.getStatusLine().getStatusCode());
    if (sameStatus == HttpStatus.OK) {
      return new ResponseDto(
          response.getStatusLine().getStatusCode(),
          getResponseBody(response, resultClass)
      );
    }
    if (sameStatus != null) {
      return new ResponseDto(sameStatus.value(), sameStatus);
    }
    return new ResponseDto(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND);
  }
}
