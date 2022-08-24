package com.nooblol.account.controller;

import com.nooblol.account.service.MatchGameInfoService;
import com.nooblol.global.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 게임 매치정보에 대한 컨트롤러
 */

@RestController
@RequestMapping("/match")
@RequiredArgsConstructor
public class MatchGameController {

  private final Logger log = LoggerFactory.getLogger(getClass());

  private final MatchGameInfoService matchGameInfoService;

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
      @RequestParam(value = "page", defaultValue = "0") int pageNum) throws Exception {
    pageNum = pageNum * 30;
    if (sync) {
      return matchGameInfoService.getMatchInfoListByPuuid(puuid, pageNum);
    }

    return matchGameInfoService.syncRiotToDbByPuuidAfterGetMatchSimpleList(puuid, pageNum);
  }

}
