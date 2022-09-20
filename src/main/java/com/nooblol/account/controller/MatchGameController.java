package com.nooblol.account.controller;

import com.nooblol.account.service.MatchGameAddInfoService;
import com.nooblol.account.service.MatchGameInfoService;
import com.nooblol.global.dto.ResponseDto;
import com.nooblol.global.utils.CommonUtils;
import javax.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 게임 매치정보에 대한 컨트롤러
 */

@Slf4j
@RestController
@RequestMapping("/match")
@RequiredArgsConstructor
@Validated
public class MatchGameController {

  private final MatchGameInfoService matchGameInfoService;
  private final MatchGameAddInfoService matchGameAddInfoService;

  /**
   * Puuid를 요청한 사용자의 최근 전적 조회
   *
   * @param puuid    Summoner_Account의 puuid컬럼값으로, 해당 파라미터를 통해 해당 유저의 최근 전적데이터를 Return한다
   * @param sync     해당값은 Default로 false를 Riot과 통신하여 최근 전적 데이터를 DB로 삽입한 이후 전적 데이터를 Return하며, True인
   *                 경우 Riot서버와 추가적인 통신 없이, DB데이터를 바로 RETURN하며,
   * @param pageNum  Default Value는 0으로, 0은 최근전적 Row 0~29경기, 1은 Row 30~59경기까지를 Return하게 된다.
   * @param limitNum Default Value는 30으로 한번 조회에 몇건의 전적을 가져갈지를 설정하는 Parameter
   * @return
   * @throws Exception
   */
  @GetMapping("/list")
  public ResponseDto selectMatchList(
      @RequestParam(value = "puuid", required = false) @NotBlank String puuid,
      @RequestParam(value = "sync", required = false) boolean sync,
      @RequestParam(value = "page", defaultValue = "0") int pageNum,
      @RequestParam(value = "limit", defaultValue = "30") int limitNum
  ) throws Exception {
    pageNum = pageNum * limitNum;
    if (sync) {
      return matchGameInfoService.getMatchInfoListByPuuid(puuid, pageNum, limitNum);
    }

    return matchGameInfoService.syncRiotToDbByPuuidAfterGetMatchSimpleList(puuid, pageNum,
        limitNum);
  }

  /**
   * Match Id를 통하여 해당 게임에 참가한 모든 사용자에 대하여 DB에서 조회한다.
   *
   * @param matchId `/list` 에서 제공하는 항목중 MatchId값
   * @return
   */
  @GetMapping("/participants")
  public ResponseDto getMatchAllParticipants(
      @RequestParam(value = "matchId", required = false) @NotBlank String matchId
  ) {
    return CommonUtils.makeListToResponseDto(
        matchGameAddInfoService.getMatchAllParticipantsList(matchId)
    );
  }

  /**
   * Match Id를 통하여 해당 게임에서 금지된 챔피언의 목록을 조회한다.
   *
   * @param matchId `/list` 에서 제공하는 항목중 MatchId값
   * @return
   */
  @GetMapping("/ban")
  public ResponseDto getMatchBan(
      @RequestParam(value = "matchId", required = false) @NotBlank String matchId
  ) {
    return CommonUtils.makeListToResponseDto(matchGameAddInfoService.getMatchBanList(matchId));
  }

  /**
   * MatchId와 Puuid를 통하여 해당 게임에서 특정유저(puuid)의 사용한 룬정보를 조회한다.
   *
   * @param matchId `/list` 에서 제공하는 항목중 MatchId값
   * @param puuid   `/list`, `/participants` 에서 제공이된 사용자들의 puuid값
   * @return
   */
  @GetMapping("/rune")
  public ResponseDto getMatchUseRun(
      @RequestParam(value = "matchId", required = false) @NotBlank String matchId,
      @RequestParam(value = "puuid", required = false) @NotBlank String puuid
  ) {
    return CommonUtils.makeListToResponseDto(
        matchGameAddInfoService.getMatchUseRunList(matchId, puuid)
    );
  }

}
