package com.ibizabroker.lms.dto;

import java.util.ArrayList;
import java.util.List;

public class ImportSummaryDto {
    private int successCount;
    private int failedCount;
    private int skippedCount;
    private List<String> errors = new ArrayList<>();

    public ImportSummaryDto() {}

    public ImportSummaryDto(int successCount, int failedCount, int skippedCount, List<String> errors) {
        this.successCount = successCount;
        this.failedCount = failedCount;
        this.skippedCount = skippedCount;
        if (errors != null) {
            this.errors = errors;
        }
    }

    public int getSuccessCount() { return successCount; }
    public void setSuccessCount(int successCount) { this.successCount = successCount; }

    public int getFailedCount() { return failedCount; }
    public void setFailedCount(int failedCount) { this.failedCount = failedCount; }

    public int getSkippedCount() { return skippedCount; }
    public void setSkippedCount(int skippedCount) { this.skippedCount = skippedCount; }

    public List<String> getErrors() { return errors; }
    public void setErrors(List<String> errors) { this.errors = errors; }
}
