package com.ibizabroker.lms.dto;

import java.util.List;

public class UserDto {
    private Integer userId;
    private String name;
    private String username;
    private List<String> roles;

    // Constructors
    public UserDto() {}

    public UserDto(Integer userId, String name, String username, List<String> roles) {
        this.userId = userId;
        this.name = name;
        this.username = username;
        this.roles = roles;
    }

    // Getters and Setters
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public List<String> getRoles() { return roles; }
    public void setRoles(List<String> roles) { this.roles = roles; }
}
