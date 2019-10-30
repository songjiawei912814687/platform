package com.stamp.domain.output;


import com.stamp.model.Menu;

import java.util.List;

public class MenuOutput extends Menu {
    List<MenuOutput> children;
    private Integer checkState = 0;

    private Integer type = 0;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getCheckState() {
        return checkState;
    }

    public void setCheckState(Integer checkState) {
        this.checkState = checkState;
    }


    public List<MenuOutput> getChildren() {
        return children;
    }

    public void setChildren(List<MenuOutput> children) {
        this.children = children;
    }
}
