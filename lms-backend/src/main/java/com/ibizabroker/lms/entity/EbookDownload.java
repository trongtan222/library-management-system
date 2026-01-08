package com.ibizabroker.lms.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Feature 5: E-book Management - Theo dõi lượt download
 */
@Entity
@Table(name = "ebook_downloads")
public class EbookDownload {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ebook_id", nullable = false)
    private Ebook ebook;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "downloaded_at", nullable = false)
    private LocalDateTime downloadedAt = LocalDateTime.now();

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Ebook getEbook() { return ebook; }
    public void setEbook(Ebook ebook) { this.ebook = ebook; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public LocalDateTime getDownloadedAt() { return downloadedAt; }
    public void setDownloadedAt(LocalDateTime downloadedAt) { this.downloadedAt = downloadedAt; }

    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
}
