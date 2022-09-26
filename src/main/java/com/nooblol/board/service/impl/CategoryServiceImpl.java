package com.nooblol.board.service.impl;

import com.nooblol.board.dto.BbsDto;
import com.nooblol.board.dto.CategoryDto;
import com.nooblol.board.dto.SearchBbsListDto;
import com.nooblol.board.mapper.CategoryMapper;
import com.nooblol.board.service.CategoryService;
import com.nooblol.board.utils.BoardStatusEnum;
import com.nooblol.global.exception.ExceptionMessage;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

  private final CategoryMapper categoryMapper;

  @Override
  @Transactional(readOnly = true)
  @Cacheable(cacheNames = "category", key = "#status")
  public List<CategoryDto> getCategoryList(int status) {
    if (BoardStatusEnum.isExistStatus(status)) {
      return categoryMapper.selectCategory(status);
    }
    throw new IllegalArgumentException(ExceptionMessage.BAD_REQUEST);
  }

  @Override
  @Transactional(readOnly = true)
  @Cacheable(cacheNames = "bbs", key = "#categoryId")
  public List<BbsDto> getBbsList(int categoryId, int status) {
    if (BoardStatusEnum.isExistStatus(status)) {
      return categoryMapper.selectBbsList(
          new SearchBbsListDto().builder()
              .categoryId(categoryId)
              .status(status)
              .build()
      );
    }
    throw new IllegalArgumentException(ExceptionMessage.BAD_REQUEST);
  }

  @Override
  @Transactional(readOnly = true)
  @Cacheable(cacheNames = "allBbs")
  public List<BbsDto> getAllBbsList() {
    return categoryMapper.selectAllBbsList();
  }
}
