package com.api.domain.output;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author: Young
 * @description: 热门事项办件量对象
 * @date: Created in 16:00 2018/11/20
 * @modified by:
 */
public class HotDoOutput implements Serializable {


    private String qlFullId;
    private String qlNames;
    private Integer count;
    private BigDecimal point;

    public BigDecimal getPoint() {
        return point;
    }

    public void setPoint(BigDecimal point) {
        this.point = point;
    }

    public String getQlFullId() {
        return qlFullId;
    }

    public void setQlFullId(String qlFullId) {
        this.qlFullId = qlFullId;
    }

    public String getQlNames() {
        return qlNames;
    }

    public void setQlNames(String qlNames) {
        this.qlNames = qlNames;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
