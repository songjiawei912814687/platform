package com.stamp.domain.output;

import com.stamp.model.StampOrgan;

import java.util.List;

/**
 * @author: young
 * @project_name: svn
 * @description:
 * @date: Created in 2019-04-18  10:26
 * @modified by:
 */
public class StampOrganOutput extends StampOrgan {

    private List<StampOrganOutput> children = null;

    private String parentName;

    private String leadershipName;

    private String departmentalManagerName;

    private String isSyncQueueName;

    private Integer key;

    public StampOrganOutput() {
    }

    public StampOrganOutput(StampOrgan stampOrgan) {
        this.setId(stampOrgan.getId());
        this.key = stampOrgan.getId();
        this.setOrganizationNo( stampOrgan.getOrganizationNo());
        this.setDisplayOrder(stampOrgan.getDisplayOrder());
        this.setName(stampOrgan.getName());
        this.setParentId(stampOrgan.getParentId());
        this.setPhoneNumber(stampOrgan.getPhoneNumber());
        this.setAddress(stampOrgan.getAddress());
        this.setChildren(null);
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public void setIsSyncQueueName(String isSyncQueueName) {
        this.isSyncQueueName = isSyncQueueName;
    }

    public String getLeadershipName() {
        return leadershipName;
    }

    public void setLeadershipName(String leadershipName) {
        this.leadershipName = leadershipName;
    }

    public String getDepartmentalManagerName() {
        return departmentalManagerName;
    }

    public void setDepartmentalManagerName(String departmentalManagerName) {
        this.departmentalManagerName = departmentalManagerName;
    }
    public List<StampOrganOutput> getChildren() {
        return children;
    }

    public void setChildren(List<StampOrganOutput> children) {
        this.children = children;
    }

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }


}
