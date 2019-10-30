package com.screenData.domain.output;


import com.screenData.model.ScreenConfig;

import java.util.List;

public class ScreenConfigOutput extends ScreenConfig {


    private List<ScreenConfigOutput> children;

    public List<ScreenConfigOutput> getChildren() {
        return children;
    }

    public void setChildren(List<ScreenConfigOutput> children) {
        this.children = children;
    }

}
