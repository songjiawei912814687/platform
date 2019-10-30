package com.stamp.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author: Young
 * @description:
 * @date: Created in 15:22 2018/9/5
 * @modified by:
 */
@Entity
@DynamicUpdate
@Table(name = "attachment")
@Data
public class Attachment implements Serializable {

    private static final long serialVersionUID = 1400470781527999669L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "attachmentGenerator")
    @SequenceGenerator(name = "attachmentGenerator", sequenceName = "attachmentNew_sequence",allocationSize = 1,initialValue = 1)
    @Column(length = 8)
    private Integer id;

    @ApiModelProperty(value = "资源id", name = "resourcesId", required = true)
    @Column(length = 8,nullable = false)
    private Integer resourcesId;

    @ApiModelProperty(value = "资源类型", name = "resourcesType", required = true)
    @Column(length = 3,nullable = false)
    private Integer resourcesType;

    @ApiModelProperty(value = "附件路径", name = "url", required = true)
    @Column(nullable = false)
    private String url;

    @ApiModelProperty(value = "文件名", name = "fileName", required = true)
    @Column(length =105,nullable = false)
    private String fileName;

    @ApiModelProperty(value = "后缀名", name = "suffix", required = true)
    @Column(length = 10,nullable = false)
    private String suffix;

    @ApiModelProperty(value = "原文件名", name = "sourceFileName", required = true)
    @Column(length = 105,nullable = false)
    private String sourceFileName;

    @ApiModelProperty(value = "文件大小", name = "attachmentSize", required = true)
    @Column(length = 11,nullable = false)
    private Integer attachmentSize;

    @ApiModelProperty(value = "标签", name = "tag")
    @Column(length = 105)
    private String tag;

    @ApiModelProperty(value = "逻辑删除", name = "amputated")
    @Column(length = 1)
    private Integer amputated;

    @Column(nullable = true, length = 6)
    @ApiModelProperty(value = "创建人ID", name = "createdUserId")
    private Integer createdUserId;

    @Column(nullable = true, length = 55)
    @ApiModelProperty(value = "创建人name", name = "createdUserName")
    private String createdUserName;

    @Column(nullable = true)
    @ApiModelProperty(value = "创建时间", name = "createdDateTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdDateTime;

    @Column(nullable = true)
    @ApiModelProperty(value = "最后更新时间", name = "lastUpdateDateTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastUpdateDateTime;

    @Column(nullable = true, length = 6)
    @ApiModelProperty(value = "最后更新人Id", name = "lastUpdateUserId")
    private Integer lastUpdateUserId;

    @Column(nullable = true, length = 55)
    @ApiModelProperty(value = "最后更新人name", name = "lastUpdateUserName")
    private String lastUpdateUserName;

    public Attachment() {
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getResourcesId() {
        return resourcesId;
    }

    public void setResourcesId(Integer resourcesId) {
        this.resourcesId = resourcesId;
    }

    public Integer getResourcesType() {
        return resourcesType;
    }

    public void setResourcesType(Integer resourcesType) {
        this.resourcesType = resourcesType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getSourceFileName() {
        return sourceFileName;
    }

    public void setSourceFileName(String sourceFileName) {
        this.sourceFileName = sourceFileName;
    }

    public Integer getAttachmentSize() {
        return attachmentSize;
    }

    public void setAttachmentSize(Integer attachmentSize) {
        this.attachmentSize = attachmentSize;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Integer getAmputated() {
        return amputated;
    }

    public void setAmputated(Integer amputated) {
        this.amputated = amputated;
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
}
