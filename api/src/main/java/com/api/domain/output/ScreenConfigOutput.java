package com.api.domain.output;

import com.api.model.ScreenConfig;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;
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
