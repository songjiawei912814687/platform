package com.knowledge.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Clob;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "material_list")
@ApiModel(value = "MaterialList材料清单", description = "材料清单")
@Data
@NoArgsConstructor
@AllArgsConstructor
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
    @Length(min=1, max=500, message="材料名称长度只能在{min}和{max}之间")
    @Column(nullable = false, length = 500)
    @ApiModelProperty(value="材料名称,长度最大500",name="name",required=true)
    private String name;

    @Length(min=0, max=500, message="材料形式长度只能在{min}和{max}之间")
    @Column(nullable = false, length = 500)
    @ApiModelProperty(value="材料形式,长度最大500",name="name",required=true)
    private String materialForm;

    @Length(min=0, max=500, message="材料详细要求长度只能在{min}和{max}之间")
    @Column(length = 500)
    @ApiModelProperty(value="材料详细要求",name="requestDetail")
    private String requestDetail;

    @Length(min=0, max=500, message="必要性描述长度只能在{min}和{max}之间")
    @Column(length = 500)
    @ApiModelProperty(value="必要性描述",name="necessarilyDescription")
    private String necessarilyDescription;

    @Length(min=0, max=500, message="材料出具单位长度只能在{min}和{max}之间")
    @Column(length = 500)
    @ApiModelProperty(value="材料出具单位,长度最大500",name="meteriaOrganization")
    private String meteriaOrganization;

    @Length(min=0, max=500, message="备注长度只能在{min}和{max}之间")
    @Column(length = 500)
    @ApiModelProperty(value="备注,长度最大500",name="remark")
    private String remark;

    @Range(min=0, max=999999, message="排序只能在{min}和{max}之间")
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

    /**所属组织id*/
    private Integer orgId;

    /**是否是最小颗粒的材料清单0-不是，1-是*/
    private Integer isMini;

    //示例表
    @Transient
    private List<Attachment> exampleAttachmentList;

    //空白表
    @Transient
    private List<Attachment> blankAttachmentList;

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

}
