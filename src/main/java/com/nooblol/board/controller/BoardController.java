package com.nooblol.board.controller;

import com.nooblol.board.utils.BoardStatusEnum;
import com.nooblol.global.dto.ResponseDto;
import com.nooblol.board.service.CategoryService;
import com.nooblol.global.utils.CommonUtils;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

  /**
   * Parameter로 요청한 CategoryId의 하위 게시판리스트를 OK상태값과 함께 반환한다.
   *
   * @param categoryId     해당값은 필수로 들어와야한다
   * @param statusOptional 희망하는 상태값을 받는다. 없는 경우 DefaultValue로 Active상태값이 주어진다.
   * @return
   */
  @GetMapping({"/bbsList/#{categoryId}/#{status}", "/bbsList/#{categoryId}", "/bbsList"})
  public ResponseDto getBbsList(
      @PathVariable(required = false) int categoryId,
      @PathVariable Optional<Integer> statusOptional
  ) {

    //객체가 빈경우 DefaultValue로 Active값
    Integer status = statusOptional.orElse(BoardStatusEnum.ACTIVE.getStatus());

    return CommonUtils.makeListToResponseDto(
        categoryService.getBbsList(categoryId, status)
    );
  }
}
