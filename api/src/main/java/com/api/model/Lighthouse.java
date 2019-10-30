package com.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author: young
 * @project_name: svn
 * @description:2.5	获取曝光台接口设计
 * @date: Created in 2018-12-07  15:17
 * @modified by:
 */
@Entity
public class Lighthouse implements Serializable {


    @Id
    @Column(length = 6)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LighthouseGenerator")
    @SequenceGenerator(name = "LighthouseGenerator", sequenceName = "Lighthouse_sequence",allocationSize = 1,initialValue = 1)
    private Integer id;

    /**类别*/
    private String classValue;

    /**标题*/
    private String title;

    /**时间*/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private String publishStartTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdDateTime;


    public Lighthouse() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getClassValue() {
        return classValue;
    }

    public void setClassValue(String classValue) {
        this.classValue = classValue;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublishStartTime() {
        return publishStartTime;
    }

    public void setPublishStartTime(String publishStartTime) {
        this.publishStartTime = publishStartTime;
    }

    public Date getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(Date createdDateTime) {
        this.createdDateTime = createdDateTime;
    }
}
