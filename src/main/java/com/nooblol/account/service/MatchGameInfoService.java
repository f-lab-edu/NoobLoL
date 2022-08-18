package com.nooblol.account.service;

import com.nooblol.account.dto.match.MatchDto;
import com.nooblol.account.dto.match.MatchGameSimpleDto;
import com.nooblol.global.dto.ResponseDto;
import java.util.List;

public interface MatchGameInfoService {

  ResponseDto getMatchInfoListByPuuid(String puuid, int pageNum) throws Exception;

  List<MatchGameSimpleDto> selectMatchSimpleListByPuuidInDB(String puuid, int pageNum);

  ResponseDto syncRiotToDbByPuuidAfterGetMatchSimpleList(String puuid, int pageNum)
      throws Exception;

  ResponseDto syncRiotToDbDataProcess(String puuid) throws Exception;

  List<String> getNotExistMatchList(List<String> matchIdList);

  String getApiReplaceByMatchId(String puuid);

  String getMakeUri(String matchId);

  MatchDto getMatchDataByRiot(String matchId);

  boolean insertMatchDataByDB(MatchDto matchDto) throws Exception;
}
