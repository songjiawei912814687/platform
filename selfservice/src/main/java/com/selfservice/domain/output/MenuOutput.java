package com.selfservice.domain.output;




import com.selfservice.model.Menu;

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
