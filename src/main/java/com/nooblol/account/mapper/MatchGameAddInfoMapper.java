package com.nooblol.account.mapper;

import com.nooblol.account.dto.match.MatchGameBansDto;
import com.nooblol.account.dto.match.MatchGameParticipantsDto;
import com.nooblol.account.dto.match.MatchGameSimpleDto;
import com.nooblol.account.dto.match.MatchSearchDto;
import com.nooblol.account.dto.match.MatchUseRuneDto;
import java.util.ArrayList;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MatchGameAddInfoMapper {

  ArrayList<MatchGameSimpleDto> selectMatchSimpleList(MatchSearchDto searchDto);

  ArrayList<MatchGameSimpleDto> selectMatchSimpleParticipantsList(String matchId);

  ArrayList<MatchGameParticipantsDto> selectMatchAllParticipantsListByMatchId(String matchId);

  ArrayList<MatchGameBansDto> selectMatchGameBanList(String matchId);

  ArrayList<MatchUseRuneDto> selectMatchGameUseRunes(
      @Param("puuid") String puuid,
      @Param("matchId") String matchId
  );

}