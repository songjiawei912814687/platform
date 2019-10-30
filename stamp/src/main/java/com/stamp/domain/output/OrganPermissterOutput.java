package com.stamp.domain.output;

import java.util.List;

public class OrganPermissterOutput {

    private Integer id;

    private Integer parentId;

    private String name;

    private Integer checkState = 0;

    private List<OrganPermissterOutput> children;

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCheckState() {
        return checkState;
    }

    public void setCheckState(Integer checkState) {
        this.checkState = checkState;
    }

    public List<OrganPermissterOutput> getChildren() {
        return children;
    }

    public void setChildren(List<OrganPermissterOutput> children) {
        this.children = children;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
