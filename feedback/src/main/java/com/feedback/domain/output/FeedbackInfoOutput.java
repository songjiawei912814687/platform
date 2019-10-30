package com.feedback.domain.output;

import com.common.Enum.AppraiseStateEnum;
import com.common.Enum.CompleteRateEnum;
import com.common.Enum.SatisfactionEnum;
import com.feedback.model.FeedbackInfo;

/**
 * @author: Young
 * @description:
 * @date: Created in 14:22 2018/11/5
 * @modified by:
 */
public class FeedbackInfoOutput extends FeedbackInfo {


    private String matter;

    private String satisfactionName;

    private String completeRateName;

    private String appraiseStateName;

    public String getMatter() {
        return matter;
    }

    public void setMatter(String matter) {
        this.matter = matter;
    }

    public String getAppraiseStateName() {
        if(getAppraiseState()!=null){
            switch (getAppraiseState()){
                case 0:
                    appraiseStateName = AppraiseStateEnum.UN_APPRAISE.getDesc();
                    break;
                case 1:
                    appraiseStateName = AppraiseStateEnum.IS_APPRAISE.getDesc();
                    break;
            }
        }
        return appraiseStateName;
    }

    public void setAppraiseStateName(String appraiseStateName) {
        this.appraiseStateName = appraiseStateName;
    }

    public String getSatisfactionName() {
        if(getSatisfaction()!=null){
            switch (getSatisfaction()){
                case 0:
                    satisfactionName = SatisfactionEnum.SATISFIED.getDesc();
                    break;
                case 1:
                    satisfactionName = SatisfactionEnum.UN_SATISFIED.getDesc();
                    break;
            }
        }
        return satisfactionName;
    }

    public void setSatisfactionName(String satisfactionName) {
        this.satisfactionName = satisfactionName;
    }

    public String getCompleteRateName() {
        if(getCompleteRate()!=null){
            switch (getCompleteRate()){
                case 1:
                    completeRateName = CompleteRateEnum.ONE.getDesc();
                    break;
                case 2:
                    completeRateName = CompleteRateEnum.MORE.getDesc();
                    break;
            }
        }

        return completeRateName;
    }

    public void setCompleteRateName(String completeRateName) {
        this.completeRateName = completeRateName;
    }

    private Integer satisfactionNumber;
    private Integer unSatisfactionNumber;
    private Integer runOneTimes;
    private Integer runManyTimes;

    public Integer getSatisfactionNumber() {
        return satisfactionNumber;
    }

    public void setSatisfactionNumber(Integer satisfactionNumber) {
        this.satisfactionNumber = satisfactionNumber;
    }

    public Integer getUnSatisfactionNumber() {
        return unSatisfactionNumber;
    }

    public void setUnSatisfactionNumber(Integer unSatisfactionNumber) {
        this.unSatisfactionNumber = unSatisfactionNumber;
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
}
