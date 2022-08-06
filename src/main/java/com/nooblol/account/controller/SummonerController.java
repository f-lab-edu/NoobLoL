package com.nooblol.account.controller;

import com.nooblol.account.service.SummonerHistoryService;
import com.nooblol.account.service.SummonerService;
import com.nooblol.global.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Controller
@RequestMapping("/summoner")
@RequiredArgsConstructor
public class SummonerController {

  private final SummonerService summonerService;
  private final SummonerHistoryService summonerHistoryService;

  @GetMapping("/search/name")
  public ResponseDto searchSummonerByName(@RequestParam(value = "summonerName") String summonerName)
      throws IllegalArgumentException {
    return summonerService.getSummonerAccointInfo(summonerName);
  }

  @GetMapping("/history")
  public ResponseDto searchSummonerHistryById(
      @RequestParam(value = "summonerId") String summonerId,
      @RequestParam(value = "sync", required = false) boolean sync
  ) throws IllegalArgumentException {
    return summonerHistoryService.getSummonerHistoryInfo(summonerId, sync);
  }
}
