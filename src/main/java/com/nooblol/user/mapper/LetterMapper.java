package com.nooblol.user.mapper;

import com.nooblol.user.dto.LetterDto;
import com.nooblol.user.dto.LetterSearchDto;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LetterMapper {

  int insertLetter(LetterDto letterDto);

  LetterDto selectLetterByLetterId(int letterId);

  List<LetterDto> selectLetterListByUserIdAndTypeTo(LetterSearchDto letterSearchDto);

  List<LetterDto> selectLetterListByUserIdAndTypeFrom(LetterSearchDto letterSearchDto);

  int updateLetterToStatusByLetterIdAndToUserId(LetterDto letterDto);

  int updateLetterFromStatusByLetterIdAndFromUserId(LetterDto letterDto);
}
