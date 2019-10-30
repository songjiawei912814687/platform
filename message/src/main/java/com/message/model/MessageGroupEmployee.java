package com.message.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author: XiGuoQing
 * @description:
 * @date: Created in 下午 6:53 2018/10/25 0025
 * @modified by:
 */
@Entity
@Table(name = "MESSAGE_GROUP_EMPLOYEE")
@ApiModel(value = "messageGroupEmployee短信小组成员", description = "短信小组成员")
public class MessageGroupEmployee implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "messageGEGenerator")
  @SequenceGenerator(name = "messageGEGenerator", sequenceName = "messageGE_sequence",allocationSize = 1,initialValue = 1)
  @Column(length = 8)
  private Integer id;

  @Column(length = 8)
  private Integer groupId;

  @Column(length = 8)
  private Integer employeeId;


  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getGroupId() {
    return groupId;
  }

  public void setGroupId(Integer groupId) {
    this.groupId = groupId;
  }

  public Integer getEmployeeId() {
    return employeeId;
  }

  public void setEmployeeId(Integer employeeId) {
    this.employeeId = employeeId;
  }
}
