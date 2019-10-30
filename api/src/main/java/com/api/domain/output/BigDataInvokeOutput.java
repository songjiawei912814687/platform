package com.api.domain.output;

import java.io.Serializable;

/**
 * @author: Young
 * @description: 统计口径
 * @date: Created in 14:41 2018/10/30
 * @modified by:
 */
public class BigDataInvokeOutput implements Serializable {

    private static final long serialVersionUID = 2995011434270951716L;
    private String date;

    private Integer count;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
