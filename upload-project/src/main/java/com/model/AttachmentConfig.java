package com.model;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author: Young
 * @description:
 * @date: Created in 15:44 2018/9/5
 * @modified by:
 */
@Entity
@Table(name = "attachment_config")
public class AttachmentConfig implements Serializable {

    private static final long serialVersionUID = 3256998577030993646L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(length = 6, nullable = false)
    private Integer id;

    @Column(length = 55, nullable = false)
    private String configName;

    @Column(length = 3, nullable = false)
    private Integer configType;

    @Column(length = 11)
    private Integer maxSize;

    @Column(length = 6)
    private Integer minSize;

    @Column(length = 2000)
    private String allowFiles;

    @Column(length = 105, nullable = false)
    private String urlPrefix;

    @Column(length = 11)
    private Integer compressMaxWidth;

    @Column()
    private String description;

    @Column(length = 1, nullable = false)
    private Integer state;

    @Column(nullable = true, length = 6)
    private Integer createdUserId;

    @Column(nullable = true, length = 55)
    private String createdUserName;

    @Column(nullable = true)
    private Date createdDateTime;

    @Column(nullable = true)
    private Date lastUpdateDateTime;

    @Column(nullable = true, length = 6)
    private Integer lastUpdateUserId;

    @Column(nullable = true, length = 55)
    private String lastUpdateUserName;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public Integer getConfigType() {
        return configType;
    }

    public void setConfigType(Integer configType) {
        this.configType = configType;
    }

    public Integer getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(Integer maxSize) {
        this.maxSize = maxSize;
    }

    public Integer getMinSize() {
        return minSize;
    }

    public void setMinSize(Integer minSize) {
        this.minSize = minSize;
    }

    public String getAllowFiles() {
        return allowFiles;
    }

    public void setAllowFiles(String allowFiles) {
        this.allowFiles = allowFiles;
    }

    public String getUrlPrefix() {
        return urlPrefix;
    }

    public void setUrlPrefix(String urlPrefix) {
        this.urlPrefix = urlPrefix;
    }

    public Integer getCompressMaxWidth() {
        return compressMaxWidth;
    }

    public void setCompressMaxWidth(Integer compressMaxWidth) {
        this.compressMaxWidth = compressMaxWidth;
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

    public AttachmentConfig() {
    }
}
