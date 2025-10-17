package com.ibizabroker.lms.dao;

import com.ibizabroker.lms.entity.Books;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BooksRepository extends JpaRepository<Books, Integer> {

    @Transactional
    @Modifying
    @Query("update Books b set b.numberOfCopiesAvailable = b.numberOfCopiesAvailable - 1 " +
           "where b.id = :bookId and b.numberOfCopiesAvailable > 0")
    int decrementAvailable(@Param("bookId") Integer bookId);

    @Transactional
    @Modifying
    @Query("update Books b set b.numberOfCopiesAvailable = b.numberOfCopiesAvailable + 1 " +
           "where b.id = :bookId")
    int incrementAvailable(@Param("bookId") Integer bookId);
    
    List<Books> findByNumberOfCopiesAvailableGreaterThan(int n);

    // --- THAY THẾ PHƯƠNG THỨC CŨ BẰNG PHƯƠNG THỨC NÀY ---
    @Query("SELECT b FROM Books b WHERE " +
           "(:search IS NULL OR :search = '' OR LOWER(b.name) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(b.author) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
           "(:genre IS NULL OR :genre = '' OR b.genre = :genre) AND " +
           "(:availableOnly = false OR b.numberOfCopiesAvailable > 0)")
    Page<Books> findWithFiltersAndPagination(
        @Param("search") String search,
        @Param("genre") String genre,
        @Param("availableOnly") boolean availableOnly,
        Pageable pageable); // Thêm Pageable để phân trang

       List<Books> findTop10ByOrderByIdDesc();
}