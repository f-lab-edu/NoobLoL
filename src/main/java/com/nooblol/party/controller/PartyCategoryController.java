package com.nooblol.party.controller;


import com.nooblol.global.dto.ResponseDto;
import com.nooblol.party.service.PartyCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 카테고리의 경우에는 휘발이 되면 안되기 떄문에 DB에서 데이터를 가져온다.
 */

@Slf4j
@Validated
@RestController
@RequestMapping("/party/category")
@RequiredArgsConstructor
public class PartyCategoryController {

  private final PartyCategoryService partyCategoryService;

  /**
   * 카테고리 단건 조회
   *
   * @return
   */
  @GetMapping("/")
  public ResponseDto getPartyCategory() {
    return null;
  }

  /**
   * 카테고리 리스트 획득
   *
   * @return
   */
  @GetMapping("/list")
  public ResponseDto getPartyCategoryList() {
    return null;
  }

  /**
   * 관리자기능 - 카테고리추가
   *
   * @return
   */
  //TODO [22. 09. 15] : 관리자 권한 확인하는 Annotation 추후 추가
  @PostMapping("/")
  public ResponseDto insertCategory() {
    return null;
  }

  /**
   * 카테고리 정보 수정
   */
  //TODO [22. 09. 15] : 관리자 권한 확인하는 Annotation 추후 추가
  @PutMapping("/")
  public ResponseDto updateCategry() {
    return null;
  }

  /**
   * 카테고리의 삭제 - 실제로 삭제하진 않고 Status만
   */
  //TODO [22. 09. 15] : 관리자 권한 확인하는 Annotation 추후 추가
  @DeleteMapping("/")
  public ResponseDto deleteCategory() {
    return null;
  }
}
