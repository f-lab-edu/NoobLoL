package com.nooblol.account.mapper;

import com.nooblol.account.dto.match.MatchGameParticipantsDto;
import com.nooblol.account.dto.match.MatchGameSimpleDto;
import java.util.ArrayList;
import java.util.Map;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MatchGameAddInfoMapper {

  ArrayList<MatchGameSimpleDto> selectMatchSimpleList(Map<String, Object> searchParam);

  ArrayList<MatchGameSimpleDto> selectMatchSimpleParticipantsList(String matchId);

  ArrayList<MatchGameParticipantsDto> selectMatchAllParticipantsListByMatchId(String matchId);


}