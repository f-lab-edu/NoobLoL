package com.nooblol.board.mapper;

import com.nooblol.board.dto.ReplyDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ArticleReplyMapper {

  int upsertReply(ReplyDto insertDto);

  int selectMaxReplyId();

  int selectMaxSortNoByArticleId(int articleId);

  String selectCreatedUserIdByReplyId(int replyId);

  int deleteReplyByReplyId(int replyId);

  int deleteReplyByArticleId(int articleId);

}