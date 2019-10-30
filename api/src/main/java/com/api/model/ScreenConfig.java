package com.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "screen_config")
public class ScreenConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "screenConfigGenerator")
    @SequenceGenerator(name = "screenConfigGenerator", sequenceName = "screenConfig_sequence", allocationSize = 1, initialValue = 1)
    private Integer id;

    @Column(length = 8)
    private Integer parentId;

    @Column(length = 64)
    private String name;

    @Column(length = 64)
    private String configNo;

    @Column(length = 64)
    private String configKey;

    @Column(length = 64)
    private String configValue;

    @Column(length = 8)
    private Integer disPlayOrderBy;

    @Column(nullable = false)
    private Integer hisChild;

    @Column(nullable = false)
    private Integer state;

    @Column(nullable = true,length = 64)
    private String resourceMoudle;



    @Column(nullable = false)
    private Integer createdUserId;//

    @Column(nullable = false,length = 55)
    private String createdUserName;//

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdDateTime;//

    @Column(nullable = false)
    private Integer lastUpdateUserId;//

    @Column(nullable = false)
    private String lastUpdateUserName;//

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastUpdateDateTime;//


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getConfigNo() {
        return configNo;
    }

    public void setConfigNo(String configNo) {
        this.configNo = configNo;
    }

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    public Integer getDisPlayOrderBy() {
        return disPlayOrderBy;
    }

    public void setDisPlayOrderBy(Integer disPlayOrderBy) {
        this.disPlayOrderBy = disPlayOrderBy;
    }

    public Integer getHisChild() {
        return hisChild;
    }

    public void setHisChild(Integer hisChild) {
        this.hisChild = hisChild;
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

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getResourceMoudle() {
        return resourceMoudle;
    }

    public void setResourceMoudle(String resourceMoudle) {
        this.resourceMoudle = resourceMoudle;
    }


}
