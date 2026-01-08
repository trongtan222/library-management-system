package com.ibizabroker.lms.dto;

import jakarta.validation.constraints.NotBlank;

public class ReviewCommentDto {
    public Integer id;
    public Integer reviewId;
    public Integer userId;
    public String userName;
    
    @NotBlank(message = "Nội dung comment không được để trống")
    public String content;
    
    public String createdAt;

    public String getContent() { return content; }
}
