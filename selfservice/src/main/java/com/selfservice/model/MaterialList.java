package com.selfservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


@DynamicUpdate
@Entity
@Table(name = "material_list")
@ApiModel(value = "MaterialList材料清单", description = "材料清单")
public class MaterialList implements Serializable {
    @Id
    @Column(length = 6)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "materialListGenerator")
    @SequenceGenerator(name = "materialListGenerator", sequenceName = "materialList_sequence",allocationSize = 1,initialValue = 1)
    @ApiModelProperty(value="材料清单id,后台生成无法修改",name="id")
    private Integer id;

    @NotNull
    @Range(min=0, max=999999, message="最小颗粒id只能在{min}和{max}之间")
    @Column(nullable = false, length = 6)
    @ApiModelProperty(value="最小颗粒id",name="minimumParticleId",required=true)
    private Integer minimumParticleId;

    @NotBlank
    @Length(min=1, max=105, message="材料名称长度只能在{min}和{max}之间")
    @Column(nullable = false, length = 105)
    @ApiModelProperty(value="材料名称,长度最大105",name="name",required=true)
    private String name;

    @NotBlank
    @Length(min=1, max=50, message="材料形式长度只能在{min}和{max}之间")
    @Column(nullable = false, length = 50)
    @ApiModelProperty(value="材料形式,长度最大50",name="name",required=true)
    private String materialForm;

    @Length(min=1, max=105, message="材料详细要求长度只能在{min}和{max}之间")
    @Column(length = 105)
    @ApiModelProperty(value="材料详细要求",name="requestDetail")
    private String requestDetail;

    @Length(min=1, max=105, message="必要性描述长度只能在{min}和{max}之间")
    @Column(length = 105)
    @ApiModelProperty(value="必要性描述",name="necessarilyDescription")
    private String necessarilyDescription;

    @Length(min=1, max=105, message="材料出具单位长度只能在{min}和{max}之间")
    @Column(length = 105)
    @ApiModelProperty(value="材料出具单位,长度最大105",name="meteriaOrganization")
    private String meteriaOrganization;

    @Length(min=1, max=255, message="备注长度只能在{min}和{max}之间")
    @Column(length = 255)
    @ApiModelProperty(value="备注,长度最大255",name="remark")
    private String remark;

    @Range(min=1, max=999999, message="排序只能在{min}和{max}之间")
    @Column(length = 6)
    @ApiModelProperty(value="排序",name="displayOrder")
    private Integer displayOrder;

    @Range(min=0,max = 1,message = "是否删除只能为0或1")
    @Column(nullable = false,length = 1)
    @ApiModelProperty(value="是否删除,长度最大1，默认0",name="amputated",required = true)
    private Integer amputated;

    @Column(nullable = false,length = 6)
    @ApiModelProperty(value="创建人ID,长度最大6",name="createdUserId")
    private Integer createdUserId;

    @Column(nullable = false,length = 55)
    @ApiModelProperty(value="创建人name,长度最大55",name="createdUserName")
    private String createdUserName;

    @Column(nullable = false)
    @ApiModelProperty(value="创建时间",name="createdDateTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdDateTime;

    @Column(nullable = false)
    @ApiModelProperty(value="最后更新时间",name="lastUpdateDateTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastUpdateDateTime;

    @Column(nullable = false,length = 6)
    @ApiModelProperty(value="最后更新人Id,长度最大6",name="lastUpdateUserId")
    private Integer lastUpdateUserId;

    @Column(nullable = false,length = 55)
    @ApiModelProperty(value="最后更新人name,长度最大55",name="lastUpdateUserName")
    private String lastUpdateUserName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getMinimumParticleId() {
        return minimumParticleId;
    }

    public void setMinimumParticleId(Integer minimumParticleId) {
        this.minimumParticleId = minimumParticleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMaterialForm() {
        return materialForm;
    }

    public void setMaterialForm(String materialForm) {
        this.materialForm = materialForm;
    }

    public String getRequestDetail() {
        return requestDetail;
    }

    public void setRequestDetail(String requestDetail) {
        this.requestDetail = requestDetail;
    }

    public String getNecessarilyDescription() {
        return necessarilyDescription;
    }

    public void setNecessarilyDescription(String necessarilyDescription) {
        this.necessarilyDescription = necessarilyDescription;
    }

    public String getMeteriaOrganization() {
        return meteriaOrganization;
    }

    public void setMeteriaOrganization(String meteriaOrganization) {
        this.meteriaOrganization = meteriaOrganization;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
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

    //示例表
    @Transient
    private Attachment exampleAttachment;

    //空白表
    @Transient
    private Attachment blankAttachment;

    public Attachment getExampleAttachment() {
        return exampleAttachment;
    }

    public void setExampleAttachment(Attachment exampleAttachment) {
        this.exampleAttachment = exampleAttachment;
    }

    public Attachment getBlankAttachment() {
        return blankAttachment;
    }

    public void setBlankAttachment(Attachment blankAttachment) {
        this.blankAttachment = blankAttachment;
    }

    //示例表地址
    @Column(nullable = true,length = 2000)
    private String exampleTableFileUrl;
    //示例表名称
    @Column(nullable = true,length = 512)
    private String exampleTableFileName;
    //空白表地址
    @Column(nullable = true,length = 2000)
    private String emptyTableFileUrl;
    //空白表名称
    @Column(nullable = true,length = 512)
    private String emptyTableFileName;

    public String getExampleTableFileUrl() {
        return exampleTableFileUrl;
    }

    public void setExampleTableFileUrl(String exampleTableFileUrl) {
        this.exampleTableFileUrl = exampleTableFileUrl;
    }

    public String getExampleTableFileName() {
        return exampleTableFileName;
    }

    public void setExampleTableFileName(String exampleTableFileName) {
        this.exampleTableFileName = exampleTableFileName;
    }

    public String getEmptyTableFileUrl() {
        return emptyTableFileUrl;
    }

    public void setEmptyTableFileUrl(String emptyTableFileUrl) {
        this.emptyTableFileUrl = emptyTableFileUrl;
    }

    public String getEmptyTableFileName() {
        return emptyTableFileName;
    }

    public void setEmptyTableFileName(String emptyTableFileName) {
        this.emptyTableFileName = emptyTableFileName;
    }
}
