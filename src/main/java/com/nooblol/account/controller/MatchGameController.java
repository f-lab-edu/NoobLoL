package com.nooblol.account.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 게임 매치정보에 대한 컨트롤러
 */

@RestController
@RequestMapping("/match")
@RequiredArgsConstructor
public class MatchGameController {

  private final Logger log = LoggerFactory.getLogger(getClass());


}
