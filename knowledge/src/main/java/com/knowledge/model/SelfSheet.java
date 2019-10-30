package com.knowledge.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author: young
 * @description: 自助填单
 * @date: Created in 2019-08-21  14:41
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SelfSheet implements Serializable {

    @Id
    @Column(length = 6)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "selfSheetGenerator")
    @SequenceGenerator(name = "selfSheetGenerator", sequenceName = "selfSheet_sequence",allocationSize = 1,initialValue = 1)
    private Integer id;

    /**表单名称*/
    private String name;

    /**路由地址*/
    @Column(length = 50)
    private String url;

    /**是否展示 0不展示 1展示*/
    @Column(length = 2)
    private Integer display=1;

    /**类型*/
    @Column(length = 2)
    private Integer type;

    /**是否删除*/
    @Column(length = 1)
    @ApiModelProperty(value="是否删除,长度最大1，默认0",name="amputated")
    private Integer amputated=0;

    @ApiModelProperty(value="创凯时间",name="createdDateTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdDateTime;

    @Column(length = 6)
    @ApiModelProperty(value="创建人ID,长度最大6",name="createdUserId")
    private Integer createdUserId;

    @Column(length = 55)
    @ApiModelProperty(value="创建人name,长度最大55",name="createdUserName")
    private String createdUserName;

    @ApiModelProperty(value="最后更新时间",name="lastUpdateDateTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastUpdateDateTime;

    @Column(length = 6)
    @ApiModelProperty(value="最后更新人Id,长度最大6",name="lastUpdateUserId")
    private Integer lastUpdateUserId;

    @Column(length = 55)
    @ApiModelProperty(value="最后更新人name,长度最大55",name="lastUpdateUserName")
    private String lastUpdateUserName;


}
