package com.nooblol.account.service;


import com.nooblol.account.dto.SummonerDto;
import com.nooblol.global.dto.ResponseDto;

public interface SummonerService {

  ResponseDto getSummonerAccointInfo(String summonerName);

  ResponseDto summonerAccountProcess(String summonerName);

  ResponseDto selSummonerAccountByRiot(String summonerName);

  void summonerAccountDBProcess(ResponseDto responseDto);

  SummonerDto selSummonerAccountByDB(SummonerDto summonerDto);

}
