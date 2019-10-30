package com.message.domain.input;

import com.message.model.SMSSend;

import java.util.List;

public class SMSSendInput extends SMSSend {
    private List<String> sendList;


    private String groupIdList;

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


    public String getGroupIdList() {
        return groupIdList;
    }

    public void setGroupIdList(String groupIdList) {
        this.groupIdList = groupIdList;
    }
}
