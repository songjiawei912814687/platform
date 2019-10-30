package com.attendance.domian.output;

import com.attendance.model.LeaveApplication;
import com.common.Enum.CheckStatusEnum;
import com.common.Enum.CollectionStatusEnum;
import com.common.Enum.IsCyleEnum;
import com.common.Enum.LeaveApplicationStatusEnum;

import java.util.Date;

public class LeaveApplicationOutput extends LeaveApplication {


    //组织名称
    private String organizationName;

    //姓名
    private String employeesName;

    //审核状态名称
    private String statusName;

    //请假类型名称
    private String applicationTypeName;

    //查看 是否是补录
    private String collectionName;

    //查看是否循环
    private String cycleName;

    private Date verificationTimeOne;

    private Date verificationTimeTwo;

    @Override
    public Date getVerificationTimeOne() {
        return verificationTimeOne;
    }

    @Override
    public void setVerificationTimeOne(Date verificationTimeOne) {
        this.verificationTimeOne = verificationTimeOne;
    }

    @Override
    public Date getVerificationTimeTwo() {
        return verificationTimeTwo;
    }

    @Override
    public void setVerificationTimeTwo(Date verificationTimeTwo) {
        this.verificationTimeTwo = verificationTimeTwo;
    }

    public String getCollectionName() {
        if(getIsCollection()!=null){
            switch (getIsCollection()){
                case 1:
                    collectionName =  CollectionStatusEnum.UN_COLLECTION.getMsg();
                    break;
                case 3:
                    collectionName= CollectionStatusEnum.IS_COLLECTION.getMsg();
                    break;
            }
        }
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    public String getCycleName() {
        if(getIsCycle()!=null){
           switch (getIsCycle()){
               case 1:
               cycleName = IsCyleEnum.UN_CYLE.getMsg();
               break;
               case 3:
               cycleName = IsCyleEnum.IS_CYLE.getMsg();
               break;
           }
        }
        return cycleName;
    }

    public void setCycleName(String cycleName) {
        this.cycleName = cycleName;
    }

    public String getStatusName() {
        if(getStatus()!=null){
           return CheckStatusEnum.getMsg(getStatus());
        }
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getApplicationTypeName() {
        return LeaveApplicationStatusEnum.getByCodeMsg(getApplicationType());
    }
    public void setApplicationTypeName(String applicationTypeName) {
        this.applicationTypeName = applicationTypeName;
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
}
