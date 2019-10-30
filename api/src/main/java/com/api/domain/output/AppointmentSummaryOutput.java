package com.api.domain.output;

/**
 * @author: young
 * @project_name: svn
 * @description:
 * @date: Created in 2018-12-29  11:19
 * @modified by:
 */
public class AppointmentSummaryOutput  {

    private Integer id;

    private String deptCode;

    private String deptName;

    private String groupCode;

    private String groupName;

    private String beginTime;

    private String endTime;

    //时间段内还可取号的号码数量
    private String availableEnqueueQuantity;

    //时间段内可取号的最多号码数量
    private String maxEnqueueQuantity;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getDeptCode() {
        return deptCode;
    }

    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getAvailableEnqueueQuantity() {
        return availableEnqueueQuantity;
    }

    public void setAvailableEnqueueQuantity(String availableEnqueueQuantity) {
        this.availableEnqueueQuantity = availableEnqueueQuantity;
    }

    public String getMaxEnqueueQuantity() {
        return maxEnqueueQuantity;
    }

    public void setMaxEnqueueQuantity(String maxEnqueueQuantity) {
        this.maxEnqueueQuantity = maxEnqueueQuantity;
    }
}
