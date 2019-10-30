package com.knowledge.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@DynamicUpdate
@Entity
@Table(name = "minimum_particle")
@ApiModel(value = "MinimumParticle最小颗粒", description = "最小颗粒")
@Getter
@Setter
public class MinimumParticle implements Serializable {
    @Id
    @Column(length = 6)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "minimumParticleGenerator")
    @SequenceGenerator(name = "minimumParticleGenerator", sequenceName = "minimumParticle_sequence",allocationSize = 1,initialValue = 1)
    @ApiModelProperty(value="最小颗粒id,后台生成无法修改",name="id")
    private Integer id;

    @Length(min=0, max=40, message="权力事项id长度只能在{min}和{max}之间")
    @Column( length = 40)
    @ApiModelProperty(value="权力事项id",name="qlsxId",required=true)
    private String qlsxId;

    @Column(length = 50)
    private String qlInnerCode;//权力内部码

    @Column(length = 1000)
    @ApiModelProperty(value="办事情形,长度最大1000",name="type")
    private String happeningType;

    @Column(length = 1000)
    @ApiModelProperty(value="办理流程,长度最大1000",name="type")
    private String process;

    @Range(min=1, max=999999, message="排序只能在{min}和{max}之间")
    @Column(length = 6)
    @ApiModelProperty(value="排序",name="displayOrder")
    private Integer displayOrder;


    @Column(nullable = false,length = 1)
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

    //上级最小颗粒id
    @Range(min=0,max = 999999,message = "上级最小颗粒只能为0-999999之间的数字")
    @Column(nullable = true, length = 6)
    @ApiModelProperty(value="上级最小颗粒id,长度最大6,若未选择上级最小颗粒id默认为0",name="parentId",required=true)
    private Integer parentId;

    //最小颗粒路径
    @Length(min=1, max=999, message="描述长度只能在{min}和{max}之间")
    @Column(nullable = true,length = 999)
    @ApiModelProperty(value="最小颗粒路径,长度最大999，后台处理",name="path")
    private String path;
    /**组织id*/
    private Integer orgId;
    //空白表
    @Transient
    private Integer selectAll;

}
