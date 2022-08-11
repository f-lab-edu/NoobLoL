package com.nooblol.account.service;

import com.nooblol.global.dto.ResponseDto;

public interface SummonerHistoryService {

  ResponseDto getSummonerHistoryInfo(String summonerId, boolean sync);

}
