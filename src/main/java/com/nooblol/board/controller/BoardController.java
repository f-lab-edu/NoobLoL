package com.nooblol.board.controller;

import com.nooblol.board.utils.BoardStatusEnum;
import com.nooblol.global.dto.ResponseDto;
import com.nooblol.board.service.CategoryService;
import com.nooblol.global.utils.CommonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
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
   * 파라미터로 받은 status와 일치하는 모든 category를 반환한다.
   *
   * @param status Category의 상태값
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
   * @param categoryId 해당값은 필수로 들어와야한다
   * @param status     희망하는 상태값을 받는다. 없는 경우 DefaultValue로 Active상태값이 주어진다.
   * @return
   */
  @GetMapping("/bbsList")
  public ResponseDto getBbsList(
      @RequestParam(value = "categoryId") int categoryId,
      @RequestParam(value = "status", required = false) Integer status
  ) {
    if (ObjectUtils.isEmpty(status)) {
      status = BoardStatusEnum.ACTIVE.getStatus();
    }
    return CommonUtils.makeListToResponseDto(
        categoryService.getBbsList(categoryId, status)
    );
  }


  @GetMapping("/bbsAllList")
  public ResponseDto getAllBbsList() {
    return CommonUtils.makeListToResponseDto(categoryService.getAllBbsList());
  }
}
