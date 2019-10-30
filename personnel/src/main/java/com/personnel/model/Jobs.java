package com.personnel.model;

import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "jobs")
public class Jobs implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "jobsGenerator")
    @SequenceGenerator(name = "jobsGenerator", sequenceName = "jobsNew_sequence",allocationSize = 1,initialValue = 1)
    @Column(length = 6)
    private Integer id;

    @NotNull
    @Length(min=1, max=55, message="职务名称长度只能在{min}和{max}之间")
    @Column(nullable = false,length = 55)
    @ApiModelProperty(value="职务名称,最大长度55",name="name",required=true)
    private String name;


    @Column(length = 255)
    @ApiModelProperty(value="职责,最大长度105",name="responsibilities")
    private String responsibilities;

    @Column(nullable = false,length = 6)
    @ApiModelProperty(value="创建人ID,最大长度6",name="createdUserId")
    private Integer createdUserId;//

    @Column(nullable = false,length = 55)
    @ApiModelProperty(value="创建人name,最大长度55",name="createdUserName")
    private String createdUserName;//

    @Column(nullable = false)
    @ApiModelProperty(value="创建时间",name="createdDateTime")
    private Date createdDateTime;//

    @Column(nullable = false)
    @ApiModelProperty(value="最后更新时间",name="lastUpdateDateTime")
    private Date lastUpdateDateTime;//

    @Column(nullable = false,length = 6)
    @ApiModelProperty(value="最后更新人Id,最大长度6",name="lastUpdateUserId")
    private Integer lastUpdateUserId;//

    @Column(nullable = false,length = 55)
    @ApiModelProperty(value="最后更新人name,最大长度55",name="lastUpdateUserName")
    private String lastUpdateUserName;//

    @Column(nullable =false,length = 6)
    @ApiModelProperty(value="是否删除,最大长度6",name="amputated")
    private  int amputated;

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

    public String getResponsibilities() {
        return responsibilities;
    }

    public void setResponsibilities(String responsibilities) {
        this.responsibilities = responsibilities;
    }

    public Integer getCreatedUserId() {
        return createdUserId;
    }

    public void setCreatedUserId(Integer createdUserId) {
        this.createdUserId = createdUserId;
    }

    public void setLastUpdateUserId(Integer lastUpdateUserId) {
        this.lastUpdateUserId = lastUpdateUserId;
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

    public int getAmputated() {
        return amputated;
    }

    public void setAmputated(int amputated) {
        this.amputated = amputated;
    }
}
