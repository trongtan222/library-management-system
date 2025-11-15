package com.ibizabroker.lms.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.Set;

// Tương tự BookCreateDto nhưng các trường đều optional
public class BookUpdateDto {
    @Size(min = 1, message = "Tên sách phải có ít nhất 1 ký tự")
    private String name;
    
    @Min(value = 0, message = "Số lượng không được âm")
    private Integer numberOfCopiesAvailable;
    
    @Min(value = 1000, message = "Năm xuất bản phải >= 1000")
    private Integer publishedYear;
    
    @Pattern(regexp = "^(?:[0-9]{10}|[0-9]{13})?$", message = "ISBN phải là 10 hoặc 13 chữ số")
    private String isbn;
    
    private String coverUrl;
    private Set<Integer> authorIds;
    private Set<Integer> categoryIds;

    // Getters and Setters (tương tự)
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Integer getNumberOfCopiesAvailable() { return numberOfCopiesAvailable; }
    public void setNumberOfCopiesAvailable(Integer n) { this.numberOfCopiesAvailable = n; }
    public Integer getPublishedYear() { return publishedYear; }
    public void setPublishedYear(Integer publishedYear) { this.publishedYear = publishedYear; }
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public String getCoverUrl() { return coverUrl; }
    public void setCoverUrl(String coverUrl) { this.coverUrl = coverUrl; }
    public Set<Integer> getAuthorIds() { return authorIds; }
    public void setAuthorIds(Set<Integer> authorIds) { this.authorIds = authorIds; }
    public Set<Integer> getCategoryIds() { return categoryIds; }
    public void setCategoryIds(Set<Integer> categoryIds) { this.categoryIds = categoryIds; }
}