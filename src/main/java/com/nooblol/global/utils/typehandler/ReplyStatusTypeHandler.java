package com.nooblol.global.utils.typehandler;

import com.nooblol.board.utils.ReplyStatusEnum;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

public class ReplyStatusTypeHandler extends BaseTypeHandler<ReplyStatusEnum> {

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, ReplyStatusEnum parameter,
      JdbcType jdbcType) throws SQLException {
    ps.setInt(i, parameter.getStatus());
  }

  @Override
  public ReplyStatusEnum getNullableResult(ResultSet rs, String columnName) throws SQLException {
    return ReplyStatusEnum.findStatusEnumByStatusValue(rs.getInt(columnName));
  }

  @Override
  public ReplyStatusEnum getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    return ReplyStatusEnum.findStatusEnumByStatusValue(rs.getInt(columnIndex));
  }

  @Override
  public ReplyStatusEnum getNullableResult(CallableStatement cs, int columnIndex)
      throws SQLException {
    return ReplyStatusEnum.findStatusEnumByStatusValue(cs.getInt(columnIndex));
  }
}