package com.feedback.domain.input;


import com.feedback.model.SMSSend;

import java.util.List;

public class SMSSendInput extends SMSSend {
    private List<String> sendList;

    public List<String> getSendList() {
        return sendList;
    }

    public void setSendList(List<String> sendList) {
        this.sendList = sendList;
    }

}
