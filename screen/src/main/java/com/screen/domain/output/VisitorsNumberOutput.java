package com.screen.domain.output;

import java.io.Serializable;

public class VisitorsNumberOutput implements Serializable {

    private String date;
    //取号数
    private Integer fetchNumber;
    //办理人数
    private Integer handleNumber;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getFetchNumber() {
        return fetchNumber;
    }

    public void setFetchNumber(Integer fetchNumber) {
        this.fetchNumber = fetchNumber;
    }

    public Integer getHandleNumber() {
        return handleNumber;
    }

    public void setHandleNumber(Integer handleNumber) {
        this.handleNumber = handleNumber;
    }
}
