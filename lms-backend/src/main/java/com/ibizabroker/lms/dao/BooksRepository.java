package com.ibizabroker.lms.dao;

import com.ibizabroker.lms.entity.Books;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BooksRepository extends JpaRepository<Books, Integer> {

    // ... (Các hàm decrementAvailable, incrementAvailable giữ nguyên) ...
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

   // === FIX N+1 PROBLEM: Override findAll() with EntityGraph ===
   @SuppressWarnings("null")
@Override
   @EntityGraph(attributePaths = {"authors", "categories"})
   List<Books> findAll();

   // Paged findAll: do not fetch-join collections to avoid pagination explosion; rely on batch size
   @SuppressWarnings("null")
@Override
   Page<Books> findAll(@SuppressWarnings("null")    Pageable pageable);

    // (Hàm findWithFiltersAndPagination đã đúng, giữ nguyên)
   @EntityGraph(attributePaths = {"authors", "categories"})
       @Query(value = "SELECT DISTINCT b FROM Books b " +
                 "LEFT JOIN b.authors a " +
                 "LEFT JOIN b.categories c " +
                 "WHERE " +
                 "(:search IS NULL OR :search = '' OR LOWER(b.name) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(a.name) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
                 "(:genre IS NULL OR :genre = '' OR c.name = :genre) AND " +
                 "(:availableOnly = false OR b.numberOfCopiesAvailable > 0)",
                 countQuery = "SELECT COUNT(DISTINCT b) FROM Books b " +
                                          "LEFT JOIN b.authors a " +
                                          "LEFT JOIN b.categories c " +
                                          "WHERE " +
                                          "(:search IS NULL OR :search = '' OR LOWER(b.name) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(a.name) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
                                          "(:genre IS NULL OR :genre = '' OR c.name = :genre) AND " +
                                          "(:availableOnly = false OR b.numberOfCopiesAvailable > 0)")
    Page<Books> findWithFiltersAndPagination(
        @Param("search") String search,
        @Param("genre") String genre,
        @Param("availableOnly") boolean availableOnly,
        Pageable pageable);

    // === SỬA LỖI 1: GHI ĐÈ findById ĐỂ TẢI KÈM DETAILS ===
   @SuppressWarnings("null")
@EntityGraph(attributePaths = {"authors", "categories"})
   @Query("SELECT b FROM Books b LEFT JOIN FETCH b.authors LEFT JOIN FETCH b.categories WHERE b.id = :id")
   Optional<Books> findById(@SuppressWarnings("null") @Param("id") Integer bookId);

    // === SỬA LỖI 2: GHI ĐÈ findTop10... ĐỂ TẢI KÈM DETAILS ===
    // (JPQL không hỗ trợ LIMIT 10, nên chúng ta dùng Pageable)
      @EntityGraph(attributePaths = {"authors", "categories"})
      @Query("SELECT b FROM Books b ORDER BY b.id DESC")
      List<Books> findNewestBooks(Pageable pageable);
    
    // Xóa hàm cũ đi (hoặc để đó cũng không sao, nhưng hàm mới sẽ được dùng)
    // List<Books> findTop10ByOrderByIdDesc(); 

    // === ADVANCED SEARCH QUERIES ===
    
    @EntityGraph(attributePaths = {"authors", "categories"})
    @Query(value = "SELECT DISTINCT b FROM Books b " +
           "LEFT JOIN b.authors a " +
           "LEFT JOIN b.categories c " +
           "WHERE (:query IS NULL OR :query = '' OR " +
           "       LOWER(b.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "       LOWER(b.isbn) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "       LOWER(a.name) LIKE LOWER(CONCAT('%', :query, '%'))) " +
           "AND (:authorIds IS NULL OR a.id IN :authorIds) " +
           "AND (:categoryIds IS NULL OR c.id IN :categoryIds) " +
           "AND (:yearFrom IS NULL OR b.publishedYear >= :yearFrom) " +
           "AND (:yearTo IS NULL OR b.publishedYear <= :yearTo) " +
           "AND (:availableOnly = false OR b.numberOfCopiesAvailable > 0)",
           countQuery = "SELECT COUNT(DISTINCT b) FROM Books b " +
           "LEFT JOIN b.authors a " +
           "LEFT JOIN b.categories c " +
           "WHERE (:query IS NULL OR :query = '' OR " +
           "       LOWER(b.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "       LOWER(b.isbn) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "       LOWER(a.name) LIKE LOWER(CONCAT('%', :query, '%'))) " +
           "AND (:authorIds IS NULL OR a.id IN :authorIds) " +
           "AND (:categoryIds IS NULL OR c.id IN :categoryIds) " +
           "AND (:yearFrom IS NULL OR b.publishedYear >= :yearFrom) " +
           "AND (:yearTo IS NULL OR b.publishedYear <= :yearTo) " +
           "AND (:availableOnly = false OR b.numberOfCopiesAvailable > 0)")
    Page<Books> advancedSearch(
        @Param("query") String query,
        @Param("authorIds") List<Integer> authorIds,
        @Param("categoryIds") List<Integer> categoryIds,
        @Param("yearFrom") Integer yearFrom,
        @Param("yearTo") Integer yearTo,
        @Param("availableOnly") boolean availableOnly,
        Pageable pageable);

    @EntityGraph(attributePaths = {"authors", "categories"})
    @Query("SELECT b FROM Books b " +
           "JOIN b.categories c " +
           "WHERE b.id <> :excludeBookId " +
           "AND c.id IN :categoryIds " +
           "ORDER BY b.numberOfCopiesAvailable DESC")
    List<Books> findSimilarBooks(
        @Param("excludeBookId") Integer excludeBookId,
        @Param("categoryIds") List<Integer> categoryIds,
        Pageable pageable);

    @Query("SELECT DISTINCT b.name FROM Books b WHERE LOWER(b.name) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<String> findSearchSuggestions(@Param("query") String query, Pageable pageable);

    @Query("SELECT DISTINCT a.name FROM Author a WHERE LOWER(a.name) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<String> findAuthorSuggestions(@Param("query") String query, Pageable pageable);

    Optional<Books> findByIsbnIgnoreCase(String isbn);
}