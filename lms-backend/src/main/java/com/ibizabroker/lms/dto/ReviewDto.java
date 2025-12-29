package com.ibizabroker.lms.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class ReviewDto {
    public Integer id;
    public Integer bookId;
    public String bookName;
    public Integer userId;
    public String userName;

    @NotNull(message = "Điểm đánh giá không được để trống.")
    @Min(value = 1, message = "Điểm đánh giá phải từ 1 đến 5.")
    @Max(value = 5, message = "Điểm đánh giá phải từ 1 đến 5.")
    public Integer rating;
    
    public String comment;
    public boolean approved;
    public String createdAt;

    // Getters để controller có thể truy cập
    public Integer getRating() { return rating; }
    public String getComment() { return comment; }
}