package com.personnel.domian.output;



import com.personnel.model.Organization;

import java.util.List;


public class OrganizationZTree {

    private  String label;

    private  Integer key;

    private  Integer value;


    private List<OrganizationZTree> children;




    public OrganizationZTree(Organization organization) {
        this.label = organization.getName();
        this.key = organization.getId();
        this.value = organization.getId();
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

    public List<OrganizationZTree> getChildren() {
        return children;
    }

    public void setChildren(List<OrganizationZTree> children) {
        this.children = children;
    }
}
