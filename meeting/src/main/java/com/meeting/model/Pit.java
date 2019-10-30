package com.meeting.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "pit")
public class Pit implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "pitGenerator")
    @SequenceGenerator(name = "pitGenerator",sequenceName = "pitNew_sequence",allocationSize = 1,initialValue = 1)
    @Column(length = 6)
    private Integer id;

    @NotBlank(message = "名字不能为空")
    @Length(min=1, max=255, message="名称长度只能在{min}和{max}之间")
    @Column(nullable = false,length = 255,unique = true)
    @ApiModelProperty(value="名称,长度最长255",name="name",required=true)
    private String name;

    @Range(min = 10,max = 100,message = "会议室容纳人数最少为10人，最多为100人")
    @Column(length = 3,nullable = false)
    @ApiModelProperty(value = "可容纳人数",name = "containNumber")
    private Integer containNumber;

    private String ip;

    @Length(min = 0,max = 255,message = "描述内容长度只能在{min}和{max}之间")
    @Column(length = 255)
    @ApiModelProperty(value = "描述",name = "description")
    private String description;

    @Column(nullable = false,length = 1)
    @ApiModelProperty(value = "类型",name = "state")
    private Integer type;

    @Column(nullable = false,length = 1)
    @ApiModelProperty(value = "排序",name = "displayOrder")
    private Integer displayOrder;

    @Column(nullable = false,length = 6)
    @ApiModelProperty(value="创建人ID",name="createdUserId")

    private Integer createdUserId;//

    @Column(nullable = false,length = 55)
    @ApiModelProperty(value="创建人name,最大长度55",name="createdUserName")
    private String createdUserName;//

    @Column(nullable = false)
    @ApiModelProperty(value="创建时间,最大长度7",name="createdDateTime")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createdDateTime;//

    @Column(nullable = false)
    @ApiModelProperty(value="最后更新时间,最大长度7",name="lastUpdateDateTime")
    private Date lastUpdateDateTime;//

    @Column(nullable = false,length = 19)
    @ApiModelProperty(value="最后更新人Id,最大长度19",name="lastUpdateUserId")
    private Integer lastUpdateUserId;//

    @Column(nullable = false,length = 55)
    @ApiModelProperty(value="最后更新人name,最大长度55",name="lastUpdateUserName")
    private String lastUpdateUserName;//

    @Column(nullable = false,length = 6)
    @ApiModelProperty(value = "是否删除",name = "isDelete")
    private Integer amputated;

    @Transient
    private List<String> ipList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getContainNumber() {
        return containNumber;
    }

    public void setContainNumber(Integer containNumber) {
        this.containNumber = containNumber;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
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

    public Integer getAmputated() {
        return amputated;
    }

    public void setAmputated(Integer amputated) {
        this.amputated = amputated;
    }

    public List<String> getIpList() {
        return ipList;
    }

    public void setIpList(List<String> ipList) {
        this.ipList = ipList;
    }
}
