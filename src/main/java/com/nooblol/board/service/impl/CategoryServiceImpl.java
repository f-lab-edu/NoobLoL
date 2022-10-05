package com.nooblol.board.service.impl;

import com.nooblol.board.dto.BbsDto;
import com.nooblol.board.dto.BbsInsertDto;
import com.nooblol.board.dto.BbsUpdateDto;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.apache.commons.lang3.StringUtils;
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

  @Override
  public boolean insertCategory(CategoryInsertDto categoryInsertDto, HttpSession session) {
    String reqUserId = Optional.of(SessionUtils.getSessionUserId(session)).get();
    categoryInsertDto.setCreatedUserId(reqUserId);
    categoryInsertDto.setUpdatedUserId(reqUserId);

    return categoryMapper.insertCategory(categoryInsertDto) > 0;
  }

  @Override
  public boolean updateCategory(CategoryUpdateDto categoryUpdateDto, HttpSession session) {
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

  @Override
  public boolean insertBbs(BbsInsertDto bbsInsertDto, HttpSession session) {
    String createdUserId = SessionUtils.getSessionUserId(session);

    bbsInsertDto.setCreatedUserId(createdUserId);
    bbsInsertDto.setUpdatedUserId(createdUserId);
    bbsInsertDto.setCreatedAt(LocalDateTime.now());
    bbsInsertDto.setUpdatedAt(LocalDateTime.now());

    return categoryMapper.insertBbs(bbsInsertDto) > 0;
  }

  @Override
  public boolean updateBbs(BbsUpdateDto bbsUpdateDto, HttpSession session) {
    BbsDto dbBbsData = Optional.of(getBbsDataByBbsId(bbsUpdateDto.getBbsId())).get();

    isChangeBbsData(bbsUpdateDto, dbBbsData);
    bbsUpdateDto.setUpdatedUserId(SessionUtils.getSessionUserId(session));
    bbsUpdateDto.setUpdatedAt(LocalDateTime.now());
    return categoryMapper.updateBbs(bbsUpdateDto) > 0;
  }

  @Override
  public boolean deleteBbs(int bbsId, HttpSession session) {
    if (ObjectUtils.isEmpty(getBbsDataByBbsId(bbsId))) {
      log.warn("[deleteBbsData " + ExceptionMessage.NOT_FOUND + "]", bbsId);
      throw new IllegalArgumentException(ExceptionMessage.NO_DATA);
    }
    BbsDto deleteDto = new BbsDto().builder()
        .bbsId(bbsId)
        .status(BoardStatusEnum.DELETE)
        .updatedUserId(SessionUtils.getSessionUserId(session))
        .updatedAt(LocalDateTime.now())
        .build();
    return categoryMapper.deleteBbs(deleteDto) > 0;
  }

  // DTO에 놓는게 맞을지, 여기두는게 맞을지

  /**
   * BBS의 Update할 정보 기본값 세팅
   *
   * @param bbsUpdateDto
   * @param dbBbsDto
   */
  private void isChangeBbsData(BbsUpdateDto bbsUpdateDto, BbsDto dbBbsDto) {
    if (ObjectUtils.isEmpty(bbsUpdateDto.getCategoryId())) {
      bbsUpdateDto.setCategoryId(dbBbsDto.getCategoryId());
    }

    if (StringUtils.isBlank(bbsUpdateDto.getBbsName())) {
      bbsUpdateDto.setBbsName(dbBbsDto.getBbsName());
    }

    if (ObjectUtils.isEmpty(bbsUpdateDto.getStatus())) {
      bbsUpdateDto.setStatus(dbBbsDto.getStatus());
    }

    //수정할 데이터와 DB의 데이터가 동일한경우
    if (bbsUpdateDto.getCategoryId().equals(dbBbsDto.getCategoryId()) &&
        bbsUpdateDto.getBbsName().equals(dbBbsDto.getBbsName()) &&
        bbsUpdateDto.getStatus().equals(dbBbsDto.getStatus())) {
      throw new IllegalArgumentException(ExceptionMessage.BAD_REQUEST);
    }

  }

  private CategoryDeleteDto makeCategoryDeleteDto(int categoryId, HttpSession session) {
    return new CategoryDeleteDto().builder()
        .categoryId(categoryId)
        .status(BoardStatusEnum.DELETE.getStatus())
        .updatedUserId(SessionUtils.getSessionUserId(session))
        .updatedAt(LocalDateTime.now())
        .build();
  }

  private BbsDto getBbsDataByBbsId(int bbsId) {
    return categoryMapper.selectBbsByBbsId(bbsId);
  }

}
