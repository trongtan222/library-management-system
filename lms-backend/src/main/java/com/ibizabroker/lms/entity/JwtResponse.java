package com.ibizabroker.lms.entity;

public class JwtResponse {

    private Users user;
    private String token; // đổi tên cho thống nhất với FE

    public JwtResponse() {
        // ⚙ bắt buộc cho Jackson
    }

    public JwtResponse(Users user, String token) {
        this.user = user;
        this.token = token;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
