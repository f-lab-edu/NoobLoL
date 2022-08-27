package com.nooblol.community.dto;

import java.sql.Timestamp;
import lombok.AllArgsConstructor;
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
  private Timestamp createdAt;
  private Timestamp updatedAt;

}