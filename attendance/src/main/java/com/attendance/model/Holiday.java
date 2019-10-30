package com.attendance.model;

import com.common.Enum.CheckStatusEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author: Young
 * @description: 节假日管理
 * @date: Created in 20:08 2018/9/11
 * @modified by:
 */
@Entity
@Table(name = "holiday")
public class Holiday implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "holidayGenerator")
    @SequenceGenerator(name = "holidayGenerator", sequenceName = "Holiday_sequence",allocationSize = 1,initialValue = 1)
    @ApiModelProperty(name = "id",value = "主键",required = true)
    @Column(nullable = false,length = 8)
    private Integer id;

    @NotNull
    @Column(nullable = false,length = 55,unique = true)
    @ApiModelProperty(name = "holidayName",value = "节假日名称",required = true)
    private String holidayName;

    @NotNull
    @Column(nullable = false)
    @ApiModelProperty(name = "startDate",value = "开始日期",required = true)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startDate;

    @NotNull
    @Column(nullable = false)
    @ApiModelProperty(name = "endDate",value = "结束日期",required = true)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date endDate;

    @NotNull
    @Column(nullable = false)
    @Range(min=1,max = 3,message = "只能填写1或者3，1为非工作日，3是工作日")
    @ApiModelProperty(name = "isWorkDay",value = "是否是工作日",required = true)
    private Integer isWorkDay;


    @Column(length = 255)
    @ApiModelProperty(name = "description",value = "描述,备注")
    private String description;

    @Column(nullable = false)
    @ApiModelProperty(name = "status",value = "状态")
    private Integer status= CheckStatusEnum.CHECK_FINISH.getCode();

    //排序
    @Column(length = 8)
    @ApiModelProperty(value = "排序", name = "orderNumber")
    private Integer orderNumber;


    @Column(length = 6,nullable = false)
    @ApiModelProperty(value="创建人ID",name="createdUserId")
    private Integer createdUserId;//


    @Column(length = 80,nullable = false)
    @ApiModelProperty(value="创建人name",name="createdUserName")
    private String createdUserName;//


    @Column(nullable = false)
    @ApiModelProperty(value="创建时间",name="createdDateTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdDateTime;//


    @Column(nullable = false)
    @ApiModelProperty(value="最后更新时间",name="lastUpdateDateTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastUpdateDateTime;//


    @Column(length = 6,nullable = false)
    @ApiModelProperty(value="最后更新人Id",name="lastUpdateUserId")
    private Integer lastUpdateUserId;//


    @Column(length = 80,nullable = false)
    @ApiModelProperty(value="最后更新人name",name="lastUpdateUserName")
    private String lastUpdateUserName;//


    @Column(length = 6,nullable = false)
    @ApiModelProperty(value="是否删除",name="amputated")
    private  int amputated;














    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHolidayName() {
        return holidayName;
    }

    public void setHolidayName(String holidayName) {
        this.holidayName = holidayName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Integer getIsWorkDay() {
        return isWorkDay;
    }

    public void setIsWorkDay(Integer isWorkDay) {
        this.isWorkDay = isWorkDay;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
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

    public Date getLastUpdateDateTime() {
        return lastUpdateDateTime;
    }

    public void setLastUpdateDateTime(Date lastUpdateDateTime) {
        this.lastUpdateDateTime = lastUpdateDateTime;
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


    public int getAmputated() {
        return amputated;
    }

    public void setAmputated(int amputated) {
        this.amputated = amputated;
    }
}
