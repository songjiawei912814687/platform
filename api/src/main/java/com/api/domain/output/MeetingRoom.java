package com.api.domain.output;

import java.io.Serializable;
import java.util.Date;


public class MeetingRoom implements Serializable {

    private Integer id;

    private String name;

    private String ip;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}

