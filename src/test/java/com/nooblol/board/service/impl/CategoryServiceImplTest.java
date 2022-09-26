package com.nooblol.board.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import com.nooblol.board.dto.BbsDto;
import com.nooblol.board.dto.CategoryDto;
import com.nooblol.board.mapper.CategoryMapper;
import com.nooblol.board.utils.BoardStatusEnum;
import com.nooblol.global.exception.ExceptionMessage;
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
  @DisplayName("카테고리를 조회시 Enum에 없는 값인 경우 BadRequest Exception이 발생한다")
  void getCategoryList_WhenNoHaveEnumThenReturnNull() {
    //given
    int noHaveStatus = 999;

    //mock

    //when
    Exception e = assertThrows(IllegalArgumentException.class, () -> {
      categoryService.getCategoryList(noHaveStatus);
    });
    //then
    assertEquals(e.getMessage(), ExceptionMessage.BAD_REQUEST);
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

  @Test
  @DisplayName("게시판리스트 조회시 Enum에 없는 값인 경우 BadRequestException이 발생한다")
  void getBbsList_WhenNoHaveEnumThenReturnNull() {
    //given
    int noHaveCategoryId = 999;
    int noHaveStatus = 999;

    //mock

    //when
    Exception e = assertThrows(IllegalArgumentException.class, () ->
        categoryService.getBbsList(noHaveCategoryId, noHaveStatus)
    );

    //then
    assertEquals(e.getMessage(), ExceptionMessage.BAD_REQUEST);
  }

  @Test
  @DisplayName("게시판리스트 조회시 Enum에 없는 값인 경우 Null이 반환된다")
  void getBbsList_WhenNoHaveEnumThenReturnListCategory() {
    //given
    int haveStatus = BoardStatusEnum.ACTIVE.getStatus();
    int haveCategoryId = 1;

    List<BbsDto> mockBbsList = new ArrayList<>();

    BbsDto mockBbsDto1 = new BbsDto().builder()
        .bbsId(1)
        .bbsName("샘플1")
        .status(BoardStatusEnum.ACTIVE.getStatus())
        .categoryId(haveCategoryId)
        .createdUserId("test1").updatedUserId("test1")
        .createdAt(new Timestamp(System.currentTimeMillis()))
        .updatedAt(new Timestamp(System.currentTimeMillis()))
        .build();

    BbsDto mockBbsDto2 = new BbsDto().builder()
        .bbsId(1)
        .bbsName("샘플2")
        .status(BoardStatusEnum.ACTIVE.getStatus())
        .categoryId(haveCategoryId)
        .createdUserId("test1").updatedUserId("test1")
        .createdAt(new Timestamp(System.currentTimeMillis()))
        .updatedAt(new Timestamp(System.currentTimeMillis()))
        .build();

    mockBbsList.add(mockBbsDto1);
    mockBbsList.add(mockBbsDto2);

    //mock
    when(categoryMapper.selectBbsList(any())).thenReturn(mockBbsList);

    //when
    List<BbsDto> result = categoryService.getBbsList(haveCategoryId, haveStatus);

    //then
    assertEquals(result, mockBbsList);
  }

}