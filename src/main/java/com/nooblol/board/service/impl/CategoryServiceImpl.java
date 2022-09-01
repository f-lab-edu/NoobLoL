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
    for (BoardStatusEnum enumObj : BoardStatusEnum.values()) {
      if (enumObj.getStatus() == status) {
        return categoryMapper.selectCategory(enumObj.getStatus());
      }
    }
    return null;
  }

  @Override
  @Transactional(readOnly = true)
  public List<BbsDto> getBbsList(int categoryId, int status) {
    for (BoardStatusEnum enumObj : BoardStatusEnum.values()) {
      if (enumObj.getStatus() == status) {
        SearchBbsListDto searchParamDto =
            new SearchBbsListDto().builder()
                .categoryId(categoryId)
                .status(status)
                .build();
        return categoryMapper.selectBbsList(searchParamDto);
      }
    }

    return null;
  }
}
