package com.ibizabroker.lms.dao;

import com.ibizabroker.lms.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Set;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    // Tìm một tập hợp các thể loại bằng ID của họ
    Set<Category> findByIdIn(Set<Integer> ids);
}