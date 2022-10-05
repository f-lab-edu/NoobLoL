package com.nooblol.global.utils.typehandler;

import com.nooblol.board.utils.ArticleStatusEnum;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

public class ArticleStatusTypeHandler extends BaseTypeHandler<ArticleStatusEnum> {

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, ArticleStatusEnum parameter,
      JdbcType jdbcType) throws SQLException {
    ps.setInt(i, parameter.getStatus());
  }

  @Override
  public ArticleStatusEnum getNullableResult(ResultSet rs, String columnName) throws SQLException {
    return ArticleStatusEnum.findStatusEnumByIntValue(rs.getInt(columnName));
  }

  @Override
  public ArticleStatusEnum getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    return ArticleStatusEnum.findStatusEnumByIntValue(rs.getInt(columnIndex));
  }

  @Override
  public ArticleStatusEnum getNullableResult(CallableStatement cs, int columnIndex)
      throws SQLException {
    return ArticleStatusEnum.findStatusEnumByIntValue(cs.getInt(columnIndex));
  }
}
