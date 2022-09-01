package com.nooblol.board.mapper;

import com.nooblol.board.dto.CategoryDto;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper {

  List<CategoryDto> selectCategory(int active);
}
