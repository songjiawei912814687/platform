package com.meeting.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.meeting.domain.output.Member;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Entity
@Table(name = "meeting_apply")
public class MeetingApply implements Serializable {

    @Id
    @Column(length = 8)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "meetingApplyGenerator")
    @SequenceGenerator(name = "meetingApplyGenerator", sequenceName = "meetingApplyNew_sequence",allocationSize = 1,initialValue = 1)
    public Integer id;

    @NotNull
    @Length(min = 1,max = 255,message = "会议主题只能在{min}和{max}")
    @Column(length = 255)
    @ApiModelProperty(value = "会议主题",name = "theme")
    public String theme;

    @Column(nullable = false,length = 6)
    @ApiModelProperty(value = "会议室ID",name = "meetingRoomId")
    public Integer meetingRoomId;

    @Column(nullable = false)
    @ApiModelProperty(value = "预约结束时间",name = "endDateTime")
    public Integer endDateTime;

    @Column(nullable = false)
    @ApiModelProperty(value = "预约开始时间",name = "startDateTime")
    public Integer startDateTime;

    @ApiModelProperty(value = "会议真正的开始时间",name = "startDateTime")
    @JsonFormat(pattern = "HH:mm", timezone = "GMT+8")
    public String realStartTime;

    @Length(min = 1,max = 255,message = "长度在{min}和{max}之间")
    @Column(nullable = false,length = 255)
    @ApiModelProperty(value = "主办单位",name = "hostUnit")
    public String hostUnit;

    @Column(nullable = false,length = 6)
    @ApiModelProperty(value = "预约部门ID",name = "organizationId")
    public Integer organizationId;

    @Column(nullable = false,length = 6)
    @ApiModelProperty(value = "预约人ID",name = "employeesId")
    public Integer employeesId;

    @Length(min = 1,max = 255,message = "描述内容长度只能在{min}和{max}之间")
    @Column(length = 255)
    @ApiModelProperty(value = "描述",name = "description")
    public String description;

    @Column(nullable = false,length = 1)
    @ApiModelProperty(value = "状态",name = "state")
    public Integer state;

    @Column(nullable = false,length = 6)
    @ApiModelProperty(value = "创建人ID",name = "createByUserId")
    public Integer createdUserId;

    @Column(nullable = false,length = 55)
    @ApiModelProperty(value="创建人name,最大长度55",name="createdUserName")
    public String createdUserName;//

    @Column(nullable = false)
    @ApiModelProperty(value="创建时间,最大长度7",name="createdDateTime")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    public Date createdDateTime;//

    @Column(nullable = false,length = 6)
    @ApiModelProperty(value = "最后更新人ID",name = "lastUpdateUserId")
    public Integer lastUpdateUserId;

    @Column(nullable = false,length = 80)
    @ApiModelProperty(value = "最后更新人姓名",name = "lastUpdateUserName")
    public String lastUpdateUserName;

    @Column(nullable = false)
    @ApiModelProperty(value = "最后更新时间",name = "lastUpdateDateTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GAM+8")
    public Date lastUpdateDateTime;

    @Column(nullable = false,length = 6)
    @ApiModelProperty(value = "是否删除",name = "amputated")
    public Integer amputated;

    @Column(length = 80)
    @ApiModelProperty(value = "与会人员ID",name = "attendants")
    public String attendants;

    @Column(length = 255)
    @ApiModelProperty(value = "与会人员名字",name = "attendantsName")
    public String attendantsName;

    @Column(length = 10)
    @ApiModelProperty(value = "与会人数",name = "attendantsName")
    public Integer meetingNumber;

    @ApiModelProperty(value = "预约日期",name = "attendantsName")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    public Date appointmentDate;

    public Integer isNeedTea;

    private Integer isNeedNeetwork;


    @Column(length = 80)
    @ApiModelProperty(value = "与会人员ID",name = "attendants")
    public String retirees;

    @Column(length = 80)
    @ApiModelProperty(value = "退休老干部",name = "retireesName")
    public String retireesName;

    @Transient
    private List<Attachment> attachmentList;

    @Transient
    private List<Member> addressBookList;

    private String teaName;

    private String teaPhone;

    private String itName;

    private String itPhone;

    public Integer getMeetingNumber() {
        return meetingNumber;
    }

    public void setMeetingNumber(Integer meetingNumber) {
        this.meetingNumber = meetingNumber;
    }

    public String getRealStartTime() {
        return realStartTime;
    }

    public void setRealStartTime(String realStartTime) {
        this.realStartTime = realStartTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public Integer getMeetingRoomId() {
        return meetingRoomId;
    }

    public void setMeetingRoomId(Integer meetingRoomId) {
        this.meetingRoomId = meetingRoomId;
    }

    public Integer getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(Integer endDateTime) {
        this.endDateTime = endDateTime;
    }

    public Integer getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(Integer startDateTime) {
        this.startDateTime = startDateTime;
    }

    public String getHostUnit() {
        return hostUnit;
    }

    public void setHostUnit(String hostUnit) {
        this.hostUnit = hostUnit;
    }

    public Integer getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

    public Integer getEmployeesId() {
        return employeesId;
    }

    public void setEmployeesId(Integer employeesId) {
        this.employeesId = employeesId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getCreatedUserId() {
        return createdUserId;
    }

    public void setCreatedUserId(Integer createdUserId) {
        this.createdUserId = createdUserId;
    }

    public String getCreatedUserName() {
        return createdUserName;
    }

    public void setCreatedUserName(String createdUserName) {
        this.createdUserName = createdUserName;
    }

    public Date getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(Date createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public Integer getLastUpdateUserId() {
        return lastUpdateUserId;
    }

    public void setLastUpdateUserId(Integer lastUpdateUserId) {
        this.lastUpdateUserId = lastUpdateUserId;
    }

    public String getLastUpdateUserName() {
        return lastUpdateUserName;
    }

    public void setLastUpdateUserName(String lastUpdateUserName) {
        this.lastUpdateUserName = lastUpdateUserName;
    }

    public Date getLastUpdateDateTime() {
        return lastUpdateDateTime;
    }

    public void setLastUpdateDateTime(Date lastUpdateDateTime) {
        this.lastUpdateDateTime = lastUpdateDateTime;
    }

    public Integer getAmputated() {
        return amputated;
    }

    public void setAmputated(Integer amputated) {
        this.amputated = amputated;
    }

    public String getAttendants() {
        return attendants;
    }

    public void setAttendants(String attendants) {
        this.attendants = attendants;
    }

    public String getAttendantsName() {
        return attendantsName;
    }

    public void setAttendantsName(String attendantsName) {
        this.attendantsName = attendantsName;
    }

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public List<Attachment> getAttachmentList() {
        return attachmentList;
    }

    public void setAttachmentList(List<Attachment> attachmentList) {
        this.attachmentList = attachmentList;
    }

    public Integer getIsNeedTea() {
        return isNeedTea;
    }

    public void setIsNeedTea(Integer isNeedTea) {
        this.isNeedTea = isNeedTea;
    }

    public Integer getIsNeedNeetwork() {
        return isNeedNeetwork;
    }

    public void setIsNeedNeetwork(Integer isNeedNeetwork) {
        this.isNeedNeetwork = isNeedNeetwork;
    }

    public String getRetirees() {
        return retirees;
    }

    public void setRetirees(String retirees) {
        this.retirees = retirees;
    }

    public List<Member> getAddressBookList() {
        return addressBookList;
    }

    public void setAddressBookList(List<Member> addressBookList) {
        this.addressBookList = addressBookList;
    }

    public String getRetireesName() {
        return retireesName;
    }

    public void setRetireesName(String retireesName) {
        this.retireesName = retireesName;
    }

    public String getTeaName() {
        return teaName;
    }

    public void setTeaName(String teaName) {
        this.teaName = teaName;
    }

    public String getTeaPhone() {
        return teaPhone;
    }

    public void setTeaPhone(String teaPhone) {
        this.teaPhone = teaPhone;
    }

    public String getItName() {
        return itName;
    }

    public void setItName(String itName) {
        this.itName = itName;
    }

    public String getItPhone() {
        return itPhone;
    }

    public void setItPhone(String itPhone) {
        this.itPhone = itPhone;
    }


}
