package com.ibizabroker.lms.dao;

import com.ibizabroker.lms.entity.EbookDownload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EbookDownloadRepository extends JpaRepository<EbookDownload, Long> {

    List<EbookDownload> findByUserId(Integer userId);
    
    List<EbookDownload> findByEbookId(Long ebookId);
    
    @Query("SELECT COUNT(d) FROM EbookDownload d WHERE d.ebook.id = :ebookId AND d.userId = :userId")
    long countByEbookAndUser(@Param("ebookId") Long ebookId, @Param("userId") Integer userId);
    
    @Query("SELECT COUNT(d) FROM EbookDownload d WHERE d.userId = :userId")
    long countByUser(@Param("userId") Integer userId);
}
