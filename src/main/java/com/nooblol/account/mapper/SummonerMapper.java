package com.nooblol.account.mapper;

import com.nooblol.account.dto.SummonerDto;
import com.nooblol.account.dto.SummonerHistoryDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SummonerMapper {
    SummonerDto selectAccount(SummonerDto account);
    int updateAccount(SummonerDto account);
    int insertAccount(SummonerDto account);

    SummonerHistoryDto selectSummonerHistoryByLeagueIdAndSummonerId(SummonerHistoryDto summonerHistoryDto);
    void updateSummonerHistory(SummonerHistoryDto summonerHistoryDto);
    void insertSummonerHistory(SummonerHistoryDto summonerHistoryDto);
    List<SummonerHistoryDto> selectSummonerHistoryBySummonerId(String summonerId);
}
