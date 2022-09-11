package com.nooblol.board.controller;

import com.nooblol.board.dto.CategoryRequestDto;
import com.nooblol.board.utils.ArticleMessage;
import com.nooblol.board.utils.BoardStatusEnum;
import com.nooblol.global.annotation.UserLoginCheck;
import com.nooblol.global.dto.ResponseDto;
import com.nooblol.board.service.CategoryService;
import com.nooblol.global.exception.ExceptionMessage;
import com.nooblol.global.utils.CommonUtils;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
  @GetMapping("/category/list")
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
  @GetMapping({"/bbsList/{categoryId}/{status}", "/bbsList/{categoryId}", "/bbsList"})
  public ResponseDto getBbsList(
      @PathVariable(required = false) Optional<Integer> categoryId,
      @PathVariable(required = false) Optional<Integer> status
  ) {
    if (categoryId.isEmpty()) {
      throw new IllegalArgumentException(ExceptionMessage.BAD_REQUEST);
    }

    //객체가 빈경우 DefaultValue로 Active값
    Integer statusValue = status.orElse(BoardStatusEnum.ACTIVE.getStatus()).intValue();
    Integer categoryIdValue = categoryId.get().intValue();
    return CommonUtils.makeListToResponseDto(
        categoryService.getBbsList(categoryIdValue, statusValue)
    );
  }

  @GetMapping("/bbsAllList")
  public ResponseDto getAllBbsList() {
    return CommonUtils.makeListToResponseDto(categoryService.getAllBbsList());
  }


  /**
   * 카테고리 추가하며, 정상적으로 Insert가 되면 결과값으로 True를 Return한다
   *
   * @param categoryInsertDto
   * @param session           현재 로그인중인 사용자 확인
   * @return
   */
  @UserLoginCheck
  @PostMapping("/category")
  public ResponseDto insertCategory(
      @Valid @RequestBody CategoryRequestDto.CategoryInsertDto categoryInsertDto,
      HttpSession session
  ) {
    return CommonUtils.makeToResponseOkDto(
        categoryService.insertCategory(categoryInsertDto, session)
    );
  }

  /**
   * 카테고리 정보수정하며, 정상적으로 데이터 변경이 성공하면 결과값으로 True를 Return한다
   *
   * @return
   */
  @UserLoginCheck
  @PutMapping("/category")
  public ResponseDto updateCategory(
      @Valid @RequestBody CategoryRequestDto.CategoryUpdateDto categoryUpdateDto,
      HttpSession session
  ) {
    return CommonUtils.makeToResponseOkDto(
        categoryService.updateCategory(categoryUpdateDto, session)
    );
  }

  /**
   * 카테고리의 삭제하며,  정상적으로 데이터 변경이 성공하면 결과값으로 True를 Return한다
   * @param categoryId
   * @param session
   * @return
   */
  @UserLoginCheck
  @DeleteMapping("/category/{categoryId}")
  public ResponseDto deleteCategory(
      @PathVariable @NotNull(message = ArticleMessage.CATEGORY_ID_NULL) Integer categoryId,
      HttpSession session) {
    return CommonUtils.makeToResponseOkDto(categoryService.deleteCategory(categoryId, session));
  }
}
