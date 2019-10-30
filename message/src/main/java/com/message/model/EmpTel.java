package com.message.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Pattern;

/**
 * @Author QuanCZS 2019/5/30 14:29
 */

@Entity
@Table(name = "EmpTel")
@ApiModel(value = "empTel员工", description = "员工")
public class EmpTel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "empTELGenerator")
    @SequenceGenerator(name = "empTELGenerator", sequenceName = "empTEL_sequence",allocationSize = 1,initialValue = 1)
    @Column(length = 8)
    private Integer id;

    @Length(min=1, max=55, message="用户名长度只能在{min}和{max}之间")
    @Column(nullable = false,length = 55)
    @ApiModelProperty(value="会议主题，长度不大于55",name="name",required=true)
    private String keyWord;

    @Pattern(regexp = "^1\\d{10}$",message = "手机号码格式不合法")
    @Column(nullable = false,length = 12)
    @ApiModelProperty(value="手机号码",name="phoneNumber",required=true)
    private String mobilPhone;

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public String getMobilPhone() {
        return mobilPhone;
    }

    public void setMobilPhone(String mobilPhone) {
        this.mobilPhone = mobilPhone;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
