package com.ibizabroker.lms.dao;

import com.ibizabroker.lms.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Set;

public interface AuthorRepository extends JpaRepository<Author, Integer> {
    // Tìm một tập hợp các tác giả bằng ID của họ
    Set<Author> findByIdIn(Set<Integer> ids);
}