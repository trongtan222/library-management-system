package com.ibizabroker.lms.entity;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "books")
public class Books {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonAlias({"bookName"})
    private String name;

    // --- THAY ĐỔI 1: Đổi LAZY thành EAGER ---
    @ManyToMany(fetch = FetchType.EAGER) // <-- SỬA DÒNG NÀY
    @JoinTable(
        name = "book_authors",
        joinColumns = @JoinColumn(name = "book_id"),
        inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private Set<Author> authors = new HashSet<>();


    // --- THAY ĐỔI 2: Đổi LAZY thành EAGER ---
    @ManyToMany(fetch = FetchType.EAGER) // <-- SỬA DÒNG NÀY
    @JoinTable(
        name = "book_categories",
        joinColumns = @JoinColumn(name = "book_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();


    @Column(name = "number_of_copies_available")
    @JsonAlias({"noOfCopies", "numberOfCopies"})
    private Integer numberOfCopiesAvailable;

    @Column(name = "published_year")
    private Integer publishedYear;

    @Column(length = 32)
    private String isbn;

    @Column(name = "cover_url", length = 512)
    private String coverUrl;

    // ===== getters/setters (Giữ nguyên) =====
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Set<Author> getAuthors() { return authors; }
    public void setAuthors(Set<Author> authors) { this.authors = authors; }

    public Set<Category> getCategories() { return categories; }
    public void setCategories(Set<Category> categories) { this.categories = categories; }

    public Integer getNumberOfCopiesAvailable() { return numberOfCopiesAvailable; }
    public void setNumberOfCopiesAvailable(Integer v) { this.numberOfCopiesAvailable = v; }

    public Integer getPublishedYear() { return publishedYear; }
    public void setPublishedYear(Integer publishedYear) { this.publishedYear = publishedYear; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    
    public String getCoverUrl() { return coverUrl; }
    public void setCoverUrl(String coverUrl) { this.coverUrl = coverUrl; }

    // ==== ALIAS (Giữ nguyên) ====
    @JsonIgnore
    public String getBookName() { return getName(); }
    public void setBookName(String v) { setName(v); }

    @JsonIgnore
    public Integer getNoOfCopies() { return getNumberOfCopiesAvailable(); }
    public void setNoOfCopies(Integer v) { setNumberOfCopiesAvailable(v); }


    // ===== business helpers (Giữ nguyên) =====
    public void borrowBook() {
        if (numberOfCopiesAvailable == null) numberOfCopiesAvailable = 0;
        if (numberOfCopiesAvailable <= 0) throw new IllegalStateException("No copies available");
        numberOfCopiesAvailable--;
    }

    public void returnBook() {
        if (numberOfCopiesAvailable == null) numberOfCopiesAvailable = 0;
        numberOfCopiesAvailable++;
    }
}