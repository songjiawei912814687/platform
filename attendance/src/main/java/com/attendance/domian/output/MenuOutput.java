package com.attendance.domian.output;


import com.attendance.model.Menu;

import java.util.List;

public class MenuOutput extends Menu {
    List<MenuOutput> children;

    public List<MenuOutput> getChildren() {
        return children;
    }

    public void setChildren(List<MenuOutput> children) {
        this.children = children;
    }
}
