package com.nooblol.account.service;

import com.nooblol.global.dto.ResponseDto;

/**
 * Account의 puuid를 통하여 MatchId의 List를 받아오는 Service
 */
public interface MatchGameListService {

  ResponseDto getMatchListId(String puuid);

  ResponseDto getMatchListIdProcessByRiot(String puuid);

  String getApiReplaceByPuuid(String puuid);
}
