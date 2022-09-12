package com.nooblol.board.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import com.nooblol.board.dto.BbsDto;
import com.nooblol.board.dto.CategoryDto;
import com.nooblol.board.dto.CategoryRequestDto.CategoryInsertDto;
import com.nooblol.board.dto.CategoryRequestDto.CategoryUpdateDto;
import com.nooblol.board.mapper.CategoryMapper;
import com.nooblol.board.utils.BoardStatusEnum;
import com.nooblol.global.exception.ExceptionMessage;
import com.nooblol.global.utils.SessionEnum;
import com.nooblol.global.utils.SessionSampleObject;
import com.nooblol.user.dto.UserDto;
import com.nooblol.user.utils.UserRoleStatus;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

  @InjectMocks
  CategoryServiceImpl categoryService;

  @Mock
  CategoryMapper categoryMapper;


  HttpSession authUserSession = SessionSampleObject.authUserLoginSession;
  HttpSession adminSession = SessionSampleObject.adminUserLoginSession;

  @Nested
  @DisplayName("카테고리 테스트")
  class CategoryTest {

    @Nested
    @DisplayName("조회 테스트")
    class SelectTest {

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
        mockCategoryDto1.setCreatedAt(LocalDateTime.now());
        mockCategoryDto1.setUpdatedAt(LocalDateTime.now());

        CategoryDto mockCategoryDto2 = new CategoryDto();
        mockCategoryDto2.setCategoryId(2);
        mockCategoryDto2.setCategoryName("샘플2");
        mockCategoryDto2.setStatus(BoardStatusEnum.ACTIVE.getStatus());
        mockCategoryDto2.setCreatedUserId("a");
        mockCategoryDto2.setUpdatedUserId("a");
        mockCategoryDto2.setCreatedAt(LocalDateTime.now());
        mockCategoryDto2.setUpdatedAt(LocalDateTime.now());

        mockCategoryList.add(mockCategoryDto1);
        mockCategoryList.add(mockCategoryDto2);

        //mock
        when(categoryMapper.selectCategoryList(haveStatus)).thenReturn(mockCategoryList);

        //when
        List<CategoryDto> result = categoryService.getCategoryList(haveStatus);

        //then
        assertEquals(result, mockCategoryList);
      }

    }

    @Nested
    @DisplayName("생성 테스트")
    class InsertTest {

      @Test
      @DisplayName("카테고리 생성시 데이터가 삽입에 성공한 경우 결과값으로 True를 획득힌다.")
      void insertCategory_WhenInsertSuccessThenReturnTrue() {
        //given
        CategoryInsertDto mockCategoryInsertDto = new CategoryInsertDto();

        //mock
        when(categoryMapper.insertCategory(mockCategoryInsertDto)).thenReturn(1);

        //when
        boolean result = categoryService.insertCategory(mockCategoryInsertDto, adminSession);

        //then
        assertTrue(result);
      }
    }

    @Nested
    @DisplayName("수정 테스트")
    class UpdateTest {

      @Test
      @DisplayName("카테고리의 수정시, DB에 수정요청한 카테고리정보가 없는 경우 NotFound Exception이 발생한다")
      void updateCategory_WhenNoFoundCategoryDataThenNotFoundException() {
        //given
        int nullCategoryId = 99999;

        CategoryUpdateDto mockUpdateDto = new CategoryUpdateDto().builder()
            .categoryId(nullCategoryId)
            .build();
        //mock
        when(categoryMapper.selectCategoryByCategoryId(nullCategoryId)).thenReturn(null);

        //when
        Exception e = assertThrows(IllegalArgumentException.class, () -> {
          categoryService.updateCategory(mockUpdateDto, adminSession);
        });

        //then
        assertEquals(e.getMessage(), ExceptionMessage.NO_DATA);
      }

      @Test
      @DisplayName("카테고리의 수정시, 변경되는 데이터가 없는 경우 BadRequest Exception이 발생한다")
      void updateCategory_WhenNoDataToChangeThenBadRequestException() {
        //given
        int categoryId = 1;
        String sameTitle = "title";
        int sameStatus = BoardStatusEnum.ACTIVE.getStatus();

        CategoryUpdateDto updateDto = new CategoryUpdateDto().builder()
            .categoryId(categoryId)
            .newCategoryName(sameTitle)
            .status(sameStatus)
            .build();

        CategoryDto mockCategoryDto = new CategoryDto().builder()
            .categoryId(categoryId)
            .categoryName(sameTitle)
            .status(sameStatus)
            .build();
        //mock
        when(categoryMapper.selectCategoryByCategoryId(categoryId)).thenReturn(mockCategoryDto);

        //when
        Exception e = assertThrows(IllegalArgumentException.class, () -> {
          categoryService.updateCategory(updateDto, adminSession);
        });

        //then
        assertEquals(e.getMessage(), ExceptionMessage.BAD_REQUEST);
      }


      @Test
      @DisplayName("카테고리의 수정시, 데이터의 수정이 이루어진 경우 결과값으로 true를 획득한다")
      void updateCategory_WhenUpdateSuccessThenReturnTrue() {
        //given
        int categoryId = 1;

        CategoryUpdateDto updateDto = new CategoryUpdateDto().builder()
            .categoryId(categoryId)
            .newCategoryName("reqNewCategory")
            .build();

        CategoryDto mockCategoryDto = new CategoryDto().builder()
            .categoryId(categoryId)
            .categoryName("dbTitle")
            .status(BoardStatusEnum.ACTIVE.getStatus())
            .build();
        //mock
        when(categoryMapper.selectCategoryByCategoryId(categoryId)).thenReturn(mockCategoryDto);
        when(categoryMapper.updateCategory(updateDto)).thenReturn(1);

        //when
        boolean result = categoryService.updateCategory(updateDto, adminSession);

        //then
        assertTrue(result);
      }

    }


    @Nested
    @DisplayName("삭제 테스트")
    class DeleteTest {

      @Test
      @DisplayName("카테고리를 삭제 할 때, 카테고리가 DB에 없는 경우, NotFound Exception이 발생한다")
      void deleteCategory_WhenNoHaveCategoryDataThenNotFoundException() {
        //given
        int nullCategoryId = 99999;

        //mock
        when(categoryMapper.selectCategoryByCategoryId(nullCategoryId)).thenReturn(null);

        //when
        Exception e = assertThrows(IllegalArgumentException.class, () -> {
          categoryService.deleteCategory(nullCategoryId, adminSession);
        });

        //then
        assertEquals(e.getMessage(), ExceptionMessage.NO_DATA);
      }

      @Test
      @DisplayName("카테고리를 삭제 할 때, 이미 DB에서 상태값이 삭제가 되어있는 경우 결과값으로 true를 획득한다")
      void deleteCategory_WhenCategoryAlreadyDeleteThenReturnTrue() {
        //given
        int categoryId = 1;

        CategoryDto mockCategoryDto = new CategoryDto().builder()
            .categoryId(categoryId)
            .status(BoardStatusEnum.DELETE.getStatus())
            .build();

        //mock
        when(categoryMapper.selectCategoryByCategoryId(categoryId)).thenReturn(mockCategoryDto);

        //when
        boolean result = categoryService.deleteCategory(categoryId, adminSession);

        //then
        assertTrue(result);
      }

      @Test
      @DisplayName("카테고리를 삭제 할 때, 삭제가 이뤄진 경우 결과값으로 true를 획득한다")
      void deleteCategory_WhenUpdateStatusDeleteSuccessThenReturnTrue() {
        //given
        int categoryId = 1;

        CategoryDto mockCategoryDto = new CategoryDto().builder()
            .categoryId(categoryId)
            .status(BoardStatusEnum.ACTIVE.getStatus())
            .build();

        //mock
        when(categoryMapper.selectCategoryByCategoryId(categoryId)).thenReturn(mockCategoryDto);
        when(categoryMapper.deleteCategory(any())).thenReturn(1);

        //when
        boolean result = categoryService.deleteCategory(categoryId, adminSession);

        //then
        assertTrue(result);
      }
    }


  }

  @Nested
  @DisplayName("게시판 테스트 ")
  class BbsTest {

    @Nested
    @DisplayName("조회 테스트")
    class SelectTest {

      @Test
      @DisplayName("게시판리스트 조회시 Enum에 없는 값인 경우 Null이 반환된다")
      void getBbsList_WhenNoHaveEnumThenReturnNull() {
        //given
        int noHaveCategoryId = 999;
        int noHaveStatus = 999;

        //mock

        //when

        //then
        assertEquals(categoryService.getBbsList(noHaveCategoryId, noHaveStatus), null);
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

  }


}