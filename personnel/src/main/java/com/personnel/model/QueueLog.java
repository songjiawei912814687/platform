package com.personnel.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.Date;

/**
 * @author: young
 * @project_name: platfrom
 * @description: 取号叫号系统日志
 * @date: Created in 2019-02-13  09:40
 * @modified by:
 */
@Entity
public class QueueLog {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QueueLogGenerator")
    @SequenceGenerator(name = "QueueLogGenerator", sequenceName = "QueueLog_sequence",allocationSize = 1,initialValue = 1)
    private Integer id;

    @ApiModelProperty(value="创建时间",name="createdDateTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdDateTime;

    @ApiModelProperty(value="跟新时间",name="lastUpdateDateTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastUpdateDateTime;

    /**事项*/
    private String matters;

    /**对应取号叫号窗口或者人员的主键*/
    private Integer resourceId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getMatters() {
        return matters;
    }

    public void setMatters(String matters) {
        this.matters = matters;
    }

    public Integer getResourceId() {
        return resourceId;
    }

    public void setResourceId(Integer resourceId) {
        this.resourceId = resourceId;
    }
}
