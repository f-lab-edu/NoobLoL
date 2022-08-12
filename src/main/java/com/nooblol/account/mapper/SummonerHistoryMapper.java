package com.nooblol.account.mapper;

import com.nooblol.account.dto.summoner.SummonerHistoryDto;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SummonerHistoryMapper {

  List<SummonerHistoryDto> selectSummonerHistoryById(String id);

  SummonerHistoryDto selectSummonerHistoryByLeagueAndId(@Param("leagueId") String leagueId,
      @Param("summonerId") String summonerId);

  int updateSummonerHistory(SummonerHistoryDto summonerHistoryDto);

  int insertSummonerHistory(SummonerHistoryDto summonerHistoryDto);
}
