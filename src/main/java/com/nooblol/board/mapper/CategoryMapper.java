package com.nooblol.board.mapper;

import com.nooblol.board.dto.BbsDto;
import com.nooblol.board.dto.CategoryDto;
import com.nooblol.board.dto.SearchBbsListDto;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.cache.annotation.CacheEvict;

@Mapper
public interface CategoryMapper {

  @CacheEvict(value = "category", allEntries = true, key = "#status")
  List<CategoryDto> selectCategory(int status);

  @CacheEvict(value = "bbs", allEntries = true, key = "#searchBbsListDto.categoryId")
  List<BbsDto> selectBbsList(SearchBbsListDto searchBbsListDto);

  @CacheEvict(value = "allBbs", allEntries = true)
  List<BbsDto> selectAllBbsList();

}
