package com.nooblol.account.service;

import com.nooblol.account.dto.match.MatchDto;
import com.nooblol.global.dto.ResponseDto;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

public interface MatchGameInfoService {

  ResponseDto getMatchInfoListByPuuid(String puuid) throws Exception;

  ResponseDto syncRiotToDbDataProcess(String puuid) throws Exception;

  List<String> getNotExistMatchList(List<String> matchIdList);

  String getApiReplaceByMatchId(String puuid);

  String getMakeUri(String matchId);

  MatchDto getMatchDataByRiot(String matchId);

  boolean insertMatchDataByDB(MatchDto matchDto) throws Exception;
}
