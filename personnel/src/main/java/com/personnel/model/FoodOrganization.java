package com.personnel.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author: young
 * @project_name: svn
 * @description:
 * @date: Created in 2019-01-08  17:59
 * @modified by:
 */
@Entity
@Table(name = "FOOD_ORGANIZATION")
public class FoodOrganization implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "foodorgGenerator")
    @SequenceGenerator(name = "foodorgGenerator", sequenceName = "foodorgNew_sequence",allocationSize = 1,initialValue = 1)
    private Integer id;

    @Column(length = 10,nullable = false)
    private String sDepCode;

    @Column(length = 10)
    private String sParentDepCode;

    @Column(length = 20,nullable = false)
    private String sDepName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdDateTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastUpdateDateTime;

    /**下发状态0-未下发,1-已下发，2-已删除**/
    @Column(length = 2,nullable = false)
    private Integer state;

    public String getsDepCode() {
        return sDepCode;
    }

    public void setsDepCode(String sDepCode) {
        this.sDepCode = sDepCode;
    }

    public String getsParentDepCode() {
        return sParentDepCode;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setsParentDepCode(String sParentDepCode) {
        this.sParentDepCode = sParentDepCode;
    }

    public String getsDepName() {
        return sDepName;
    }

    public void setsDepName(String sDepName) {
        this.sDepName = sDepName;
    }

    public Date getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(Date createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public Date getLastUpdateDateTime() {
        return lastUpdateDateTime;
    }

    public void setLastUpdateDateTime(Date lastUpdateDateTime) {
        this.lastUpdateDateTime = lastUpdateDateTime;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "{" +
                "sDepCode:'" + sDepCode + '\'' +
                ", sParentDepCode:'" + sParentDepCode + '\'' +
                ", sDepName:'" + sDepName + '\'' +
                '}';
    }

    public String toStringDept() {
        return "{" +
                "SData:[{"+
                "sDepCode:'" + sDepCode + '\'' +
                '}'+"]}";
    }
}
