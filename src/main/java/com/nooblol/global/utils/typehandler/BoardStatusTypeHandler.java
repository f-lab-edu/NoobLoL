package com.nooblol.global.utils.typehandler;

import com.nooblol.board.utils.BoardStatusEnum;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

public class BoardStatusTypeHandler extends BaseTypeHandler<BoardStatusEnum> {

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, BoardStatusEnum parameter,
      JdbcType jdbcType) throws SQLException {
    ps.setInt(i, parameter.getStatus());
  }

  @Override
  public BoardStatusEnum getNullableResult(ResultSet rs, String columnName) throws SQLException {
    return BoardStatusEnum.findStatusEnumByIntValue(rs.getInt(columnName));
  }

  @Override
  public BoardStatusEnum getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    return BoardStatusEnum.findStatusEnumByIntValue(rs.getInt(columnIndex));
  }

  @Override
  public BoardStatusEnum getNullableResult(CallableStatement cs, int columnIndex)
      throws SQLException {
    return BoardStatusEnum.findStatusEnumByIntValue(cs.getInt(columnIndex));
  }
}