package com.nooblol.board.mapper;

import com.nooblol.board.dto.BbsDto;
import com.nooblol.board.dto.BbsRequestDto.BbsDeleteDto;
import com.nooblol.board.dto.BbsRequestDto.BbsInsertDto;
import com.nooblol.board.dto.BbsRequestDto.BbsUpdateDto;
import com.nooblol.board.dto.CategoryDto;
import com.nooblol.board.dto.CategoryRequestDto.CategoryDeleteDto;
import com.nooblol.board.dto.CategoryRequestDto.CategoryInsertDto;
import com.nooblol.board.dto.CategoryRequestDto.CategoryUpdateDto;
import com.nooblol.board.dto.SearchBbsListDto;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper {

  List<CategoryDto> selectCategoryList(int active);

  CategoryDto selectCategoryByCategoryId(int categoryId);

  List<BbsDto> selectBbsList(SearchBbsListDto searchBbsListDto);

  List<BbsDto> selectAllBbsList();

  int insertCategory(CategoryInsertDto categoryInsertDto);

  int updateCategory(CategoryUpdateDto categoryUpdateDto);

  int deleteCategory(CategoryDeleteDto makeCategoryDeleteDto);

  BbsDto selectBbsByBbsId(int bbsId);

  int insertBbs(BbsInsertDto bbsInsertDto);

  int updateBbs(BbsUpdateDto bbsUpdateDto);

  int deleteBbs(BbsDeleteDto bbsDeleteDto);
}
