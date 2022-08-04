package com.nooblol.account.mapper;

import com.nooblol.account.dto.SummonerDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SummonerMapper {

  SummonerDto selSummonerAccount(SummonerDto summonerDto);

  int updSummonerAccount(SummonerDto summonerDto);

  int insSummonerAccount(SummonerDto summonerDto);

}
