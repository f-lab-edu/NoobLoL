package com.nooblol.account.controller;

import com.nooblol.account.service.SummonerService;
import com.nooblol.global.config.RiotConfiguration;
import com.nooblol.global.dto.ResponseDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@Controller
@RequestMapping("/summoner/*")
public class SummonerController {
    SummonerService summonerService;

    public SummonerController(SummonerService summonerService) {
        this.summonerService = summonerService;
    }

    // TODO: 일단 '소환사명만 받는다' 전제로 제작, 결과 유무만 알려준다.
    @GetMapping("account")
    public ResponseDto searchSummoner(@Param(value="summonerName")String summonerName){
        //받아온 소환사의 존재 유무 확인
        return summonerService.accountProcess(summonerName);
    }
    


}
