package com.nooblol.board.service.impl;

import com.nooblol.board.dto.BbsDto;
import com.nooblol.board.dto.CategoryDto;
import com.nooblol.board.dto.CategoryRequestDto.CategoryDeleteDto;
import com.nooblol.board.dto.CategoryRequestDto.CategoryInsertDto;
import com.nooblol.board.dto.CategoryRequestDto.CategoryUpdateDto;
import com.nooblol.board.dto.SearchBbsListDto;
import com.nooblol.board.mapper.CategoryMapper;
import com.nooblol.board.service.CategoryService;
import com.nooblol.board.utils.BoardStatusEnum;
import com.nooblol.global.exception.ExceptionMessage;
import com.nooblol.global.utils.SessionUtils;
import com.nooblol.user.utils.UserRoleStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

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
        return categoryMapper.selectCategoryList(enumObj.getStatus());
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

  @Override
  public List<BbsDto> getAllBbsList() {
    return categoryMapper.selectAllBbsList();
  }

  @Override
  public boolean insertCategory(CategoryInsertDto categoryInsertDto, HttpSession session) {
    isSessionUserIsAdmin(session);

    String reqUserId = Optional.of(SessionUtils.getSessionUserId(session)).get();
    categoryInsertDto.setCreatedUserId(reqUserId);
    categoryInsertDto.setUpdatedUserId(reqUserId);

    return categoryMapper.insertCategory(categoryInsertDto) > 0;
  }

  @Override
  public boolean updateCategory(CategoryUpdateDto categoryUpdateDto, HttpSession session) {
    isSessionUserIsAdmin(session);

    CategoryDto dbCategoryData = selectCategory(categoryUpdateDto.getCategoryId());

    isChangeCategoryData(categoryUpdateDto, dbCategoryData);
    categoryUpdateDto.setUpdatedUserId(SessionUtils.getSessionUserId(session));
    categoryUpdateDto.setUpdatedAt(LocalDateTime.now());
    return categoryMapper.updateCategory(categoryUpdateDto) > 0;

  }

  private void isChangeCategoryData(CategoryUpdateDto categoryUpdateDto,
      CategoryDto dbCategoryData) {
    if (dbCategoryData == null) {
      log.warn(
          ExceptionMessage.NOT_FOUND,
          categoryUpdateDto.getCategoryId()
      );
      throw new IllegalArgumentException(ExceptionMessage.NO_DATA);
    }

    // TODO : IFNULL을 쿼리에서 사용할지, 파라미터에 그냥 넣어서 보낼지를 고민함.
    if (ObjectUtils.isEmpty(categoryUpdateDto.getNewCategoryName())) {
      categoryUpdateDto.setNewCategoryName(dbCategoryData.getCategoryName());
    }

    if (ObjectUtils.isEmpty(categoryUpdateDto.getStatus())) {
      categoryUpdateDto.setStatus(dbCategoryData.getStatus());
    }

    //변경점이 없는 경우 Exception
    if (categoryUpdateDto.getNewCategoryName().equals(dbCategoryData.getCategoryName())
        && categoryUpdateDto.getStatus().equals(dbCategoryData.getStatus())) {
      throw new IllegalArgumentException(ExceptionMessage.BAD_REQUEST);
    }
  }

  @Override
  public CategoryDto selectCategory(int categoryId) {
    return categoryMapper.selectCategoryByCategoryId(categoryId);
  }

  @Override
  public boolean deleteCategory(int categoryId, HttpSession session) {
    isSessionUserIsAdmin(session);

    CategoryDto dbCategoryData = selectCategory(categoryId);

    if (ObjectUtils.isEmpty(dbCategoryData)) {
      log.warn(ExceptionMessage.NOT_FOUND, categoryId);
      throw new IllegalArgumentException(ExceptionMessage.NO_DATA);
    }

    if (dbCategoryData.getStatus() == BoardStatusEnum.DELETE.getStatus()) {
      return true;
    }

    return categoryMapper.deleteCategory(
        makeCategoryDeleteDto(categoryId, session)
    ) > 0;
  }

  private CategoryDeleteDto makeCategoryDeleteDto(int categoryId, HttpSession session) {
    return new CategoryDeleteDto().builder()
        .categoryId(categoryId)
        .status(BoardStatusEnum.DELETE.getStatus())
        .updatedUserId(SessionUtils.getSessionUserId(session))
        .updatedAt(LocalDateTime.now())
        .build();
  }


  private void isSessionUserIsAdmin(HttpSession session) {
    Integer sessionUserRole = Optional.of(SessionUtils.getSessionUserRole(session)).get();

    if (sessionUserRole != UserRoleStatus.ADMIN.getRoleValue()) {
      throw new IllegalArgumentException(ExceptionMessage.FORBIDDEN);
    }
  }
}
