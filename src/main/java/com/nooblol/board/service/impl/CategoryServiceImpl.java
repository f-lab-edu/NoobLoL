package com.nooblol.board.service.impl;

import com.nooblol.board.dto.BbsDto;
import com.nooblol.board.dto.CategoryDto;
import com.nooblol.board.dto.SearchBbsListDto;
import com.nooblol.board.mapper.CategoryMapper;
import com.nooblol.board.service.CategoryService;
import com.nooblol.board.utils.BoardStatusEnum;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

  private final CategoryMapper categoryMapper;

  @Override
  @Transactional(readOnly = true)
  public List<CategoryDto> getCategoryList(int status) {
    if (BoardStatusEnum.isExistStatus(status)) {
      return categoryMapper.selectCategory(status);
    }
    return null;
  }

  @Override
  @Transactional(readOnly = true)
  public List<BbsDto> getBbsList(int categoryId, int status) {
    if (BoardStatusEnum.isExistStatus(status)) {
      SearchBbsListDto searchParamDto =
          new SearchBbsListDto().builder()
              .categoryId(categoryId)
              .status(status)
              .build();
      return categoryMapper.selectBbsList(searchParamDto);
    }
    return null;
  }

  @Override
  public List<BbsDto> getAllBbsList() {
    return categoryMapper.selectAllBbsList();
  }
}
