package com.api.domain.output;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author: young
 * @project_name: svn
 * @description:
 * @date: Created in 2018-12-29  10:30
 * @modified by:
 */
public class GetBookableDateByDeptOutput {

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
