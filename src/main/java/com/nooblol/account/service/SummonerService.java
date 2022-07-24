package com.nooblol.account.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nooblol.account.dto.SummonerDto;
import com.nooblol.account.mapper.SummonerMapper;
import com.nooblol.global.config.RiotConfiguration;
import com.nooblol.global.dto.ResponseDto;
import com.nooblol.global.utils.SummonerUtils;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class SummonerService {

    private final RiotConfiguration riotConfiguration;
    private final SummonerMapper summonerMapper;

    public ResponseDto accountProcess(String summonerName) {
        ResponseDto riotResult = getAccountInfo(summonerName);

        //정상적으로 통신을 진행하였을 경우에만 실행
        if(riotResult.getResultCode() == HttpStatus.OK.value()) {
            SummonerDto riotServerDto = (SummonerDto)riotResult.getResult();
            SummonerDto dbSummonerDto = selectAccount(riotServerDto);

            if(dbSummonerDto != null) {
                //DB에 데이터가 존재하는 경우 (false가 틀린경우)
                boolean compareObjResult = compareSummonerAccount(riotServerDto, dbSummonerDto);
                updateAccount(compareObjResult, riotServerDto);
            } else {
                //DB에 데이터가 존재하지 않는 경우
                insertAccount(riotServerDto);
            }
        }
        return riotResult;
    }

    /**
     * 라이엇서버에서 받아온 Account데이터가 DB에 존재하지 않는 경우
     * @param riotServerDto
     */
    public void insertAccount(SummonerDto riotServerDto) {
        summonerMapper.insertAccount(riotServerDto);
    }

    /**
     * 라이엇 서버에서 받아온 데이터와 DB의 Account테이블이 일치하지 않는 경우 Update
     * @param compareObjResult
     * @param riotServerDto
     */
    public void updateAccount(boolean compareObjResult, SummonerDto riotServerDto) {
        if(!compareObjResult){
            summonerMapper.updateAccount(riotServerDto);
        }
    }

    /**
     * 계정조회-서버확인하여 존재하는지 확인, 무조건 DTO 반환함
     * @param summonerName
     * @return
     */
    public ResponseDto getAccountInfo(String summonerName) {
        //변수 초기화
        ResponseDto rtnDto = null;
        summonerName = SummonerUtils.whitespaceReplace(summonerName);
        String requestURL = "https://kr.api.riotgames.com/lol/summoner/v4/summoners/by-name/"+ summonerName + "?api_key=" + riotConfiguration.getApiKey();

        ObjectMapper objectMapper = new ObjectMapper();
        SummonerDto dto = null;
        try {
            HttpClient client = HttpClientBuilder.create().build(); //HttpClient 생성
            HttpGet getRequest = new HttpGet(requestURL);
            getRequest.addHeader("X-Riot-Token", riotConfiguration.getApiKey());
            HttpResponse response = client.execute(getRequest);

            if(response.getStatusLine().getStatusCode() == HttpStatus.OK.value()) {
                ResponseHandler<String> handler = new BasicResponseHandler();
                String body = handler.handleResponse(response);
                dto = objectMapper.readValue(body, SummonerDto.class);
                rtnDto = new ResponseDto(response.getStatusLine().getStatusCode(), dto);
            } else {
                rtnDto = new ResponseDto(response.getStatusLine().getStatusCode(), "error");
            }
        } catch (IOException e) {
            e.printStackTrace();
            rtnDto = new ResponseDto(404, "error");
        } finally {
            return rtnDto;
        }
    }

    /**
     * 계정조회-account의 id값으로 확인
     * @param accountDto
     * @return
     */
    public SummonerDto selectAccount(SummonerDto accountDto) {
        return summonerMapper.selectAccount(accountDto);
    }
    /**
     * 계정조회-account의 id값으로 확인
     * @param accountDto
     * @return
     */
    public SummonerDto selectAccount(ResponseDto accountDto) {
        if(accountDto.getResult() instanceof SummonerDto){
            return selectAccount((SummonerDto)accountDto.getResult());
        }
        return null;
    }

    /**
     * 계정조회-객체의 VALUE비교 및 틀린사항 확인
     */
    public boolean compareSummonerAccount(SummonerDto serverDto, SummonerDto dbDto) {
        return serverDto.equals(dbDto);
    }


}
