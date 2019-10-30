package com.selfservice.domain.output;

import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @author: Young
 * @description:
 * @date: Created in 22:07 2018/11/24
 * @modified by:
 */
@Component
public class SelfServiceOutput implements Serializable {

    private static final long serialVersionUID = -7317605169763173975L;
    public String id;
    public String name;
    public Integer type;


    public SelfServiceOutput() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
