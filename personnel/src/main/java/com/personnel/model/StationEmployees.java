package com.personnel.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
@Entity
@Table(name = "station_employees")
@ApiModel(value = "station_employees员工中间表", description = "员工中间表")
public class StationEmployees  implements Serializable  {
    @Id
    private Integer id;

    @Column(nullable = false,length = 55)
    @ApiModelProperty(value="工号，工号后台程序生成且不可修改",name="employeesNo")
    private String employeeNo;

    @NotBlank(message = "名字不能为空")
    @Length(min=1, max=55, message="用户名长度只能在{min}和{max}之间")
    @Column(nullable = false,length = 55)
    @ApiModelProperty(value="名字，长度不大于55",name="name",required=true)
    private String name;

    @Range(min=0,max = 1,message = "是否删除只能为0或1")
    @Column(nullable =false,length = 6)
    @ApiModelProperty(value="是否删除,0-未删除 1-已删除 默认为0",name="amputated",example = "0")
    private  Integer amputated;

    @Range(min=0,max = 1,message = "是否上传到税务系统只能为0或1")
    @Column(nullable =false,length = 6)
    @ApiModelProperty(value="是否上传,0-未上传 1-已上传 默认为0",name="amputated",example = "0")
    private Integer state;

    public StationEmployees(){

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getAmputated() {
        return amputated;
    }

    public void setAmputated(Integer amputated) {
        this.amputated = amputated;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
