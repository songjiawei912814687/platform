package com.screen.domain.output;

import java.math.BigDecimal;

public class PerformanceIndexOutput {

   private String departmentName;

   private BigDecimal score;

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }
}
