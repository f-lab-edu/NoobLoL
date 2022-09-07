package com.nooblol.account.service;


import com.nooblol.account.dto.summoner.SummonerDto;
import com.nooblol.global.dto.ResponseDto;

public interface SummonerService {

  ResponseDto getSummonerAccointInfo(String summonerName);


  /**
   * 소환사명을 바탕으로 RiotAPI에 무조건 조회를 하며, 조회해온 데이터가 DB와 일치하는 경우 별다른 작업없이 반환하며 데이터가 변경이 있는 경우에는 DB Update
   * 또는 Insert이후 RiotAPI에서 조회해온 데이터를 반환한다.
   *
   * @param summonerName 실제 사용하는 소환사명
   * @return
   */
  ResponseDto summonerAccountProcess(String summonerName);

  /**
   * 소환사명 검색 : https://kr.api.riotgames.com/lol/summoner/v4/summoners/by-name/
   *
   * @param summonerName
   * @return
   */
  ResponseDto selectSummonerAccountByRiot(String summonerName);

  void summonerAccountDBProcess(ResponseDto responseDto);

  SummonerDto selectSummonerAccountByDB(SummonerDto summonerDto);

}
