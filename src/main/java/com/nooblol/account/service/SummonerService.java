package com.nooblol.account.service;


import com.nooblol.account.dto.summoner.SummonerDto;
import com.nooblol.global.dto.ResponseDto;

public interface SummonerService {

  ResponseDto getSummonerAccointInfo(String summonerName);

  ResponseDto summonerAccountProcess(String summonerName);

  ResponseDto selectSummonerAccountByRiot(String summonerName);

  void summonerAccountDBProcess(ResponseDto responseDto);

  SummonerDto selectSummonerAccountByDB(SummonerDto summonerDto);

}
