package com.nooblol.account.mapper;

import com.nooblol.account.dto.SummonerHistoryDto;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SummonerHistoryMapper {

  List<SummonerHistoryDto> selectSummonerHistoryById(String summonerId);

  SummonerHistoryDto selectSummonerHistoryByLeagueAndId(SummonerHistoryDto summonerHistoryDto);

  int updateSummonerHistory(SummonerHistoryDto summonerHistoryDto);

  int insertSummonerHistory(SummonerHistoryDto summonerHistoryDto);
}
