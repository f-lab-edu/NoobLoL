package com.nooblol.account.service.impl;

import com.nooblol.account.dto.match.MatchGameBansDto;
import com.nooblol.account.dto.match.MatchGameParticipantsDto;
import com.nooblol.account.dto.match.MatchUseRuneDto;
import com.nooblol.account.mapper.MatchGameAddInfoMapper;
import com.nooblol.account.service.MatchGameAddInfoService;
import com.nooblol.global.dto.ResponseDto;
import com.nooblol.global.utils.ResponseEnum;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MatchGameAddInfoServiceImpl implements MatchGameAddInfoService {

  private final Logger log = LoggerFactory.getLogger(getClass());

  private final MatchGameAddInfoMapper matchGameAddInfoMapper;


  @Override
  @Transactional(readOnly = true)
  public ResponseDto getMatchAllParticipantsList(String matchId) {
    return makeReturnValue(selectMatchAllParticipantsListByMatchId(matchId));
  }

  @Override
  @Transactional(readOnly = true)
  public List<MatchGameParticipantsDto> selectMatchAllParticipantsListByMatchId(String matchId) {
    return matchGameAddInfoMapper.selectMatchAllParticipantsListByMatchId(matchId);
  }

  @Override
  @Transactional(readOnly = true)
  public ResponseDto getMatchBanList(String matchId) {
    return makeReturnValue(selectMatchBanListByMatchId(matchId));
  }

  @Override
  public List<MatchGameBansDto> selectMatchBanListByMatchId(String matchId) {
    return matchGameAddInfoMapper.selectMatchGameBanList(matchId);
  }

  @Override
  @Transactional(readOnly = true)
  public ResponseDto getMatchUseRunList(String matchId, String puuid) {
    return makeReturnValue(selectMatchUseRuneByMatchIdAndPuuid(matchId, puuid));
  }

  @Override
  public List<MatchUseRuneDto> selectMatchUseRuneByMatchIdAndPuuid(String matchId, String puuid) {
    return matchGameAddInfoMapper.selectMatchGameUseRunes(puuid, matchId);
  }


  /**
   * MatchGameInfo에서의 경우에는 사용하지 않은 방식이며, 해당 테이블의 경우 대량의 정보를 서로 주고받기 때문에 한개의 메소드에서 모든 처리를 하는 과정을 막고
   * 싶었다.
   * <p>
   * 해당 클래스에서 사용한 이유는 MatchGameInfo에 비해 상대적으로 적은 요청이 들어오는 경우가 많을 것이라 예상되어 한개의 메소드에서 모든 Return작업을
   * 처리하였다.
   *
   * @param list
   * @param <T>
   * @return
   */
  private <T> ResponseDto makeReturnValue(List<T> list) {
    if (list == null || list.size() <= 0) {
      return ResponseEnum.NOT_FOUND.getResponse();
    }
    return new ResponseDto(HttpStatus.OK.value(), list);
  }
}
