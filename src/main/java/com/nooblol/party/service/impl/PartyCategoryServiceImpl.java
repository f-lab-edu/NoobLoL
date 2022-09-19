package com.nooblol.party.service.impl;

import com.nooblol.party.dto.PartyCategoryDto;
import com.nooblol.party.mapper.PartyCategoryMapper;
import com.nooblol.party.service.PartyCategoryService;
import java.util.List;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PartyCategoryServiceImpl implements PartyCategoryService {

  private final PartyCategoryMapper partyCategoryMapper;

  @Override
  public PartyCategoryDto getPartyCategoryByCategoryId(int categoryId) {
    return null;
  }

  @Override
  public List<PartyCategoryDto> getPartyCategoryList() {
    return null;
  }

  @Override
  public boolean insertPartyCategory(HttpSession session) {
    return false;
  }

  @Override
  public boolean updatePartyCategory() {
    return false;
  }

  @Override
  public boolean deletePartyCategory() {
    return false;
  }

  @Override
  public boolean updatePartyCategoryStatus() {
    return false;
  }
}
