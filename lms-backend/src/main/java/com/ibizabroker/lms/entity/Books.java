package com.ibizabroker.lms.entity;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;


@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "books")
public class Books {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // FE cũ có "bookName" → vẫn nhận được nhờ @JsonAlias
    @JsonAlias({"bookName"})
    private String name;

    @JsonAlias({"bookAuthor"})
    private String author;

    @JsonAlias({"bookGenre"})
    private String genre;

    @Column(name = "number_of_copies_available")
    @JsonAlias({"noOfCopies", "numberOfCopies"}) // phòng có tên khác
    private Integer numberOfCopiesAvailable;

    @Column(name = "published_year")
    private Integer publishedYear;

    @Column(length = 32)
    private String isbn;

    // ===== getters/setters =====
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public Integer getNumberOfCopiesAvailable() { return numberOfCopiesAvailable; }
    public void setNumberOfCopiesAvailable(Integer v) { this.numberOfCopiesAvailable = v; }

    public Integer getPublishedYear() { return publishedYear; }
    public void setPublishedYear(Integer publishedYear) { this.publishedYear = publishedYear; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    // ==== ALIAS để không phải sửa controller cũ ====
// Ẩn khỏi JSON output để không trùng field
@JsonIgnore
public String getBookName() { return getName(); }
public void setBookName(String v) { setName(v); }

@JsonIgnore
public String getBookAuthor() { return getAuthor(); }
public void setBookAuthor(String v) { setAuthor(v); }

@JsonIgnore
public String getBookGenre() { return getGenre(); }
public void setBookGenre(String v) { setGenre(v); }

// số lượng bản sao
@JsonIgnore
public Integer getNoOfCopies() { return getNumberOfCopiesAvailable(); }
public void setNoOfCopies(Integer v) { setNumberOfCopiesAvailable(v); }


    // ===== business helpers =====
    public void borrowBook() {
        if (numberOfCopiesAvailable == null) numberOfCopiesAvailable = 0;
        if (numberOfCopiesAvailable <= 0) throw new IllegalStateException("No copies available");
        numberOfCopiesAvailable--;
    }

    public void returnBook() {
        if (numberOfCopiesAvailable == null) numberOfCopiesAvailable = 0;
        numberOfCopiesAvailable++;
    }

    @Column(name = "cover_url", length = 512)
    private String coverUrl;

    // --- BỔ SUNG GETTERS/SETTERS CHO NÓ ---
    public String getCoverUrl() { return coverUrl; }
    public void setCoverUrl(String coverUrl) { this.coverUrl = coverUrl; }
}
