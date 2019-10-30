package com.stamp.domain.input;


import com.stamp.model.SMSSend;

import java.util.List;

public class SMSSendInput extends SMSSend {

    private List<String> sendList;

    public List<String> getSendList() {
        return sendList;
    }

    public void setSendList(List<String> sendList) {
        this.sendList = sendList;
    }

    private Double workDays;

    public Double getWorkDays() {
        return workDays;
    }

    public void setWorkDays(Double workDays) {
        this.workDays = workDays;
    }

}
