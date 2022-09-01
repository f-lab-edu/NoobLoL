package com.nooblol.account.controller;

import com.nooblol.account.service.SummonerHistoryService;
import com.nooblol.account.service.SummonerService;
import com.nooblol.global.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/summoner")
@RequiredArgsConstructor
public class SummonerController {


  private final SummonerService summonerService;
  private final SummonerHistoryService summonerHistoryService;

  /**
   * 소환사명(닉네임)을 받아 Riot API로 확인하여 존재유무 확인 및 존재시 해당 Data Update
   *
   * @param summonerName
   * @return
   * @throws IllegalArgumentException
   */
  @GetMapping("/search/name")
  public ResponseDto searchSummonerByName(@RequestParam(value = "summonerName") String summonerName)
      throws IllegalArgumentException {
    return summonerService.getSummonerAccointInfo(summonerName);
  }

  /**
   * searchSummonerByName메소드에서 획득한 id값을 기반으로 하여 해당 계정의 랭크정보(랭크 타입, 티어, 승, 패)에 대한 정보를 반환한다.
   *
   * @param summonerId
   * @param sync
   * @return
   * @throws IllegalArgumentException
   */
  @GetMapping("/history")
  public ResponseDto searchSummonerHistryById(
      @RequestParam(value = "summonerId") String summonerId,
      @RequestParam(value = "sync", required = false) boolean sync
  ) throws IllegalArgumentException {
    return summonerHistoryService.getSummonerHistoryInfo(summonerId, sync);
  }
}
