package com.ibizabroker.lms.dto;

import java.util.List;

public class UserUpdateDto {
    private String name;
    private String username;
    private List<String> roles;

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public List<String> getRoles() { return roles; }
    public void setRoles(List<String> roles) { this.roles = roles; }
}
