package com.nooblol.user.controller;

import com.nooblol.global.annotation.LetterTypeValidation;
import com.nooblol.global.annotation.UserLoginCheck;
import com.nooblol.global.dto.ResponseDto;
import com.nooblol.global.utils.ResponseUtils;
import com.nooblol.global.utils.ResponseEnum;
import com.nooblol.global.utils.SessionUtils;
import com.nooblol.user.dto.LetterDto;
import com.nooblol.user.dto.LetterInsertRequestDto;
import com.nooblol.user.dto.LetterSearchDto;
import com.nooblol.user.service.LetterService;
import com.nooblol.user.utils.LetterConstants;
import com.nooblol.user.utils.LetterStatus;
import com.nooblol.user.utils.LetterType;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
@RequestMapping("/letter")
@RequiredArgsConstructor
public class LetterController {

  private final LetterService letterService;


  /**
   * Parameter로 받은 letterId의 쪽지를 조회한다. 조회시에는 session에 저장된 사용자가 letterId를 받은 발신자 혹은 수신자인 경우에만 데이터를
   * Return한다.
   * <p>
   * 수신자가 현재 로그인한 사용자인 경우에는 Status의 상태값을 읽은 상태로 변경한다.
   *
   * @param letterId
   * @param session
   * @return
   */
  @UserLoginCheck
  @GetMapping("/{letterId}")
  public ResponseDto getLetter(
      @PathVariable @NotNull(message = LetterConstants.LETTER_ID_NULL) int letterId,
      HttpSession session
  ) {
    return ResponseUtils.makeToResponseOkDto(letterService.getLetter(letterId, session));
  }

  /**
   * 발송, 수신 둘중 한개의 쪽지 리스트 조회한다
   *
   * @param type     발송리스트, 수신리스트를 선택한다. 값이 없는 경우 BAD_REQUEST
   * @param pageNum
   * @param limitNum
   * @param session
   * @return
   */
  @UserLoginCheck
  @LetterTypeValidation
  @GetMapping("/list/{type}")
  public ResponseDto getLetterToList(
      @PathVariable LetterType type,
      @RequestParam(value = "page", required = false, defaultValue = "0") int pageNum,
      @RequestParam(value = "limit", required = false, defaultValue = "30") int limitNum,
      HttpSession session
  ) {
    LetterSearchDto searchParameterDto = makeLetterListSearchDto(
        SessionUtils.getSessionUserId(session), pageNum, limitNum, type
    );

    //Data 가 Null인경우 CommonUtils에선 NotFound로 보내버려서 직접 설정함.
    ResponseDto result = ResponseEnum.OK.getResponse();
    result.setResult(letterService.getLetterListByUserId(searchParameterDto));

    return result;
  }

  /**
   * 쪽지의 발송기능이며, 본인이 본인한테의 쪽지 발송은 불가능하다.
   *
   * @param session
   * @return
   */
  @UserLoginCheck
  @PostMapping("/")
  public ResponseDto insertLetter(
      @Valid @RequestBody LetterInsertRequestDto letterInsertRequestDto, HttpSession session
  ) {
    return ResponseUtils.makeToResponseOkDto(
        letterService.insertLetter(letterInsertRequestDto, session)
    );
  }

  /**
   * 쪽지의 삭제 - Status만 변경한다. 수신받은 letterId와 Type인 To, From을 확인하여, 해당 Status를 삭제로 변경한다
   *
   * @param session
   * @return
   */
  @UserLoginCheck
  @LetterTypeValidation
  @DeleteMapping("/{type}/{letterId}")
  public ResponseDto deleteLetter(
      @PathVariable @NotBlank(message = LetterConstants.LETTER_TYPE_NULL) LetterType type,
      @PathVariable @NotNull(message = LetterConstants.LETTER_ID_NULL) int letterId,
      HttpSession session
  ) {
    LetterDto letterDto = LetterDto.builder()
        .letterId(letterId)
        .type(type)
        .build();

    return ResponseUtils.makeToResponseOkDto(
        letterService.deleteLetter(letterDto, session)
    );
  }

  /**
   * 쪽지 리스트를 조회하는 DTO 생성
   *
   * @param userId
   * @param pageNum
   * @param limitNum
   * @param type
   * @return
   */
  private LetterSearchDto makeLetterListSearchDto(
      String userId, int pageNum, int limitNum, LetterType type
  ) {
    String searchListStr = "";
    for (int i = 0; i < LetterStatus.SEARCH_STATUS_ARR.length; i++) {
      searchListStr += "\'" + LetterStatus.SEARCH_STATUS_ARR[i] + "\'";
      if (i != LetterStatus.SEARCH_STATUS_ARR.length - 1) {
        searchListStr += ",";
      }
    }

    return LetterSearchDto.builder()
        .userId(userId)
        .statusArr(searchListStr)
        .pageNum(pageNum * limitNum)
        .limitNum(limitNum)
        .letterType(type)
        .build();
  }
}
