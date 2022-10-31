package com.nooblol.board.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import com.nooblol.board.dto.BbsDto;
import com.nooblol.board.dto.BbsInsertDto;
import com.nooblol.board.dto.BbsUpdateDto;
import com.nooblol.board.dto.CategoryDto;
import com.nooblol.board.dto.CategoryInsertDto;
import com.nooblol.board.dto.CategoryUpdateDto;
import com.nooblol.board.mapper.CategoryMapper;
import com.nooblol.board.utils.BoardStatus;
import com.nooblol.board.utils.CategoryStatus;
import com.nooblol.global.exception.ExceptionMessage;
import com.nooblol.global.utils.SessionSampleObject;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
@DisplayName("Category Service Test")
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
class CategoryServiceImplTest {

  @InjectMocks
  CategoryServiceImpl categoryService;

  @Mock
  CategoryMapper categoryMapper;

  HttpSession authUserSession = SessionSampleObject.authUserLoginSession;

  HttpSession adminSession = SessionSampleObject.adminUserLoginSession;

  @Order(1)
  @Nested
  @DisplayName("카테고리 테스트")
  class CategoryTest {

    @Nested
    @DisplayName("조회 테스트")
    class SelectTest {

      @Test
      @DisplayName("카테고리를 조회시 Enum에 없는 값인 경우 BadRequest Exception이 발생한다")
      void getCategoryList_WhenNoHaveEnum_ThenBadRequestException() {
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
      @DisplayName("카테고리를 조회시 상태값이 Enum에 존재하는 값인 경우에는 해당 Status와 일치하는 카테고리 리스트를 획득한다")
      void getCategoryList_WhenNoHaveEnumThenReturnListCategory() {
        //given
        int haveStatus = BoardStatus.ACTIVE.getStatus();
        List<CategoryDto> mockCategoryList = new ArrayList<>();

        mockCategoryList.add(
            new CategoryDto().builder()
                .categoryId(1)
                .categoryName("샘플1")
                .status(CategoryStatus.ACTIVE)
                .createdUserId("a")
                .updatedUserId("a")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build()
        );
        mockCategoryList.add(
            new CategoryDto().builder()
                .categoryId(2)
                .categoryName("샘플2")
                .status(CategoryStatus.ACTIVE)
                .createdUserId("a")
                .updatedUserId("a")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build()
        );

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
      @DisplayName("카테고리의 수정시, 변경되는 데이터가 없는 경우 BadRequestException이 발생한다")
      void updateCategory_WhenNoDataToChange_ThenBadRequestException() {
        //given
        int categoryId = 1;
        String sameTitle = "title";

        CategoryUpdateDto updateDto = new CategoryUpdateDto().builder()
            .categoryId(categoryId)
            .newCategoryName(sameTitle)
            .status(CategoryStatus.ACTIVE)
            .build();

        CategoryDto mockCategoryDto = new CategoryDto().builder()
            .categoryId(categoryId)
            .categoryName(sameTitle)
            .status(CategoryStatus.ACTIVE)
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
            .status(CategoryStatus.ACTIVE)
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
      @DisplayName("카테고리를 삭제 할 때, 이미 DB에서 상태값이 삭제가 되어있는 경우 BadRequestException이 발생한다")
      void deleteCategory_WhenCategoryAlreadyDeleteThenReturnTrue() {
        //given
        int categoryId = 1;

        CategoryDto mockCategoryDto = new CategoryDto().builder()
            .categoryId(categoryId)
            .status(CategoryStatus.DELETE)
            .build();

        //mock
        when(categoryMapper.selectCategoryByCategoryId(categoryId)).thenReturn(mockCategoryDto);

        //when
        Exception result = assertThrows(IllegalArgumentException.class, () -> {
          categoryService.deleteCategory(categoryId, adminSession);
        });

        //then
        assertEquals(result.getMessage(), ExceptionMessage.BAD_REQUEST);
      }

      @Test
      @DisplayName("카테고리를 삭제 할 때, 삭제가 이뤄진 경우 결과값으로 true를 획득한다")
      void deleteCategory_WhenUpdateStatusDeleteSuccessThenReturnTrue() {
        //given
        int categoryId = 1;

        CategoryDto mockCategoryDto = new CategoryDto().builder()
            .categoryId(categoryId)
            .status(CategoryStatus.ACTIVE)
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

  @Order(2)
  @Nested
  @DisplayName("게시판 테스트 ")
  class BbsTest {

    @Nested
    @DisplayName("조회 테스트")
    class SelectTest {

      @Test
      @DisplayName("게시판리스트 조회시 Enum에 없는 값인 경우 BadRequestException이 발생한다")
      void getBbsList_WhenNoHaveEnum_ThenBadRequestException() {
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
      @DisplayName("게시판리스트 조회시 조회하고자 하는 Status가 Enum에 존재하고, 카테고리Id도 존재하는 경우, 해당 항목과 일치하는 값을 가진 게시판 리스트를 획득한다.")
      void getBbsList_WhenNoHaveEnum_ThenReturnListCategory() {
        //given
        int haveStatus = BoardStatus.ACTIVE.getStatus();
        int haveCategoryId = 1;

        List<BbsDto> mockBbsList = new ArrayList<>();

        BbsDto mockBbsDto1 = new BbsDto().builder()
            .bbsId(1)
            .bbsName("샘플1")
            .status(BoardStatus.ACTIVE)
            .categoryId(haveCategoryId)
            .createdUserId("test1").updatedUserId("test1")
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

        BbsDto mockBbsDto2 = new BbsDto().builder()
            .bbsId(1)
            .bbsName("샘플2")
            .status(BoardStatus.ACTIVE)
            .categoryId(haveCategoryId)
            .createdUserId("test1").updatedUserId("test1")
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
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

      @Nested
      @DisplayName("생성 테스트")
      class InsertTest {

        @Test
        @DisplayName("게시판의 생성시, 정상적으로 데이터가 삽입이 되었다면 결과값으로 True를 획득한다")
        void insertBbs_WhenInsertSuccessThenReturnTrue() {
          //given
          BbsInsertDto bbsInsertDto = new BbsInsertDto();

          //mock
          when(categoryMapper.insertBbs(bbsInsertDto)).thenReturn(1);

          //when
          boolean result = categoryService.insertBbs(bbsInsertDto, adminSession);

          //then
          assertTrue(result);
        }
      }

      @Nested
      @DisplayName("수정 테스트")
      class UpdateTest {

        @Test
        @DisplayName("게시판의 정보를 수정하고자 할 때, 해당 게시판정보가 DB에 없는 경우 NPE가 발생한다.")
        void updateBbs_WhenDbNoHaveBbsDataThenNullPointerException() {
          //given
          int nullBbsId = 99999;
          BbsUpdateDto mockBbsUpdateDto = new BbsUpdateDto().builder().bbsId(nullBbsId).build();

          //mock
          when(categoryMapper.selectBbsByBbsId(nullBbsId)).thenReturn(null);

          //when
          Exception e = assertThrows(NullPointerException.class, () -> {
            categoryService.updateBbs(mockBbsUpdateDto, adminSession);
          });

          assertEquals(e.getClass(), NullPointerException.class);
        }

        @Test
        @DisplayName("게시판의 정보 수정시 할 때, 해당 정보와 DB의 게시판정보가 동일한 경우 BadRequest Exception이 발생한다")
        void updateBbs_WhenDbEqualsUpdateDataThenBadRequestException() {
          //given
          int bbsId = 1;
          BbsUpdateDto bbsUpdateDto = new BbsUpdateDto().builder()
              .bbsId(bbsId)
              .categoryId(1)
              .bbsName("test")
              .status(BoardStatus.ACTIVE)
              .build();

          BbsDto mockBbsDto = new BbsDto().builder()
              .bbsId(bbsId)
              .categoryId(1)
              .bbsName("test")
              .status(BoardStatus.ACTIVE)
              .build();

          //mock
          when(categoryMapper.selectBbsByBbsId(bbsId)).thenReturn(mockBbsDto);

          //when
          Exception e = assertThrows(IllegalArgumentException.class, () -> {
            categoryService.updateBbs(bbsUpdateDto, adminSession);
          });

          //then
          assertEquals(e.getMessage(), ExceptionMessage.BAD_REQUEST);
        }

        @Test
        @DisplayName("게시판의 정보 수정시, 수정된 데이터가 존재하는 경우 Return으로 True를 획득한다")
        void updateBbs_WhenDbDataUpdateSuccessThenReturnTrue() {
          //given
          int bbsId = 1;
          BbsUpdateDto bbsUpdateDto = new BbsUpdateDto().builder()
              .bbsId(bbsId)
              .categoryId(1)
              .bbsName("test")
              .status(BoardStatus.ACTIVE)
              .build();

          BbsDto mockBbsDto = new BbsDto().builder()
              .bbsId(bbsId)
              .categoryId(5)
              .bbsName("sample")
              .status(BoardStatus.ACTIVE)
              .build();

          //mock
          when(categoryMapper.selectBbsByBbsId(bbsId)).thenReturn(mockBbsDto);
          when(categoryMapper.updateBbs(bbsUpdateDto)).thenReturn(1);

          //when
          boolean result = categoryService.updateBbs(bbsUpdateDto, adminSession);

          //then
          assertTrue(result);
        }
      }

      @Nested
      @DisplayName("삭제 테스트")
      class DeleteTest {

        @Test
        @DisplayName("파라미터로 받은 게시판ID가 DB에 데이터가 없는 경우 NotFoundException이 발생한다")
        void deleteBbs_WhenDbNotFoundBbsThenNotFoundException() {
          //given
          int noBbsId = 99999;

          //mock
          when(categoryMapper.selectBbsByBbsId(noBbsId)).thenReturn(null);

          //when
          Exception e = assertThrows(IllegalArgumentException.class, () -> {
            categoryService.deleteBbs(noBbsId, adminSession);
          });

          //then
          assertEquals(e.getMessage(), ExceptionMessage.NO_DATA);
        }
      }

      @Test
      @DisplayName("파라미터로 받은 게시판ID가 DB에 존재하고, Status값을 정상적으로 Update한 경우 결과값으로 True를 획득한다.")
      void deleteBbs_WhenDeleteBbsSuccessThenReturnTrue() {
        //given
        int bbsId = 1;

        //mock
        when(categoryMapper.selectBbsByBbsId(bbsId)).thenReturn(new BbsDto());
        when(categoryMapper.deleteBbs(any())).thenReturn(1);

        //when
        boolean result = categoryService.deleteBbs(bbsId, adminSession);

        //then
        assertTrue(result);
      }
    }
  }
}

