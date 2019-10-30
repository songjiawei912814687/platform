package com.message.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.jdo.annotations.Unique;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author: XiGuoQing
 * @description: 短信回访系统-短信小组
 * @date: Created in 下午 3:27 2018/10/23 0023
 * @modified by:
 */

@Entity
@Table(name = "message_group")
@ApiModel(value = "messageGroup短信小组", description = "短信小组")
public class MessageGroup implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "messageGroupGenerator")
  @SequenceGenerator(name = "messageGroupGenerator", sequenceName = "messageGroup_sequence",allocationSize = 1,initialValue = 1)
  @Column(length = 8)
  private Integer id;

  @Column(nullable = false,length = 255,unique = true)
  @ApiModelProperty(value="名称，长度不大于55",name="name",required=true)
  private String name;

  @Column(nullable = false,length = 255)
  @ApiModelProperty(value="描述,长度最大255",name="description",required = true)
  private String description;

  @Column(length = 2)
  private Integer state;

  @Column(length = 2)
  @ApiModelProperty(value="排序,长度不超过3",name="displayOrder")
  private Integer displayOrder;

  @Column
  @ApiModelProperty(value="创建人ID",name="createdUserId")
  private Integer createdUserId;

  @Column(length = 80)
  @ApiModelProperty(value="创建人name",name="createdUserName")
  private String createdUserName;

  @Column
  @ApiModelProperty(value="创建时间",name="createdDateTime")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private Date createdDateTime;

  @Column
  @ApiModelProperty(value="最后更新时间",name="lastUpdateDateTime")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private Date lastUpdateDateTime;

  @Column
  @ApiModelProperty(value="最后更新人Id",name="lastUpdateUserId")
  private Integer lastUpdateUserId;

  @Column(length = 80)
  @ApiModelProperty(value="最后更新人name",name="lastUpdateUserName")
  private String lastUpdateUserName;

  @Range(min=0,max = 1,message = "是否删除只能为0或1")
  @Column(length = 6)
  @ApiModelProperty(value="是否删除,0-未删除 1-已删除 默认为0",name="amputated",example = "0")
  private  Integer amputated;

  //
  @Transient
  private List<Integer> employeeIds;

  public List<Integer> getEmployeeIds() {
    return employeeIds;
  }

  public void setEmployeeIds(List<Integer> employeeIds) {
    this.employeeIds = employeeIds;
  }

  public MessageGroup() {
  }

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

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Integer getState() {
    return state;
  }

  public void setState(Integer state) {
    this.state = state;
  }

  public Integer getDisplayOrder() {
    return displayOrder;
  }

  public void setDisplayOrder(Integer displayOrder) {
    this.displayOrder = displayOrder;
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

  public Integer getAmputated() {
    return amputated;
  }

  public void setAmputated(Integer amputated) {
    this.amputated = amputated;
  }
}
