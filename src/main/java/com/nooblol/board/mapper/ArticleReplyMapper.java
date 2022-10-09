package com.nooblol.board.mapper;

import com.nooblol.board.dto.ReplyDto;
import java.util.ArrayList;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ArticleReplyMapper {

  int insertReply(ReplyDto insertDto);

  int updateReply(ReplyDto updateDto);

  String selectCreatedUserIdByReplyId(int replyId);

  int deleteReplyByReplyId(int replyId);

  int deleteReplyByArticleId(int articleId);

  ReplyDto selectReplyByReplyId(int replyId);

  ArrayList<ReplyDto> selectReplyListByArticleId(int articleId);

}
