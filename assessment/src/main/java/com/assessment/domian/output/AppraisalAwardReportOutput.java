package com.assessment.domian.output;

import com.assessment.model.AppraisalPlan;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author: XiGuoQing
 * @description: 考核奖报表
 * @date: Created in 下午 1:48 2018/10/10 0010
 * @modified by:
 */
public class AppraisalAwardReportOutput extends AppraisalPlan implements Serializable{
  private Integer id;

  @Override
  public Integer getId() {
    return id;
  }

  @Override
  public void setId(Integer id) {
    this.id = id;
  }

  //单位：员工所属机构名称
  private String organizationName;

  //窗口(员工属于窗口时显示窗口号)/后台(员工属于后台时显示后台号)
  private String windowNoOrOffice;

  public String getWindowNoOrOffice() {

    return windowNoOrOffice;
  }

  public void setWindowNoOrOffice(String windowNoOrOffice) {
    this.windowNoOrOffice = windowNoOrOffice;
  }

  //员工姓名
  private String employeeName;

  //市民卡银行卡号(员工基本信息)
  private String bankCardNumber;

  /*
  考核奖：基准考核奖金配置在系统参数中，默认300，基准考核得分奖金配置在系统参数中，默认20。
  考核奖计算公式：基准考核奖金+（该月员工考核得分-该月员工考核计划基准分）*基准考核得分奖金。不得小于0。
   */
  private Integer appraisalAward;

  public Integer getAppraisalAward() {
    return appraisalAward;
  }

  public void setAppraisalAward(Integer appraisalAward) {
    this.appraisalAward = appraisalAward;
  }

  //服务明星(是，值为90，否，值为"")
  private Integer isStar;

  @Override
  public Integer getIsStar() {
    return isStar;
  }

  @Override
  public void setIsStar(Integer isStar) {
    this.isStar = isStar;
  }

  //合计：考核奖+服务明星
  private Integer total;

  public Integer getTotal() {
    return total;
  }

  public void setTotal(Integer total) {
    this.total = total;
  }

  //备注：该月员工考核得分与基准分的差异，例如“加1.5分”、“减0.5分”
  private String description;

  public AppraisalAwardReportOutput() {
  }


  public String getOrganizationName() {
    return organizationName;
  }

  public void setOrganizationName(String organizationName) {
    this.organizationName = organizationName;
  }

  public String getEmployeeName() {
    return employeeName;
  }

  public void setEmployeeName(String employeeName) {
    this.employeeName = employeeName;
  }

  public String getBankCardNumber() {
    return bankCardNumber;
  }

  public void setBankCardNumber(String bankCardNumber) {
    this.bankCardNumber = bankCardNumber;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  //基准值(该月员工考核计划基准分)
  private BigDecimal datumValue;
  //最终得分(该月员工考核得分)
  private BigDecimal finalScore;

  @Override
  public BigDecimal getDatumValue() {
    return datumValue;
  }

  @Override
  public void setDatumValue(BigDecimal datumValue) {
    this.datumValue = datumValue;
  }

  @Override
  public BigDecimal getFinalScore() {
    return finalScore;
  }

  @Override
  public void setFinalScore(BigDecimal finalScore) {
    this.finalScore = finalScore;
  }
}
