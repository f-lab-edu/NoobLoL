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
   * 전적항목을 간단하게 표현함에 있어서 필요한 정보만 Return 한다.
   * PUUID사용자의 가장최근 진행한 게임순으로 순차가 이뤄지며, 같이 참여한
   * @param puuid
   * @param sync
   * @param pageNum
   * @return
   * @throws Exception
   */
  @GetMapping("/simplelist")
  public ResponseDto selectMatchList(@RequestParam("puuid") String puuid,
      @RequestParam(value = "sync", required = false) boolean sync,
      @RequestParam(value = "page", defaultValue = "0") int pageNum) throws Exception {
    ResponseDto rtnData = null;
    pageNum = pageNum * 30;

    if (sync) {
      return matchGameInfoService.getMatchInfoListByPuuid(puuid, pageNum);
    }

    return matchGameInfoService.syncRiotToDbByPuuidAfterGetMatchSimpleList(puuid, pageNum);
  }

}
