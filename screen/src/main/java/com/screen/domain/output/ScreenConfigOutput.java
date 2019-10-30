package com.screen.domain.output;


import com.screen.model.ScreenConfig;

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
