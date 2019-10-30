package com.personnel.model;import com.fasterxml.jackson.annotation.JsonFormat;import io.swagger.annotations.ApiModel;import io.swagger.annotations.ApiModelProperty;import org.hibernate.validator.constraints.Length;import javax.persistence.*;import java.io.Serializable;import java.util.Date;/** * @author: Administrator */@Entity@Table(name = "CITYCARD")@ApiModel(value = "cityCard市民卡", description = "市民卡")public class CityCard implements Serializable {  @Id  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cityCardGenerator")  @SequenceGenerator(name = "cityCardGenerator", sequenceName = "cityCardNew_sequence",allocationSize = 1,initialValue = 1)  @Column(length = 8)  private Integer id;  @Column(nullable = false,length = 55)  @ApiModelProperty(value="人员编号",name="personId")  private Integer personId;  @Column(nullable = false,length = 55)  @ApiModelProperty(value="车牌号",name="cityCardNo")  private String cityCardNo;  @Length(min=1, max=55, message="用户名长度只能在{min}和{max}之间")  @Column(nullable = false,length = 55)  @ApiModelProperty(value="名字，长度不大于55",name="name",required=true)  private String name;  @Column(nullable = false,length = 55)  @ApiModelProperty(value="人员工号",name="personNo")  private String personNo;  @Column(length = 12)  private String phoneNo;  private Integer state;  @ApiModelProperty(value="创建时间",name="createdDateTime")  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")  private Date createdDateTime;//  @ApiModelProperty(value="下发成功时间",name="lastUpdateDateTime")  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")  private Date lastUpdateDateTime;  public Integer getId() {    return id;  }  public void setId(Integer id) {    this.id = id;  }  public Integer getPersonId() {    return personId;  }  public void setPersonId(Integer personId) {    this.personId = personId;  }  public String getCityCardNo() {    return cityCardNo;  }  public void setCityCardNo(String cityCardNo) {    this.cityCardNo = cityCardNo;  }  public String getName() {    return name;  }  public void setName(String name) {    this.name = name;  }  public String getPersonNo() {    return personNo;  }  public void setPersonNo(String personNo) {    this.personNo = personNo;  }  public String getPhoneNo() {    return phoneNo;  }  public void setPhoneNo(String phoneNo) {    this.phoneNo = phoneNo;  }  public Integer getState() {    return state;  }  public void setState(Integer state) {    this.state = state;  }  public Date getCreatedDateTime() {    return createdDateTime;  }  public void setCreatedDateTime(Date createdDateTime) {    this.createdDateTime = createdDateTime;  }  public Date getLastUpdateDateTime() {    return lastUpdateDateTime;  }  public void setLastUpdateDateTime(Date lastUpdateDateTime) {    this.lastUpdateDateTime = lastUpdateDateTime;  }}