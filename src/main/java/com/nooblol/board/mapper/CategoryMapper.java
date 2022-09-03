package com.nooblol.board.mapper;

import com.nooblol.board.dto.BbsDto;
import com.nooblol.board.dto.CategoryDto;
import com.nooblol.board.dto.SearchBbsListDto;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper {

  List<CategoryDto> selectCategory(int active);

  List<BbsDto> selectBbsList(SearchBbsListDto searchBbsListDto);

  List<BbsDto> selectAllBbsList();
}
