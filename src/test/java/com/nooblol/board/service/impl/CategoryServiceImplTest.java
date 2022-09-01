package com.nooblol.board.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import com.nooblol.board.dto.CategoryDto;
import com.nooblol.board.mapper.CategoryMapper;
import com.nooblol.board.utils.BoardStatusEnum;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

  @InjectMocks
  private CategoryServiceImpl categoryService;

  @Mock
  private CategoryMapper categoryMapper;

  @Test
  @DisplayName("카테고리를 조회시 Enum에 없는 값인 경우 Null이 반환된다")
  void getCategoryList_WhenNoHaveEnumThenReturnNull() {
    //given
    int noHaveStatus = 999;

    //mock

    //when

    //then
    assertEquals(categoryService.getCategoryList(noHaveStatus), null);
  }

  @Test
  @DisplayName("카테고리를 조회시 Enum에 없는 값인 경우 Null이 반환된다")
  void getCategoryList_WhenNoHaveEnumThenReturnListCategory() {
    //given
    int haveStatus = BoardStatusEnum.ACTIVE.getStatus();
    List<CategoryDto> mockCategoryList = new ArrayList<>();

    CategoryDto mockCategoryDto1 = new CategoryDto();
    mockCategoryDto1.setCategoryId(1);
    mockCategoryDto1.setCategoryName("샘플1");
    mockCategoryDto1.setStatus(BoardStatusEnum.ACTIVE.getStatus());
    mockCategoryDto1.setCreatedUserId("a");
    mockCategoryDto1.setUpdatedUserId("a");
    mockCategoryDto1.setCreatedAt(new Timestamp(System.currentTimeMillis()));
    mockCategoryDto1.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

    CategoryDto mockCategoryDto2 = new CategoryDto();
    mockCategoryDto2.setCategoryId(2);
    mockCategoryDto2.setCategoryName("샘플2");
    mockCategoryDto2.setStatus(BoardStatusEnum.ACTIVE.getStatus());
    mockCategoryDto2.setCreatedUserId("a");
    mockCategoryDto2.setUpdatedUserId("a");
    mockCategoryDto2.setCreatedAt(new Timestamp(System.currentTimeMillis()));
    mockCategoryDto2.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

    mockCategoryList.add(mockCategoryDto1);
    mockCategoryList.add(mockCategoryDto2);

    //mock
    when(categoryMapper.selectCategory(haveStatus)).thenReturn(mockCategoryList);

    //when
    List<CategoryDto> result = categoryService.getCategoryList(haveStatus);

    //then
    assertEquals(result, mockCategoryList);
  }
}