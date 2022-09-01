package com.nooblol.board.service;

import com.nooblol.board.dto.CategoryDto;
import java.util.List;

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

}
