package com.screen.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "window_employees")
@ApiModel(value = "window_employees窗口工作人员", description = "窗口工作人员")
public class WindowEmployees {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "windowEmployeesGenerator")
    @SequenceGenerator(name = "windowEmployeesGenerator", sequenceName = "windowEmployeesNew_sequence",allocationSize = 1,initialValue = 1)
    @Column(length = 8)
    private Integer id;

    @Column(nullable =false,length = 6)
    private Integer employeeId;

    @Column(nullable = false,length = 55)
    @ApiModelProperty(value="工号，工号后台程序生成且不可修改",name="employeesNo")
    private String employeeNo;

    @NotBlank(message = "名字不能为空")
    @Column(nullable = false,length = 55)
    @ApiModelProperty(value="名字，长度不大于55",name="name",required=true)
    private String name;

    @NotBlank(message = "头像路径不能为空")
    @Column(nullable = false,length = 255)
    @ApiModelProperty(value="头像，长度不大于255",name="icon",required=true)
    private String icon;

    @Column(nullable =false,length = 6)
    private Integer organizationId;

    private Integer windowId;

    @Column(nullable =false,length =30)
    private String  satisfaction;

    private Integer  isStar;

    @NotNull
    @Pattern(regexp = "^1\\d{10}$",message = "手机号码格式不合法")
    @Column(nullable = false,length = 12)
    @ApiModelProperty(value="手机号码",name="phoneNumber",required=true)
    private String phoneNumber;

    @Column(length = 13)
    @ApiModelProperty(value="办公电话",name="officePhone")
    private String officePhone;

    @Range(min=0,max = 1,message = "是否删除只能为0或1")
    @Column(nullable =false,length = 6)
    @ApiModelProperty(value="是否删除,0-未删除 1-已删除 默认为0",name="amputated",example = "0")
    private  Integer amputated;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeNo() {
        return employeeNo;
    }

    public void setEmployeeNo(String employeeNo) {
        this.employeeNo = employeeNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }

    public Integer getWindowId() {
        return windowId;
    }

    public void setWindowId(Integer windowId) {
        this.windowId = windowId;
    }

    public String getSatisfaction() {
        return satisfaction;
    }

    public void setSatisfaction(String satisfaction) {
        this.satisfaction = satisfaction;
    }

    public Integer getIsStar() {
        return isStar;
    }

    public void setIsStar(Integer isStar) {
        this.isStar = isStar;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getOfficePhone() {
        return officePhone;
    }

    public void setOfficePhone(String officePhone) {
        this.officePhone = officePhone;
    }

    public Integer getAmputated() {
        return amputated;
    }

    public void setAmputated(Integer amputated) {
        this.amputated = amputated;
    }
}
