package com.nooblol.party.service;

import com.nooblol.party.dto.PartyCategoryDto;
import java.util.List;
import javax.servlet.http.HttpSession;

public interface PartyCategoryService {

  PartyCategoryDto getPartyCategoryByCategoryId(int categoryId);

  List<PartyCategoryDto> getPartyCategoryList();

  boolean insertPartyCategory(HttpSession session);

  boolean updatePartyCategory();

  boolean deletePartyCategory();

  /**
   * Status의 변경시 공통 사용
   *
   * @return
   */
  boolean updatePartyCategoryStatus();


}
