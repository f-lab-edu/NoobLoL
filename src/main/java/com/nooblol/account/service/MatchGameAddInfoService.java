package com.nooblol.account.service;

import com.nooblol.account.dto.match.MatchGameBansDto;
import com.nooblol.account.dto.match.MatchGameParticipantsDto;
import com.nooblol.global.dto.ResponseDto;
import java.util.List;

public interface MatchGameAddInfoService {

  ResponseDto getMatchAllParticipantsList(String matchId);

  List<MatchGameParticipantsDto> selectMatchAllParticipantsListByMatchId(String matchId);

  ResponseDto getMatchBanList(String matchId);

  List<MatchGameBansDto> selectMatchBanListByMatchId(String matchId);

}
