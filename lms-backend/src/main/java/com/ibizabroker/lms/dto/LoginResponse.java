// src/main/java/com/ibizabroker/lms/dto/LoginResponse.java
package com.ibizabroker.lms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class LoginResponse {
  private String token;      // JWT
  private Integer userId;
  private String username;
  private String name;
  private List<String> roles;
}
