package com.ibizabroker.lms.dto;

import java.util.List;

public class AdvancedSearchRequest {

    private String query;
    private List<Integer> authorIds;
    private List<Integer> categoryIds;
    private Integer yearFrom;
    private Integer yearTo;
    private Boolean availableOnly;
    private String sortBy; // name, year, popularity

    // Getters and Setters
    public String getQuery() { return query; }
    public void setQuery(String query) { this.query = query; }

    public List<Integer> getAuthorIds() { return authorIds; }
    public void setAuthorIds(List<Integer> authorIds) { this.authorIds = authorIds; }

    public List<Integer> getCategoryIds() { return categoryIds; }
    public void setCategoryIds(List<Integer> categoryIds) { this.categoryIds = categoryIds; }

    public Integer getYearFrom() { return yearFrom; }
    public void setYearFrom(Integer yearFrom) { this.yearFrom = yearFrom; }

    public Integer getYearTo() { return yearTo; }
    public void setYearTo(Integer yearTo) { this.yearTo = yearTo; }

    public Boolean getAvailableOnly() { return availableOnly; }
    public void setAvailableOnly(Boolean availableOnly) { this.availableOnly = availableOnly; }

    public String getSortBy() { return sortBy; }
    public void setSortBy(String sortBy) { this.sortBy = sortBy; }
}
