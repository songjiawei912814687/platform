package com.feedback.domain.output;

import com.feedback.model.FeedbackInfo;

import java.io.Serializable;

public class WindowSatisfactionStatisticsOutput extends FeedbackInfo implements Serializable {

    private String organizationName;//部门名称

    @Override
    public String getOrganizationName() {
        return organizationName;
    }

    @Override
    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    private Integer organizationId;

    @Override
    public Integer getOrganizationId() {
        return organizationId;
    }

    @Override
    public void setOrganizationId(Integer organizationId) {
        this.organizationId = organizationId;
    }


    private Integer satisfactoryNumbers;
    private Integer unsatisfactoryNumbers;
    private String satisfactionRate;
    private Integer runOneTimes;
    private Integer runManyTimes;
    private String realizationRate;
    private Integer sum;//总数
    private Integer count;


    public Integer getSatisfactoryNumbers() {
        return satisfactoryNumbers;
    }

    public void setSatisfactoryNumbers(Integer satisfactoryNumbers) {
        this.satisfactoryNumbers = satisfactoryNumbers;
    }

    public Integer getUnsatisfactoryNumbers() {
        return unsatisfactoryNumbers;
    }

    public void setUnsatisfactoryNumbers(Integer unsatisfactoryNumbers) {
        this.unsatisfactoryNumbers = unsatisfactoryNumbers;
    }

    public String getSatisfactionRate() {
        return satisfactionRate;
    }

    public void setSatisfactionRate(String satisfactionRate) {
        this.satisfactionRate = satisfactionRate;
    }

    public Integer getRunOneTimes() {
        return runOneTimes;
    }

    public void setRunOneTimes(Integer runOneTimes) {
        this.runOneTimes = runOneTimes;
    }

    public Integer getRunManyTimes() {
        return runManyTimes;
    }

    public void setRunManyTimes(Integer runManyTimes) {
        this.runManyTimes = runManyTimes;
    }

    public String getRealizationRate() {
        return realizationRate;
    }

    public void setRealizationRate(String realizationRate) {
        this.realizationRate = realizationRate;
    }

    public Integer getSum() {
        return sum;
    }

    public void setSum(Integer sum) {
        this.sum = sum;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}
