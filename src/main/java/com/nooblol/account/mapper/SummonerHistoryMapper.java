package com.nooblol.account.mapper;

import com.nooblol.account.dto.SummonerHistoryDto;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SummonerHistoryMapper {

  List<SummonerHistoryDto> selSummonerHistoryById(String summonerId);

  SummonerHistoryDto selSummonerHistoryByLeagueAndId(SummonerHistoryDto summonerHistoryDto);

  int updSummonerHistry(SummonerHistoryDto summonerHistoryDto);

  int insSummonerHistory(SummonerHistoryDto summonerHistoryDto);
}
