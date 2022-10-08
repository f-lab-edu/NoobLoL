package com.nooblol.global.utils.typehandler;

import com.nooblol.board.utils.BoardStatus;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

public class BoardStatusTypeHandler extends BaseTypeHandler<BoardStatus> {

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, BoardStatus parameter,
      JdbcType jdbcType) throws SQLException {
    ps.setInt(i, parameter.getStatus());
  }

  @Override
  public BoardStatus getNullableResult(ResultSet rs, String columnName) throws SQLException {
    return BoardStatus.findStatusEnumByIntValue(rs.getInt(columnName));
  }

  @Override
  public BoardStatus getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    return BoardStatus.findStatusEnumByIntValue(rs.getInt(columnIndex));
  }

  @Override
  public BoardStatus getNullableResult(CallableStatement cs, int columnIndex)
      throws SQLException {
    return BoardStatus.findStatusEnumByIntValue(cs.getInt(columnIndex));
  }
}