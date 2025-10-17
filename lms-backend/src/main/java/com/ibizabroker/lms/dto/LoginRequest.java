// src/main/java/com/ibizabroker/lms/dto/LoginRequest.java
package com.ibizabroker.lms.dto;

import lombok.Data;

@Data
public class LoginRequest {
  private String username;
  private String password;
}
