package com.nooblol.global.utils.typehandler;

import com.nooblol.board.utils.ArticleStatus;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

public class ArticleStatusTypeHandler extends BaseTypeHandler<ArticleStatus> {

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, ArticleStatus parameter,
      JdbcType jdbcType) throws SQLException {
    ps.setInt(i, parameter.getStatus());
  }

  @Override
  public ArticleStatus getNullableResult(ResultSet rs, String columnName) throws SQLException {
    return ArticleStatus.findStatusEnumByIntValue(rs.getInt(columnName));
  }

  @Override
  public ArticleStatus getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    return ArticleStatus.findStatusEnumByIntValue(rs.getInt(columnIndex));
  }

  @Override
  public ArticleStatus getNullableResult(CallableStatement cs, int columnIndex)
      throws SQLException {
    return ArticleStatus.findStatusEnumByIntValue(cs.getInt(columnIndex));
  }
}
