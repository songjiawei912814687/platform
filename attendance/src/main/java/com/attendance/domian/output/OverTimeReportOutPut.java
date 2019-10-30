package com.attendance.domian.output;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author: Young
 * @description:
 * @date: Created in 16:23 2018/9/27
 * @modified by:
 */

public class OverTimeReportOutPut  implements Serializable {

    private Integer id;

    private Integer organizationId;

    private Integer employeesId;
    //组织名称
    private String organizationName;

    //人员名称
    private String employeesName;

    //市民卡银行卡号
    private String bankCardNumber;

    //加班次数
    private Integer overTimeCount;

    //调休次数
    private Integer restCount;

    //总计金额
    private BigDecimal amount;

    private Integer year;

    private Integer month;

    public OverTimeReportOutPut() {
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

    public Integer getEmployeesId() {
        return employeesId;
    }

    public void setEmployeesId(Integer employeesId) {
        this.employeesId = employeesId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBankCardNumber() {
        return bankCardNumber;
    }

    public void setBankCardNumber(String bankCardNumber) {
        this.bankCardNumber = bankCardNumber;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getEmployeesName() {
        return employeesName;
    }

    public void setEmployeesName(String employeesName) {
        this.employeesName = employeesName;
    }

    public Integer getOverTimeCount() {
        return overTimeCount;
    }

    public void setOverTimeCount(Integer overTimeCount) {
        this.overTimeCount = overTimeCount;
    }

    public Integer getRestCount() {
        return restCount;
    }

    public void setRestCount(Integer restCount) {
        this.restCount = restCount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
