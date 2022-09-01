package com.nooblol.account.service;

import com.nooblol.account.dto.match.MatchGameBansDto;
import com.nooblol.account.dto.match.MatchGameParticipantsDto;
import com.nooblol.account.dto.match.MatchUseRuneDto;
import com.nooblol.global.dto.ResponseDto;
import java.util.List;

public interface MatchGameAddInfoService {

  /**
   * 해당 Match Id 에 참여한 모든 사용자의 게임 내용 반환
   *
   * @param matchId
   * @return
   */
  List<MatchGameParticipantsDto> getMatchAllParticipantsList(String matchId);

  /**
   * 해당 Match Id 에서 벤이된 챔피언을 리스트로 전달하며, 어느팀에서 벤을 하였는지는 구분 되어있지 않다.
   *
   * @param matchId
   * @return
   */
  List<MatchGameBansDto> getMatchBanList(String matchId);

  /**
   * 사용자가 해당 경기에서 사용한 모든 룬정보 반환.
   *
   * @param matchId
   * @param puuid
   * @return
   */
  List<MatchUseRuneDto> getMatchUseRunList(String matchId, String puuid);

}
