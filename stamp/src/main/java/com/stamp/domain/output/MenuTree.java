package com.stamp.domain.output;

import java.util.List;

public class MenuTree {
    private  String name;

    private  Integer key;

    private  Integer value;

    private List<MenuTree> children;

    private Integer displayOrder;

    public MenuTree(MenuOutput menuOutput) {
        this.name = menuOutput.getName();
        this.key = menuOutput.getId();
        this.value = menuOutput.getId();
        this.children = null;
        this.displayOrder = menuOutput.getDisPlayOrderBy();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public List<MenuTree> getChildren() {
        return children;
    }

    public void setChildren(List<MenuTree> children) {
        this.children = children;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }
}
