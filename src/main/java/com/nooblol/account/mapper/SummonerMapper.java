package com.nooblol.account.mapper;

import com.nooblol.account.dto.SummonerDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SummonerMapper {
    SummonerDto selectAccount(SummonerDto account);
    int updateAccount(SummonerDto account);
    int insertAccount(SummonerDto account);
}
