package com.ibizabroker.lms.dto;

import jakarta.validation.constraints.NotEmpty;

public class EbookDto {

    @NotEmpty(message = "Tiêu đề không được để trống")
    private String title;

    private String author;
    private String description;
    private String coverUrl;
    private Boolean isPublic;
    private Integer maxDownloadsPerUser;
    private Integer bookId; // Liên kết sách vật lý (optional)

    // Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCoverUrl() { return coverUrl; }
    public void setCoverUrl(String coverUrl) { this.coverUrl = coverUrl; }

    public Boolean getIsPublic() { return isPublic; }
    public void setIsPublic(Boolean isPublic) { this.isPublic = isPublic; }

    public Integer getMaxDownloadsPerUser() { return maxDownloadsPerUser; }
    public void setMaxDownloadsPerUser(Integer maxDownloadsPerUser) { this.maxDownloadsPerUser = maxDownloadsPerUser; }

    public Integer getBookId() { return bookId; }
    public void setBookId(Integer bookId) { this.bookId = bookId; }
}
