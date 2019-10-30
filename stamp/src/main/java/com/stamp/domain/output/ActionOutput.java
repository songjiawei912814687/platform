package com.stamp.domain.output;


import com.stamp.model.Action;

public class ActionOutput extends Action {

    private Integer checkState = 0;

    public Integer getCheckState() {
        return checkState;
    }

    public void setCheckState(Integer checkState) {
        this.checkState = checkState;
    }

}
