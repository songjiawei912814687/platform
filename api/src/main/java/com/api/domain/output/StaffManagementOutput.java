package com.api.domain.output;

public class StaffManagementOutput {
    //组织机构名字
    private String organizationName;
    //迟到人数
    private Integer beLateNumber;
    //早退人数
    private Integer leaveEarlyNumber;
    //未打卡人数
    private Integer punchNumber;
    //请假人数
    private Integer  leaveNumber;

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public Integer getBeLateNumber() {
        return beLateNumber;
    }

    public void setBeLateNumber(Integer beLateNumber) {
        this.beLateNumber = beLateNumber;
    }

    public Integer getLeaveEarlyNumber() {
        return leaveEarlyNumber;
    }

    public void setLeaveEarlyNumber(Integer leaveEarlyNumber) {
        this.leaveEarlyNumber = leaveEarlyNumber;
    }

    public Integer getPunchNumber() {
        return punchNumber;
    }

    public void setPunchNumber(Integer punchNumber) {
        this.punchNumber = punchNumber;
    }

    public Integer getLeaveNumber() {
        return leaveNumber;
    }

    public void setLeaveNumber(Integer leaveNumber) {
        this.leaveNumber = leaveNumber;
    }
}
