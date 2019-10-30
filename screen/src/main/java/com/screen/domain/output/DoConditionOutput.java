package com.screen.domain.output;
import java.io.Serializable;

/**
 * @author: Young
 * @description:
 * @date: Created in 16:22 2018/11/20
 * @modified by:
 */
public class DoConditionOutput implements Serializable {

    private String data;
    private Integer count;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

}
