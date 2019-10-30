package com.api.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author: young
 * @project_name: svn
 * @description: 取号源数据表
 * @date: Created in 2019-01-11  14:44
 * @modified by:
 */
@Entity
@Table(name="QUEUEING_LIST")
public class QueueingList implements Serializable {

    private static final long serialVersionUID = -3497100553833343797L;
    @Id
    @Column(length = 20)
    private Integer id;

    /**服务序号**/
    private Integer sort_index;

    /**排队序号**/
    private Integer sequence;

    /**带前缀的服务序号**/
    private String full_sequence;

    /**身份证号码*/
    private String code;

    /**群众名称**/
    private String name;

    /**群众手机**/
    private String mobile;

    /**优先规则编号*/
    private String priority_code;

    /**队列编号,窗口号**/
    private String group_code;

    /**取号时间*/
    private Date creation_time;

    /**呼叫时间**/
    private Date call_time;

    /**服务时间*/
    private Date service_time;

    /**服务员工*/
    private String service_emp_code;

    /**服务窗口号*/
    private String service_counter_code;

    /**完成办理时间*/
    private Date finish_time;

    /**记录状态*/
    private String status_code;

    /**部门编号*/
    private String dept_code;

    /**交叉记录id*/
    private Integer cross_target_id;

    /**工作时间段编号*/
    private String schedule_code;

    /**预约时间*/
    private Date appointed_date;

    /**来源*/
    private String source;

    /**报到序号*/
    private Integer checkin_sort_index;

    /**原生队列记录*/
    private Integer native_flag;

    /**办理事项名称*/
    private String matters;

    private Integer suspend;

    private Date standby_begin_time;

    private String serial_no;

    private String creator;

    /**同步状态0-未同步 1-已同步*/
    private Integer state=0;

    public QueueingList() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSort_index() {
        return sort_index;
    }

    public void setSort_index(Integer sort_index) {
        this.sort_index = sort_index;
    }

    public Integer getSequence() {
        return sequence;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public String getFull_sequence() {
        return full_sequence;
    }

    public void setFull_sequence(String full_sequence) {
        this.full_sequence = full_sequence;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPriority_code() {
        return priority_code;
    }

    public void setPriority_code(String priority_code) {
        this.priority_code = priority_code;
    }

    public String getGroup_code() {
        return group_code;
    }

    public void setGroup_code(String group_code) {
        this.group_code = group_code;
    }

    public Date getCreation_time() {
        return creation_time;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getMatters() {
        return matters;
    }

    public void setMatters(String matters) {
        this.matters = matters;
    }

    public void setCreation_time(Date creation_time) {
        this.creation_time = creation_time;
    }

    public Date getCall_time() {
        return call_time;
    }

    public void setCall_time(Date call_time) {
        this.call_time = call_time;
    }

    public Date getService_time() {
        return service_time;
    }

    public void setService_time(Date service_time) {
        this.service_time = service_time;
    }

    public String getService_emp_code() {
        return service_emp_code;
    }

    public void setService_emp_code(String service_emp_code) {
        this.service_emp_code = service_emp_code;
    }

    public String getService_counter_code() {
        return service_counter_code;
    }

    public void setService_counter_code(String service_counter_code) {
        this.service_counter_code = service_counter_code;
    }

    public Date getFinish_time() {
        return finish_time;
    }

    public void setFinish_time(Date finish_time) {
        this.finish_time = finish_time;
    }

    public String getStatus_code() {
        return status_code;
    }

    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }

    public String getDept_code() {
        return dept_code;
    }

    public void setDept_code(String dept_code) {
        this.dept_code = dept_code;
    }

    public Integer getCross_target_id() {
        return cross_target_id;
    }

    public void setCross_target_id(Integer cross_target_id) {
        this.cross_target_id = cross_target_id;
    }

    public String getSchedule_code() {
        return schedule_code;
    }

    public void setSchedule_code(String schedule_code) {
        this.schedule_code = schedule_code;
    }

    public Date getAppointed_date() {
        return appointed_date;
    }

    public void setAppointed_date(Date appointed_date) {
        this.appointed_date = appointed_date;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Integer getCheckin_sort_index() {
        return checkin_sort_index;
    }

    public void setCheckin_sort_index(Integer checkin_sort_index) {
        this.checkin_sort_index = checkin_sort_index;
    }

    public Integer getNative_flag() {
        return native_flag;
    }

    public void setNative_flag(Integer native_flag) {
        this.native_flag = native_flag;
    }

    public Integer getSuspend() {
        return suspend;
    }

    public void setSuspend(Integer suspend) {
        this.suspend = suspend;
    }

    public Date getStandby_begin_time() {
        return standby_begin_time;
    }

    public void setStandby_begin_time(Date standby_begin_time) {
        this.standby_begin_time = standby_begin_time;
    }

    public String getSerial_no() {
        return serial_no;
    }

    public void setSerial_no(String serial_no) {
        this.serial_no = serial_no;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }
}
