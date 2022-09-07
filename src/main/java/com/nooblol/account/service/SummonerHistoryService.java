package com.nooblol.account.service;

import com.nooblol.global.dto.ResponseDto;

public interface SummonerHistoryService {

  /**
   * 소환사의 랭크정보를 Return한다.
   *
   * @param summonerId Account의 accoount_id값이다.
   * @param sync       default값은 false이며, DB의 데이터를 조회할지, Riot서버의 정보를 조회후 Return할지 선택하는 Parameter다.
   * @return
   */
  ResponseDto getSummonerHistoryInfo(String summonerId, boolean sync);

}
