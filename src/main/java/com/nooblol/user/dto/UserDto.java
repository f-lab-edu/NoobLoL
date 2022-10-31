package com.nooblol.user.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
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
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

}