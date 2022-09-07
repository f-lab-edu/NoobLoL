package com.nooblol.account.mapper;

import com.nooblol.account.dto.summoner.SummonerDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SummonerMapper {

  SummonerDto selectSummonerAccount(String summonerDto);

  int updateSummonerAccount(SummonerDto summonerDto);

  int insertSummonerAccount(SummonerDto summonerDto);

}
