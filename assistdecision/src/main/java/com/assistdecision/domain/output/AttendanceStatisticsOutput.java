package com.assistdecision.domain.output;

import com.assistdecision.model.AttendanceStatistics;
import com.common.Enum.LeaveApplicationStatusEnum;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author: young
 * @project_name: assist-decision
 * @description: 工作人员管理对象
 * @date: Created in 2019-02-27  14:54
 * @modified by:
 */
public class AttendanceStatisticsOutput  extends AttendanceStatistics{

    /**迟到人数*/
    private Integer lateCount;

    /**请假人数*/
    private Integer leaveCount;

    /**早退人数*/
    private Integer earlyCount;

    /**未打卡人数*/
    private Integer notPunchCount;

    //迟到次数
    private Integer beLateTimes;

    //请假次数
    private Integer isLeaveTimes;

    //早退次数
    private Integer leaveEarlyTimes;

    //未打卡次数
    private Integer punchTimes;

    //最终得分
    private BigDecimal finalScore = new BigDecimal(100);

    //考核计划id
    private Integer approvalId;

    /**考核描述*/
    private String description;

    /**最终一共加了或者减了多少分*/
    private BigDecimal ruleScore;


    private String scoreTypeName;

    private String empno;

    private String lateDetail;

    private String leaveDetail;

    private String leaveEailyDetail;

    private String noPunchDetail;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date leaveDate;

    private Integer leaveType;

    private String leaveTypeName;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getRuleScore() {
        return ruleScore;
    }

    public void setRuleScore(BigDecimal ruleScore) {
        this.ruleScore = ruleScore;
    }

    public String getScoreTypeName() {
        return scoreTypeName;
    }

    public void setScoreTypeName(String scoreTypeName) {
        this.scoreTypeName = scoreTypeName;
    }

    public BigDecimal getFinalScore() {
        return finalScore;
    }

    public void setFinalScore(BigDecimal finalScore) {
        this.finalScore = finalScore;
    }

    public Integer getApprovalId() {
        return approvalId;
    }

    public void setApprovalId(Integer approvalId) {
        this.approvalId = approvalId;
    }

    public Integer getBeLateTimes() {
        return beLateTimes;
    }

    public void setBeLateTimes(Integer beLateTimes) {
        this.beLateTimes = beLateTimes;
    }

    public Integer getIsLeaveTimes() {
        return isLeaveTimes;
    }

    public void setIsLeaveTimes(Integer isLeaveTimes) {
        this.isLeaveTimes = isLeaveTimes;
    }

    public Integer getLeaveEarlyTimes() {
        return leaveEarlyTimes;
    }

    public void setLeaveEarlyTimes(Integer leaveEarlyTimes) {
        this.leaveEarlyTimes = leaveEarlyTimes;
    }

    public Integer getPunchTimes() {
        return punchTimes;
    }

    public void setPunchTimes(Integer punchTimes) {
        this.punchTimes = punchTimes;
    }

    public String getLeaveTypeName() {
        if(this.getLeaveType()!=null){
            return LeaveApplicationStatusEnum.getByCodeMsg(this.getLeaveType());
        }
        return "";
    }

    public String getEmpno() {
        return empno;
    }

    public void setEmpno(String empno) {
        this.empno = empno;
    }

    public String getLateDetail() {
        return lateDetail;
    }

    public void setLateDetail(String lateDetail) {
        this.lateDetail = lateDetail;
    }

    public String getLeaveDetail() {
        return leaveDetail;
    }

    public void setLeaveDetail(String leaveDetail) {
        this.leaveDetail = leaveDetail;
    }

    public String getLeaveEailyDetail() {
        return leaveEailyDetail;
    }

    public void setLeaveEailyDetail(String leaveEailyDetail) {
        this.leaveEailyDetail = leaveEailyDetail;
    }

    public String getNoPunchDetail() {
        return noPunchDetail;
    }

    public void setNoPunchDetail(String noPunchDetail) {
        this.noPunchDetail = noPunchDetail;
    }

    public Integer getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(Integer leaveType) {
        this.leaveType = leaveType;
    }

    public void setLeaveTypeName(String leaveTypeName) {
        this.leaveTypeName = leaveTypeName;
    }

    public Date getLeaveDate() {
        return leaveDate;
    }

    public void setLeaveDate(Date leaveDate) {
        this.leaveDate = leaveDate;
    }

    public Integer getLateCount() {
        return lateCount;
    }

    public void setLateCount(Integer lateCount) {
        this.lateCount = lateCount;
    }

    public Integer getLeaveCount() {
        return leaveCount;
    }

    public void setLeaveCount(Integer leaveCount) {
        this.leaveCount = leaveCount;
    }

    public Integer getEarlyCount() {
        return earlyCount;
    }

    public void setEarlyCount(Integer earlyCount) {
        this.earlyCount = earlyCount;
    }

    public Integer getNotPunchCount() {
        return notPunchCount;
    }

    public void setNotPunchCount(Integer notPunchCount) {
        this.notPunchCount = notPunchCount;
    }
}
