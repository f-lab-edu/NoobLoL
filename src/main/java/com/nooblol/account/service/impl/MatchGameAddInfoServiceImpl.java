package com.nooblol.account.service.impl;

import com.nooblol.account.dto.match.MatchGameBansDto;
import com.nooblol.account.dto.match.MatchGameParticipantsDto;
import com.nooblol.account.dto.match.MatchUseRuneDto;
import com.nooblol.account.mapper.MatchGameAddInfoMapper;
import com.nooblol.account.service.MatchGameAddInfoService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MatchGameAddInfoServiceImpl implements MatchGameAddInfoService {

  private final MatchGameAddInfoMapper matchGameAddInfoMapper;


  @Override
  @Transactional(readOnly = true)
  public List<MatchGameParticipantsDto> getMatchAllParticipantsList(String matchId) {
    return matchGameAddInfoMapper.selectMatchAllParticipantsListByMatchId(matchId);
  }

  @Override
  @Transactional(readOnly = true)
  public List<MatchGameBansDto> getMatchBanList(String matchId) {
    return matchGameAddInfoMapper.selectMatchGameBanList(matchId);
  }

  @Override
  @Transactional(readOnly = true)
  public List<MatchUseRuneDto> getMatchUseRunList(String matchId, String puuid) {
    return matchGameAddInfoMapper.selectMatchGameUseRunes(puuid, matchId);
  }


}
