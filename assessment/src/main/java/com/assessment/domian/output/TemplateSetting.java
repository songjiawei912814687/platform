package com.assessment.domian.output;

import java.util.List;

public class TemplateSetting {
    private Integer id;

    private Integer parentId;

    private Integer type;

    private String name;

    private Integer hasChild;

    private boolean checkState;

    private List<TemplateSetting> children;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getHasChild() {
        return hasChild;
    }

    public void setHasChild(Integer hasChild) {
        this.hasChild = hasChild;
    }

    public boolean isCheckState() {
        return checkState;
    }

    public void setCheckState(boolean checkState) {
        this.checkState = checkState;
    }

    public List<TemplateSetting> getChildren() {
        return children;
    }

    public void setChildren(List<TemplateSetting> children) {
        this.children = children;
    }
}
