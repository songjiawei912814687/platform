package com.personnel.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "person")
@ApiModel(value = "person人员", description = "人员")
public class Person implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "personGenerator")
  @SequenceGenerator(name = "personGenerator", sequenceName = "personNew_sequence",allocationSize = 1,initialValue = 1)
  @Column(length = 8)
  private Integer id;

  @Length(min=1, max=55, message="用户名长度只能在{min}和{max}之间")
  @Column(nullable = false,length = 55)
  @ApiModelProperty(value="名字，长度不大于55",name="name",required=true)
  private String name;

  @Column(nullable = false,length = 55)
  @ApiModelProperty(value="人员工号",name="personNo")
  private String personNo;

  @Column(nullable = false,length = 55)
  @ApiModelProperty(value="人员编号",name="personId")
  private Integer personId;

  @Column(nullable = false,length = 8)
  @ApiModelProperty(value="员工id,最大长度8",name="employeeId",required=true)
  private Integer employeeId;

  private Integer gender;//性别：0 男 1 女

  @Column(length = 12)
  private String phoneNo;//手机号

  private Integer state;

  @ApiModelProperty(value="创建时间",name="createdDateTime")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private Date createdDateTime;//

  @ApiModelProperty(value="下发成功时间",name="lastUpdateDateTime")
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private Date lastUpdateDateTime;

  private String description;

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
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

  public String getPersonNo() {
    return personNo;
  }

  public void setPersonNo(String personNo) {
    this.personNo = personNo;
  }

  public Integer getPersonId() {
    return personId;
  }

  public void setPersonId(Integer personId) {
    this.personId = personId;
  }

  public Integer getEmployeeId() {
    return employeeId;
  }

  public void setEmployeeId(Integer employeeId) {
    this.employeeId = employeeId;
  }

  public Integer getGender() {
    return gender;
  }

  public void setGender(Integer gender) {
    this.gender = gender;
  }

  public String getPhoneNo() {
    return phoneNo;
  }

  public void setPhoneNo(String phoneNo) {
    this.phoneNo = phoneNo;
  }

  public Integer getState() {
    return state;
  }

  public void setState(Integer state) {
    this.state = state;
  }

  private String path;

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  private String deptName;

  public String getDeptName() {
    return deptName;
  }

  public void setDeptName(String deptName) {
    this.deptName = deptName;
  }

  private String deptNo;

  public String getDeptNo() {
    return deptNo;
  }

  public void setDeptNo(String deptNo) {
    this.deptNo = deptNo;
  }

  private String deptUuid;

  public String getDeptUuid() {
    return deptUuid;
  }

  public void setDeptUuid(String deptUuid) {
    this.deptUuid = deptUuid;
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
}
