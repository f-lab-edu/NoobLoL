package com.nooblol.board.utils;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;


/*
ReferenceList
  https://umbum.dev/1122
  https://exchangetuts.com/java-mybatis-enum-string-value-1640689684810781
 */
public class ArticleLikeStatusEnumTypeHandler implements TypeHandler<ArticleLikeStatusEnum> {


  @Override
  public void setParameter(PreparedStatement ps, int i, ArticleLikeStatusEnum parameter,
      JdbcType jdbcType) throws SQLException {
    ps.setBoolean(i, parameter.likeStatus);
  }

  @Override
  public ArticleLikeStatusEnum getResult(ResultSet rs, String columnName) throws SQLException {
//    return ArticleLikeStatusEnum.findLikeStatusType(rs.getBoolean(columnName));
    return ArticleLikeStatusEnum.findLikeStatusByInt(rs.getInt(columnName));
  }

  @Override
  public ArticleLikeStatusEnum getResult(ResultSet rs, int columnIndex) throws SQLException {
//    return ArticleLikeStatusEnum.findLikeStatusType(rs.getBoolean(columnIndex));
    return ArticleLikeStatusEnum.findLikeStatusByInt(rs.getInt(columnIndex));
  }

  @Override
  public ArticleLikeStatusEnum getResult(CallableStatement cs, int columnIndex)
      throws SQLException {
//    return ArticleLikeStatusEnum.findLikeStatusType(cs.getBoolean(columnIndex));
    return ArticleLikeStatusEnum.findLikeStatusByInt(cs.getInt(columnIndex));
  }
}
