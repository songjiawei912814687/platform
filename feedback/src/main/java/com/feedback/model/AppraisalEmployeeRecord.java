package com.feedback.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "appraisal_employee_record")
@ApiModel(value = "员工加减分记录AppraisalEmployeeRecord", description = "员工加减分记录")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppraisalEmployeeRecord implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AppraisalRecordGenerator")
    @SequenceGenerator(name = "AppraisalRecordGenerator", sequenceName = "AppraisalRecordNew_sequence",allocationSize = 1,initialValue = 1)
    @Column(length = 8)
    private Integer id;

    @Column(nullable = false)
    private Integer appraisalId;

    @Column(nullable = false)
    private Integer ruleId;

    @Column(nullable = false)
    private Integer organizationId;

    @Column(nullable = false)
    private Integer employeeId;

    @Column(nullable = false)
    private BigDecimal value;

    private BigDecimal limits;

    @Column(nullable = false)
    private Integer scoreType;

    @Column(nullable = false)
    private Integer scoreSource;

    @Column(nullable = false)
    private BigDecimal defaultScore;

    @Column(nullable = false)
    @ApiModelProperty(value="记录时间",name="recordDateTime")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date recordDateTime;

    @Column(nullable = false)
    private BigDecimal score;

    @Column(nullable = false)
    private Integer state;

    @Column(length=255)
    @ApiModelProperty(value="描述",name="description")
    private String description;

    @Column(nullable = false)
    private Integer displayOrder;

    @Column(nullable = false)
    @ApiModelProperty(value="创建人ID",name="createdUserId")
    private Integer createdUserId;

    @Column(nullable = false,length = 80)
    @ApiModelProperty(value="创建人name",name="createdUserName")
    private String createdUserName;

    @Column(nullable = false)
    @ApiModelProperty(value="创建时间",name="createdDateTime")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createdDateTime;

    @Column(nullable = false)
    @ApiModelProperty(value="最后更新时间",name="lastUpdateDateTime")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date lastUpdateDateTime;

    @Column(nullable = false)
    @ApiModelProperty(value="最后更新人Id",name="lastUpdateUserId")
    private Integer lastUpdateUserId;

    @Column(nullable = false,length = 80)
    @ApiModelProperty(value="最后更新人name",name="lastUpdateUserName")
    private String lastUpdateUserName;

    @Column(nullable =false,length = 6)
    @ApiModelProperty(value="是否删除,0-未删除 1-已删除 默认为0",name="amputated",example = "0")
    private  Integer amputated;

    /**是否有效 0-有效 1-无效**/
    private Integer isValid;

    @Transient
    private List<Attachment> attachmentList;

}
