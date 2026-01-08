package com.ibizabroker.lms.dto;

import jakarta.validation.constraints.NotEmpty;

public class BadgeDto {

    @NotEmpty(message = "Mã huy hiệu không được để trống")
    private String code;

    @NotEmpty(message = "Tên huy hiệu (VI) không được để trống")
    private String nameVi;

    @NotEmpty(message = "Tên huy hiệu (EN) không được để trống")
    private String nameEn;

    private String descriptionVi;
    private String descriptionEn;
    private String iconUrl;
    private Integer pointsReward;
    private String category;
    private Integer requirementValue;

    // Getters and Setters
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
}
