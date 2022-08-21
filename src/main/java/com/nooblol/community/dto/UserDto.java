package com.nooblol.community.dto;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

  private String userId;
  private String userEmail;
  private String userName;
  private String userPassword;
  private int userRole;
  private int level;
  private int exp;
  private Timestamp createAt;
  private Timestamp updatedAt;


  public void setRandomUserId() {
    setUserId(UUID.randomUUID().toString());
  }

  public void setNowCreateAt() {
    setCreateAt(new Timestamp(System.currentTimeMillis()));
  }
}
