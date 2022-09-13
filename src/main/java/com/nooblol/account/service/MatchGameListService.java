package com.nooblol.account.service;

import com.nooblol.global.dto.ResponseDto;

/**
 * Account의 puuid를 통하여 MatchId의 List를 받아오는 Service
 * https://developer.riotgames.com/apis#match-v5/GET_getMatchIdsByPUUID
 * /lol/match/v5/matches/by-puuid/{puuid}/ids API사용
 */
public interface MatchGameListService {

  /**
   * 최근 MatchId List를 가져온다
   *
   * @param puuid
   * @return
   */
  ResponseDto getMatchListId(String puuid);

  /**
   * 파라미터로 받은 Puuid를 기준으로 Riot서버와 통신을 하여 MatchId List를 반환한다. 통신결과가 없는 경우에는 NotFound를 Return한다.
   *
   * @param puuid
   * @return
   */
  ResponseDto getMatchListIdProcessByRiot(String puuid);

  /**
   * 파라미터로 받은 Puuid를 통해 MatchId리스트를 조회할 수 있도록 URI를 제작한다.
   *
   * @param puuid
   * @return
   */
  String getApiReplaceByPuuid(String puuid);
}
