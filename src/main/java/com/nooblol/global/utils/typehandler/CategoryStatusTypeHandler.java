package com.nooblol.global.utils.typehandler;

import com.nooblol.board.utils.CategoryStatus;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

public class CategoryStatusTypeHandler extends BaseTypeHandler<CategoryStatus> {

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, CategoryStatus parameter,
      JdbcType jdbcType) throws SQLException {
    ps.setInt(i, parameter.getStatus());
  }

  @Override
  public CategoryStatus getNullableResult(ResultSet rs, String columnName) throws SQLException {
    return CategoryStatus.findStatusEnumByIntValue(rs.getInt(columnName));
  }

  @Override
  public CategoryStatus getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    return CategoryStatus.findStatusEnumByIntValue(rs.getInt(columnIndex));
  }

  @Override
  public CategoryStatus getNullableResult(CallableStatement cs, int columnIndex)
      throws SQLException {
    return CategoryStatus.findStatusEnumByIntValue(cs.getInt(columnIndex));
  }
}
