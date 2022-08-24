package com.nooblol.account.mapper;

import com.nooblol.account.dto.match.MatchGameInfoDto;
import com.nooblol.account.dto.match.TeamDto;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MatchGameInfoMapper {

  ArrayList<String> existsMatchIdListByMatch(String matchIdList);

  int insertMatchGameInfo(MatchGameInfoDto info);

  int insertMatchGameBans(List<TeamDto> teamList);

  int insertMatchGameParticipants(MatchGameInfoDto info);

  int insertMatchGameUseStatRunes(Map info);

  void insertMatchGameUseStyleRunes(Map infoMap);
}
