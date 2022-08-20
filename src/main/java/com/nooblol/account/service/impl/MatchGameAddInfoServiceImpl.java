package com.nooblol.account.service.impl;

import com.nooblol.account.dto.match.MatchGameBansDto;
import com.nooblol.account.dto.match.MatchGameParticipantsDto;
import com.nooblol.account.dto.match.MatchUseRuneDto;
import com.nooblol.account.mapper.MatchGameAddInfoMapper;
import com.nooblol.account.service.MatchGameAddInfoService;
import com.nooblol.global.dto.ResponseDto;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
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

  /**
   * 해당 경기에 참여한 모든 사용자의 게임 내용 반환
   *
   * @param matchId
   * @return
   */
  @Override
  @Transactional(readOnly = true)
  public ResponseDto getMatchAllParticipantsList(String matchId) {
    if (StringUtils.isBlank(matchId)) {
      throw new IllegalArgumentException(
          "getMatchAllParticipantsList(String) : MatchId가 입력되지 않았습니다.");
    }
    return makeReturnValue(selectMatchAllParticipantsListByMatchId(matchId));
  }

  @Override
  public List<MatchGameParticipantsDto> selectMatchAllParticipantsListByMatchId(String matchId) {
    return matchGameAddInfoMapper.selectMatchAllParticipantsListByMatchId(matchId);
  }

  /**
   * 해당경기에서 벤이된 챔피언을 리스트로 전달하며, 어느팀에서 벤을 하였는지는 구분 되어있지 않다.
   *
   * @param matchId
   * @return
   */
  @Override
  @Transactional(readOnly = true)
  public ResponseDto getMatchBanList(String matchId) {
    if (StringUtils.isBlank(matchId)) {
      throw new IllegalArgumentException("getMatchBanList(String) : MatchId가 입력되지 않았습니다.");
    }

    return makeReturnValue(selectMatchBanListByMatchId(matchId));
  }

  @Override
  public List<MatchGameBansDto> selectMatchBanListByMatchId(String matchId) {
    return matchGameAddInfoMapper.selectMatchGameBanList(matchId);
  }


  /**
   * 사용자가 해당 경기에서 사용한 모든 룬정보 반환.
   *
   * @param matchId
   * @param puuid
   * @return
   */
  @Override
  @Transactional(readOnly = true)
  public ResponseDto getMatchUseRunList(String matchId, String puuid) {
    if (StringUtils.isBlank(matchId)) {
      throw new IllegalArgumentException("getMatchUseRunList : MatchId가 입력되지 않았습니다.");
    }
    if (StringUtils.isBlank(puuid)) {
      throw new IllegalArgumentException("getMatchUseRunList : puuid가 입력되지 않았습니다.");
    }
    return makeReturnValue(selectMatchUseRuneByMatchIdAndPuuid(matchId, puuid));
  }

  @Override
  public List<MatchUseRuneDto> selectMatchUseRuneByMatchIdAndPuuid(String matchId, String puuid) {
    Map<String, String> paramMap = new HashMap<>();
    paramMap.put("matchId", matchId);
    paramMap.put("puuid", puuid);
    return matchGameAddInfoMapper.selectMatchGameUseRunes(paramMap);
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
      return new ResponseDto(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND);
    }
    return new ResponseDto(HttpStatus.OK.value(), list);
  }
}
