package com.screenData.domain.output;

import com.common.Enum.AppraiseStateEnum;
import com.common.Enum.CompleteRateEnum;
import com.common.Enum.SatisfactionEnum;
import com.screenData.model.FeedbackInfo;

/**
 * @author: Young
 * @description:
 * @date: Created in 14:22 2018/11/5
 * @modified by:
 */
public class FeedbackInfoOutput extends FeedbackInfo {


    private String satisfactionName;

    private String completeRateName;

    private String appraiseStateName;


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

    private Integer dissatisfactionCount;

    public Integer getDissatisfactionCount() {
        return dissatisfactionCount;
    }

    public void setDissatisfactionCount(Integer dissatisfactionCount) {
        this.dissatisfactionCount = dissatisfactionCount;
    }
}
