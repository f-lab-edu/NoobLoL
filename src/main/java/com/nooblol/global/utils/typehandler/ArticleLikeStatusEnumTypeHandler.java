package com.nooblol.global.utils.typehandler;

import com.nooblol.board.utils.ArticleLikeStatus;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;


/*
ReferenceList
  https://umbum.dev/1122
  https://exchangetuts.com/java-mybatis-enum-string-value-1640689684810781
 */
public class ArticleLikeStatusEnumTypeHandler extends BaseTypeHandler<ArticleLikeStatus> {

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, ArticleLikeStatus parameter,
      JdbcType jdbcType) throws SQLException {
    ps.setBoolean(i, parameter.isLikeStatus());
  }

  @Override
  public ArticleLikeStatus getNullableResult(ResultSet rs, String columnName)
      throws SQLException {
    return ArticleLikeStatus.findLikeStatusByInt(rs.getInt(columnName));
  }

  @Override
  public ArticleLikeStatus getNullableResult(ResultSet rs, int columnIndex)
      throws SQLException {
    return ArticleLikeStatus.findLikeStatusByInt(rs.getInt(columnIndex));
  }

  @Override
  public ArticleLikeStatus getNullableResult(CallableStatement cs, int columnIndex)
      throws SQLException {
    return ArticleLikeStatus.findLikeStatusByInt(cs.getInt(columnIndex));
  }
}
