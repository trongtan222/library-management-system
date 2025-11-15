package com.ibizabroker.lms.entity;

import com.fasterxml.jackson.annotation.JsonIgnore; // <-- THÊM IMPORT NÀY
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "categories")
    private Set<Books> books = new HashSet<>();

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    @JsonIgnore // <-- THÊM ANNOTATION NÀY
    public Set<Books> getBooks() { return books; }
    public void setBooks(Set<Books> books) { this.books = books; }
}