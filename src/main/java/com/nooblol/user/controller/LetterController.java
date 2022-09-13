package com.nooblol.user.controller;

import com.nooblol.global.annotation.UserLoginCheck;
import com.nooblol.global.dto.ResponseDto;
import com.nooblol.user.dto.LetterDeleteRequestDto;
import com.nooblol.user.dto.LetterInsertRequestDto;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 쪽지의 내용을 수정하는 경우는 없기에 Put의 경우에는 따로 존재하지 않는다.
 */

@Slf4j
@Validated
@RestController
@RequestMapping("/user/letter")
@RequiredArgsConstructor
public class LetterController {

  /**
   * Parameter로 받은 letterId의 쪽지를 조회한다. 조회시에는 session에 저장된 사용자가 letterId를 받은 발신자 혹은 수신자인 경우에만 데이터를
   * Return한다
   *
   * @param letterId
   * @param session
   * @return
   */
  @UserLoginCheck
  @GetMapping("/{letterId}")
  public ResponseDto getLetter(@PathVariable @NotBlank int letterId, HttpSession session) {
    return null;
  }

  /**
   * 사용자의 쪽찌 List를 조회한다.
   *
   * @param pageNum
   * @param limitNum
   * @param session
   * @return
   */
  @UserLoginCheck
  @GetMapping("/list")
  public ResponseDto getLetterList(
      @RequestParam(value = "page", defaultValue = "0") int pageNum,
      @RequestParam(value = "limit", defaultValue = "30") int limitNum,
      HttpSession session
  ) {

    return null;
  }

  /**
   * 쪽지의 발송
   *
   * @param session
   * @return
   */
  @UserLoginCheck
  @PostMapping("/")
  public ResponseDto insertLetter(
      @Valid @RequestBody LetterInsertRequestDto letterInsertRequestDto,
      HttpSession session
  ) {
    return null;
  }

  /**
   * 쪽지의 삭제 - Status값만 변경한다. 수신받은 letterId와 Type인 To, From을 확인하여, 해당 Status를 삭제로 변경한다
   *
   * @param session
   * @return
   */
  @UserLoginCheck
  @DeleteMapping("/")
  public ResponseDto deleteLetter(
      @Valid @RequestBody LetterDeleteRequestDto letterDeleteRequestDto, HttpSession session
  ) {
    return null;
  }
}
