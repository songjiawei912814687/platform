package com.attendance.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author: young
 * @project_name: assist-decision
 * @description: 核销假用于考勤日报表查询核销假的时间
 * @date: Created in 2019-03-29  11:59
 * @modified by:
 */
@Entity
@Data
public class Verification implements Serializable {

    private static final long serialVersionUID = 7243520861118196355L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "VerificationGenerator")
    @SequenceGenerator(name = "VerificationGenerator", sequenceName = "Verification_sequence",allocationSize = 1,initialValue = 1)
    @Column(length = 8)
    private Integer id;

    @Column(length = 8,nullable = false)
    private Integer leaveApplicationId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date verificationTimeOne;


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date verificationTimeTwo;


    @ApiModelProperty(value="创建时间",name="createdDateTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdDateTime;


    @ApiModelProperty(value="最后更新时间",name="lastUpdateDateTime")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastUpdateDateTime;


    @ApiModelProperty(value="是否删除",name="amputated")
    private  Integer amputated;
}
