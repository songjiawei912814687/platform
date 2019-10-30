package com.feedback.domain.output;

import com.feedback.model.FeedbackInfo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author: XiGuoQing
 * @description: 部门满意度统计报表
 */
public class DeptSatisfactionOutput extends FeedbackInfo implements Serializable{

  private String organizationName;//部门名称

  @Override
  public String getOrganizationName() {
    return organizationName;
  }

  @Override
  public void setOrganizationName(String organizationName) {
    this.organizationName = organizationName;
  }

  private Integer organizationId;

  @Override
  public Integer getOrganizationId() {
    return organizationId;
  }

  @Override
  public void setOrganizationId(Integer organizationId) {
    this.organizationId = organizationId;
  }

  private Integer satisfactionNumber;//满意数
  private Integer notSatisfactionNumber;//不满意数
  private String satisfactionRate;//满意率
  private String implementRate;//实现率
  private Integer runOnce;//跑一次
  private Integer runMany;//跑多次
  private Integer total;//总数

  public Integer getTotal() {
    return total;
  }

  public void setTotal(Integer total) {
    this.total = total;
  }

  public Integer getSatisfactionNumber() {
    return satisfactionNumber;
  }

  public void setSatisfactionNumber(Integer satisfactionNumber) {
    this.satisfactionNumber = satisfactionNumber;
  }

  public Integer getNotSatisfactionNumber() {
    return notSatisfactionNumber;
  }

  public void setNotSatisfactionNumber(Integer notSatisfactionNumber) {
    this.notSatisfactionNumber = notSatisfactionNumber;
  }

  public Integer getRunOnce() {
    return runOnce;
  }

  public void setRunOnce(Integer runOnce) {
    this.runOnce = runOnce;
  }

  public Integer getRunMany() {
    return runMany;
  }

  public void setRunMany(Integer runMany) {
    this.runMany = runMany;
  }

  public String getSatisfactionRate() {
    return satisfactionRate;
  }

  public void setSatisfactionRate(String satisfactionRate) {
    this.satisfactionRate = satisfactionRate;
  }

  public String getImplementRate() {
    return implementRate;
  }

  public void setImplementRate(String implementRate) {
    this.implementRate = implementRate;
  }
}
