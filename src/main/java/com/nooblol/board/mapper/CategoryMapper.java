package com.nooblol.board.mapper;

import com.nooblol.board.dto.BbsDto;
import com.nooblol.board.dto.BbsInsertDto;
import com.nooblol.board.dto.BbsUpdateDto;
import com.nooblol.board.dto.CategoryDto;
import com.nooblol.board.dto.CategoryInsertDto;
import com.nooblol.board.dto.CategoryUpdateDto;
import com.nooblol.board.dto.SearchBbsListDto;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.cache.annotation.CacheEvict;

@Mapper
public interface CategoryMapper {

  @CacheEvict(value = "category", allEntries = true, key = "#status")
  List<CategoryDto> selectCategoryList(int status);

  CategoryDto selectCategoryByCategoryId(int categoryId);

  @CacheEvict(value = "bbs", allEntries = true, key = "#searchBbsListDto.categoryId")
  List<BbsDto> selectBbsList(SearchBbsListDto searchBbsListDto);

  @CacheEvict(value = "allBbs", allEntries = true)
  List<BbsDto> selectAllBbsList();

  int insertCategory(CategoryInsertDto categoryInsertDto);

  int updateCategory(CategoryUpdateDto categoryUpdateDto);

  int deleteCategory(CategoryDto CategoryDto);

  BbsDto selectBbsByBbsId(int bbsId);

  int insertBbs(BbsInsertDto bbsInsertDto);

  int updateBbs(BbsUpdateDto bbsUpdateDto);

  int deleteBbs(BbsDto bbsDeleteDto);
}
