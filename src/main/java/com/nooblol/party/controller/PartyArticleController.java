package com.nooblol.party.controller;


import com.nooblol.global.dto.ResponseDto;
import com.nooblol.party.service.PartyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@Validated
@RestController
@RequestMapping("/party/article")
@RequiredArgsConstructor
public class PartyArticleController {

  private final PartyService partyService;

  /**
   * 게시물 조회
   *
   * @return
   */
  @GetMapping("/{partyId}")
  public ResponseDto getPartyArticle(
      @PathVariable int partyId
  ) {
    return null;
  }


  @GetMapping("/list")
  public ResponseDto getPartyArticleList(
      @RequestParam(value = "category") String category
  ) {
    return null;
  }

  @PutMapping("/")
  public ResponseDto updateArticle() {
    return null;
  }


}
