package com.screenData.domain.output;

import com.screenData.model.ScreenConfig;

import java.util.List;

public class ScreenConfigZTree {


    private  String label;

    private  Integer key;

    private  Integer value;

    private List<ScreenConfigZTree> children;


    public ScreenConfigZTree(ScreenConfig screenConfig) {
        this.label = screenConfig.getName();
        this.key = screenConfig.getId();
        this.value = screenConfig.getId();
        this.children = null;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
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

    public List<ScreenConfigZTree> getChildren() {
        return children;
    }

    public void setChildren(List<ScreenConfigZTree> children) {
        this.children = children;
    }
}
