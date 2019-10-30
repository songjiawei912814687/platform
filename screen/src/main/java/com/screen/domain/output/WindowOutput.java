package com.screen.domain.output;


import com.screen.model.Window;

public class WindowOutput extends Window {

    private String userName;

    private String organizationName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }
}
