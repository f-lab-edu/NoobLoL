package com.nooblol.board.service;

import com.nooblol.board.dto.BbsDto;
import com.nooblol.board.dto.BbsInsertDto;
import com.nooblol.board.dto.BbsUpdateDto;
import com.nooblol.board.dto.CategoryDto;
import com.nooblol.board.dto.CategoryInsertDto;
import com.nooblol.board.dto.CategoryUpdateDto;
import java.util.List;
import javax.servlet.http.HttpSession;

public interface CategoryService {

  /**
   * BBS_CATEGORY 테이블에서 파라미터로 받은 status 상태인 데이터를 조회하여 List로 반환한다 Enum에 존재하지 않는 Status인 경우에는 Null이
   * 반환된다.
   * <p>
   * 해당 기능은 조회만 하기 때문에 Transactional Readonly설정이 되어있다.
   *
   * @param status 현재 Category상태값
   * @return
   */
  List<CategoryDto> getCategoryList(int status);

  /**
   * parameter로 받은CategoryId의 하위 리스트중 status가 일치하는 데이터를 BBS테이블에서 조회하여 List로 반환한다
   *
   * @param categoryId 카테고리ID
   * @param status     현재 해당 게시판의 상태값
   * @return
   */
  List<BbsDto> getBbsList(int categoryId, int status);

  /**
   * 상태구분없이 모든 게시판 획득
   *
   * @return
   */
  List<BbsDto> getAllBbsList();

  /**
   * 해당 사용자가 관리자일 경우 카테고리의 추가가 가능하다.
   *
   * @param categoryInsertDto
   * @param session
   * @return
   */
  boolean insertCategory(CategoryInsertDto categoryInsertDto, HttpSession session);

  /**
   * 해당 요청자가 관리자일 경우, 카테고리 정보 수정
   *
   * @param categoryUpdateDto
   * @param session
   * @return
   */
  boolean updateCategory(CategoryUpdateDto categoryUpdateDto, HttpSession session);

  /**
   * 카테고리 한건만 검색한다
   *
   * @param categoryId
   * @return
   */
  CategoryDto selectCategory(int categoryId);

  /**
   * 카테고리의 삭제의 경우에는 실제 DELETE가 이뤄지지 않으며, STATUS의 값만 변경되며, 이미 Status가 삭제된 상태이거나 또는 DB에서 수정이 정상적으로 이뤄진
   * 경우 결과값으로 True가 반환된다.
   * <p>
   * 카테고리를 삭제 -> 게시판의 삭제 -> 게시글의 삭제로 데이터에 삭제해야 하는 데이터가 너무 방대해짐.
   *
   * @param categoryId
   * @param session
   * @return
   */
  boolean deleteCategory(int categoryId, HttpSession session);

  /**
   * 게시판 항목을 추가한다.
   *
   * @param bbsInsertDto
   * @param session
   * @return
   */
  boolean insertBbs(BbsInsertDto bbsInsertDto, HttpSession session);

  /**
   * 게시판의 정보를 수정한다.
   *
   * @param bbsUpdateDto
   * @param session
   * @return
   */
  boolean updateBbs(BbsUpdateDto bbsUpdateDto, HttpSession session);

  /**
   * 게시판의 Status를 삭제값으로 변경한다.
   *
   * @param bbsId
   * @param session
   * @return
   */
  boolean deleteBbs(int bbsId, HttpSession session);

}
