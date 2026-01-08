package com.ibizabroker.lms.dao;

import com.ibizabroker.lms.entity.Ebook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface EbookRepository extends JpaRepository<Ebook, Long> {

    Page<Ebook> findByIsPublicTrue(Pageable pageable);
    
    List<Ebook> findByBookId(Integer bookId);
    
    @Query("SELECT e FROM Ebook e WHERE " +
           "(:search IS NULL OR :search = '' OR " +
           "LOWER(e.title) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(e.author) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
           "(:fileType IS NULL OR e.fileType = :fileType) AND " +
           "(:isPublic IS NULL OR e.isPublic = :isPublic)")
    Page<Ebook> findWithFilters(
        @Param("search") String search,
        @Param("fileType") String fileType,
        @Param("isPublic") Boolean isPublic,
        Pageable pageable
    );
    
    @Transactional
    @Modifying
    @Query("UPDATE Ebook e SET e.downloadCount = e.downloadCount + 1 WHERE e.id = :id")
    void incrementDownloadCount(@Param("id") Long id);
    
    @Transactional
    @Modifying
    @Query("UPDATE Ebook e SET e.viewCount = e.viewCount + 1 WHERE e.id = :id")
    void incrementViewCount(@Param("id") Long id);
    
    @Query("SELECT e FROM Ebook e ORDER BY e.downloadCount DESC")
    List<Ebook> findTopDownloaded(Pageable pageable);
    
    @Query("SELECT e FROM Ebook e ORDER BY e.uploadedAt DESC")
    List<Ebook> findNewest(Pageable pageable);
}
