package com.ibizabroker.lms.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class BookLocationDto {

    @NotNull(message = "Tầng không được để trống")
    @Min(value = 1, message = "Tầng phải >= 1")
    private Integer floor;

    private String zone;

    @NotEmpty(message = "Mã kệ sách không được để trống")
    private String shelfCode;

    private Integer rowNumber;
    private Integer position;
    private String description;

    // Getters and Setters
    public Integer getFloor() { return floor; }
    public void setFloor(Integer floor) { this.floor = floor; }

    public String getZone() { return zone; }
    public void setZone(String zone) { this.zone = zone; }

    public String getShelfCode() { return shelfCode; }
    public void setShelfCode(String shelfCode) { this.shelfCode = shelfCode; }

    public Integer getRowNumber() { return rowNumber; }
    public void setRowNumber(Integer rowNumber) { this.rowNumber = rowNumber; }

    public Integer getPosition() { return position; }
    public void setPosition(Integer position) { this.position = position; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
