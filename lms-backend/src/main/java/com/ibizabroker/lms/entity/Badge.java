package com.ibizabroker.lms.entity;

import jakarta.persistence.*;

/**
 * Feature 9: Gamification - Định nghĩa các huy hiệu
 */
@Entity
@Table(name = "badges")
public class Badge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", nullable = false, unique = true, length = 50)
    private String code; // VD: FIRST_BORROW, SPEED_READER, TOP_REVIEWER

    @Column(name = "name_vi", nullable = false, length = 100)
    private String nameVi; // Tên tiếng Việt

    @Column(name = "name_en", nullable = false, length = 100)
    private String nameEn; // Tên tiếng Anh

    @Column(name = "description_vi", length = 255)
    private String descriptionVi;

    @Column(name = "description_en", length = 255)
    private String descriptionEn;

    @Column(name = "icon_url", length = 255)
    private String iconUrl;

    @Column(name = "points_reward", nullable = false)
    private Integer pointsReward = 0; // Điểm thưởng khi đạt được

    @Column(name = "category", length = 50)
    private String category; // READING, REVIEW, STREAK, SPECIAL

    @Column(name = "requirement_value")
    private Integer requirementValue; // VD: mượn 10 sách để đạt badge

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getNameVi() { return nameVi; }
    public void setNameVi(String nameVi) { this.nameVi = nameVi; }

    public String getNameEn() { return nameEn; }
    public void setNameEn(String nameEn) { this.nameEn = nameEn; }

    public String getDescriptionVi() { return descriptionVi; }
    public void setDescriptionVi(String descriptionVi) { this.descriptionVi = descriptionVi; }

    public String getDescriptionEn() { return descriptionEn; }
    public void setDescriptionEn(String descriptionEn) { this.descriptionEn = descriptionEn; }

    public String getIconUrl() { return iconUrl; }
    public void setIconUrl(String iconUrl) { this.iconUrl = iconUrl; }

    public Integer getPointsReward() { return pointsReward; }
    public void setPointsReward(Integer pointsReward) { this.pointsReward = pointsReward; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Integer getRequirementValue() { return requirementValue; }
    public void setRequirementValue(Integer requirementValue) { this.requirementValue = requirementValue; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
}
