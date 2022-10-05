package com.nooblol.global.utils.typehandler;

import com.nooblol.board.utils.CategoryStatusEnum;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

public class CategoryStatusTypeHandler extends BaseTypeHandler<CategoryStatusEnum> {

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, CategoryStatusEnum parameter,
      JdbcType jdbcType) throws SQLException {
    ps.setInt(i, parameter.getStatus());
  }

  @Override
  public CategoryStatusEnum getNullableResult(ResultSet rs, String columnName) throws SQLException {
    return CategoryStatusEnum.findStatusEnumByIntValue(rs.getInt(columnName));
  }

  @Override
  public CategoryStatusEnum getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    return CategoryStatusEnum.findStatusEnumByIntValue(rs.getInt(columnIndex));
  }

  @Override
  public CategoryStatusEnum getNullableResult(CallableStatement cs, int columnIndex)
      throws SQLException {
    return CategoryStatusEnum.findStatusEnumByIntValue(cs.getInt(columnIndex));
  }
}
