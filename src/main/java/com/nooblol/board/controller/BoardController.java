package com.nooblol.board.controller;

import com.nooblol.global.dto.ResponseDto;
import com.nooblol.board.service.CategoryService;
import com.nooblol.global.utils.CommonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
@Validated
public class BoardController {

  private final CategoryService categoryService;

  /**
   * @param status
   * @return
   */
  @GetMapping("/categoryList")
  public ResponseDto getCategoryList(
      @RequestParam(value = "status", defaultValue = "1") int status) {
    return CommonUtils.makeListToResponseDto(categoryService.getCategoryList(status));
  }
}
