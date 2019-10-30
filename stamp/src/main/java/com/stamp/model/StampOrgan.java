package com.stamp.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author: young
 * @project_name: svn
 * @description: 刻章组织
 * @date: Created in 2019-04-18  09:47
 * @modified by:
 */
@Entity
@Data
public class StampOrgan implements Serializable {
    private static final long serialVersionUID = -5417762405591068893L;
    @Id
    @Column(length = 6)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stampOrganGenerator")
    @SequenceGenerator(name = "stampOrganGenerator", sequenceName = "stamp_organ_sequence",allocationSize = 1,initialValue = 1)
    @ApiModelProperty(value="刻章组织机构id,后台生成无法修改",name="organizationNo")
    private Integer id;

    //组织机构编号
    @Column(length = 30)
    @ApiModelProperty(value="组织机构编号,长度最大30",name="organizationNo")
    private String organizationNo;

    @Column(nullable = false,length = 105)
    @ApiModelProperty(value="组织机构名称,长度最大105",name="name",required=true)
    private String name;

    //上级组织机构
    @Column(nullable = false, length = 6)
    @ApiModelProperty(value="上级组织机构,长度最大6,若未选择上级组织机构默认为0",name="parentId",required=true)
    private Integer parentId;

    //机构类型
    @Column(nullable = false, length = 2)
    private Integer type;

    //员工工号首字母
    @Column(nullable = true, length = 2)
    private Integer firstLetter;

    @Column(length = 12)
    @ApiModelProperty(value="手机号,长度最大12",name="phoneNumber")
    private String phoneNumber;

    @Column(length = 255)
    @ApiModelProperty(value="地址，长度最大255",name="address")
    private String address;

    //组织机构描述
    @Column(length = 255)
    @ApiModelProperty(value="描述,长度最大255",name="description")
    private String description;

    @Column(length = 20,nullable = true)
    @ApiModelProperty(value="部门管理员,长度最大20",name="departmentalManager")
    private String departmentalManager;

    @Column(nullable = false,length = 1)
    @ApiModelProperty(value="是否删除,长度最大1，默认0",name="amputated",required = true)
    private Integer amputated;

    @Column(nullable = true,length = 511)
    @ApiModelProperty(value="组织路径,长度最大511，后台处理",name="path")
    private String path;

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

    @Column(length = 3,nullable = false)
    @ApiModelProperty(value="排序,长度不超过3",name="displayOrder",required=true)
    private Integer displayOrder;

}
