package com.api.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

/**
 * @author: young
 * @project_name: svn
 * @description: 评价结果源数据
 * @date: Created in 2019-01-11  19:44
 * @modified by:
 */
@Entity
public class EvaluateResult implements Serializable {
    private static final long serialVersionUID = -3466863048646492956L;

    @Id
    @Column(length = 15)
    private Integer id;

    /**对应排队记录ID*/
    private Integer queueing_list_id;

    /**被评价的员工编号*/
    private String  employee_code;

    /**被评价的窗口*/
    private String counter_code;

    /**评价编号*/
    private String evaluate_code;

    /**评价值*/
    private Integer evaluate_value;

    /**评价人手机号码*/
    private String appraiser_mobile;

    /**评价描述*/
    private String eval_description;

    /**评价时间*/
    private Date creation_time;

    /**部门编号*/
    private String dept_code;

    /**同步状态0-未同步 1-已同步*/
    private Integer state=0;

    public EvaluateResult() {
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getQueueing_list_id() {
        return queueing_list_id;
    }

    public void setQueueing_list_id(Integer queueing_list_id) {
        this.queueing_list_id = queueing_list_id;
    }

    public String getEmployee_code() {
        return employee_code;
    }

    public void setEmployee_code(String employee_code) {
        this.employee_code = employee_code;
    }

    public String getCounter_code() {
        return counter_code;
    }

    public void setCounter_code(String counter_code) {
        this.counter_code = counter_code;
    }

    public String getEvaluate_code() {
        return evaluate_code;
    }

    public void setEvaluate_code(String evaluate_code) {
        this.evaluate_code = evaluate_code;
    }

    public Integer getEvaluate_value() {
        return evaluate_value;
    }

    public void setEvaluate_value(Integer evaluate_value) {
        this.evaluate_value = evaluate_value;
    }

    public String getAppraiser_mobile() {
        return appraiser_mobile;
    }

    public void setAppraiser_mobile(String appraiser_mobile) {
        this.appraiser_mobile = appraiser_mobile;
    }

    public String getEval_description() {
        return eval_description;
    }

    public void setEval_description(String eval_description) {
        this.eval_description = eval_description;
    }

    public Date getCreation_time() {
        return creation_time;
    }

    public void setCreation_time(Date creation_time) {
        this.creation_time = creation_time;
    }

    public String getDept_code() {
        return dept_code;
    }

    public void setDept_code(String dept_code) {
        this.dept_code = dept_code;
    }
}
