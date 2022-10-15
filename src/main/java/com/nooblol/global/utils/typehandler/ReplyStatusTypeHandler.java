package com.nooblol.global.utils.typehandler;

import com.nooblol.board.utils.ReplyStatus;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

public class ReplyStatusTypeHandler extends BaseTypeHandler<ReplyStatus> {

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, ReplyStatus parameter,
      JdbcType jdbcType) throws SQLException {
    ps.setInt(i, parameter.getStatus());
  }

  @Override
  public ReplyStatus getNullableResult(ResultSet rs, String columnName) throws SQLException {
    return ReplyStatus.findStatusEnumByStatusValue(rs.getInt(columnName));
  }

  @Override
  public ReplyStatus getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    return ReplyStatus.findStatusEnumByStatusValue(rs.getInt(columnIndex));
  }

  @Override
  public ReplyStatus getNullableResult(CallableStatement cs, int columnIndex)
      throws SQLException {
    return ReplyStatus.findStatusEnumByStatusValue(cs.getInt(columnIndex));
  }
}