package com.api.model;

import javax.persistence.*;
import java.util.Date;
/**
 * 鸿程数据表
 */
@Entity
@Table(name = "hchen_statistical")
public class HchenStatistical {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hchenGenerator")
    @SequenceGenerator(name = "hchenGenerator", sequenceName = "hchen_sequence",allocationSize = 1,initialValue = 1)
    private Integer id;

    @Column(nullable = false)
    private String department;

    @Column(nullable = false)
    private Long monthCount;

    @Column(nullable = false)
    private Date statisticalDateTime;

    public HchenStatistical() {
    }

    public HchenStatistical(String department, Long monthCount, Date statisticalDateTime) {
        this.department = department;
        this.monthCount = monthCount;
        this.statisticalDateTime = statisticalDateTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department == null ? null : department.trim();
    }

    public Long getMonthCount() {
        return monthCount;
    }

    public void setMonthCount(Long monthCount) {
        this.monthCount = monthCount;
    }

    public Date getStatisticalDateTime() {
        return statisticalDateTime;
    }

    public void setStatisticalDateTime(Date statisticalDateTime) {
        this.statisticalDateTime = statisticalDateTime;
    }
}
