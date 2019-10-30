package com.attendance.domian.output;

import com.attendance.model.Verification;
import com.common.Enum.LeaveApplicationStatusEnum;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * @author: young
 * @project_name: platform
 * @description:
 * @date: Created in 2019-04-11  16:00
 * @modified by:
 */
public class VerificationOutput extends Verification {

    private Integer LeaveId;

    private Integer applicationType;

    //请假类型名称
    private String applicationTypeName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;


    public Integer getLeaveId() {
        return LeaveId;
    }

    public void setLeaveId(Integer leaveId) {
        LeaveId = leaveId;
    }

    public Integer getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(Integer applicationType) {
        this.applicationType = applicationType;
    }



    public String getApplicationTypeName() {
        if(getApplicationType()!=null){
            return LeaveApplicationStatusEnum.getByCodeMsg(getApplicationType());
        }
        return "";
    }

    public void setApplicationTypeName(String applicationTypeName) {
        this.applicationTypeName = applicationTypeName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
