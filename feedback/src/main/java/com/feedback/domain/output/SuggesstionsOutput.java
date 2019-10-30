package com.feedback.domain.output;


import com.feedback.core.util.AppConsts;
import com.feedback.model.Suggestions;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SuggesstionsOutput extends Suggestions {

    private String userName;

    private String phoneNumber;

    private String outDays;

    private List<String> suggestionList;

    private String publishName;

    private Integer repId;

    private String dateResourceName;

    private String dealStateName;

    private String isCallName;

    private String isUseName;

    private String satisfactionName;

    private String completeRateName;

    public String getIsUseName() {
        if(getIsUse()!=null){
            if(getIsUse()==0){
                isUseName="无效";
            }else if(getIsUse()==1){
                isUseName="有效";
            }
        }
        return isUseName;
    }

    public String getIsCallName() {
        if(getIsPhoneCall()!=null){
            if(getIsPhoneCall()==0){
                isCallName = "未回访";
            }else if(getIsPhoneCall()==1){
                isCallName = "已回访";
            }
        }
        return isCallName;
    }

    public Integer getRepId() {
        if(getReplyUserId()!=null){
            repId = getReplyUserId();
        }
        return repId;
    }

    public String getPublishName() {
        if (getPublish() != null) {
            if (getPublish() == AppConsts.Publish_No) {
                publishName = "未发布";
            } else if (getPublish() == AppConsts.Publish_Yes) {
                publishName = "已发布";
            } else if (getPublish() == AppConsts.Publish_Invalid) {
                publishName = "无效事件";
            }
        }
        return publishName;
    }

    private String outOfDateName;

    public String getOutOfDateName() {
        if (getOutOfDate() != null) {
            if (getOutOfDate() == AppConsts.OutOfDate_No) {
                outOfDateName = "未逾期";
            } else if (getOutOfDate() == AppConsts.Publish_Yes) {
                outOfDateName = "逾期";
            }
        }
        return outOfDateName;
    }
    public String getDealStateName() {
        if (getDealState() != null) {
            if (getDealState() == AppConsts.DealState_No) {
                dealStateName = "未处理";
            } else if (getDealState() == AppConsts.DealState_Yes) {
                dealStateName = "已处理";
            }
        }
        return dealStateName;
    }
    public String getDateResourceName() {
        if (getDateResource() != null) {
            if (getDateResource() == AppConsts.Resource_Phone) {
                dateResourceName = "电话";
            } else if (getDateResource() == AppConsts.Resource_OnSite) {
                dateResourceName = "现场";
            }else if (getDateResource() == AppConsts.Resource_Wechat) {
                dateResourceName = "微信";
            }else if (getDateResource() == AppConsts.Resource_Message) {
                dateResourceName = "短信";
            }else if (getDateResource() == AppConsts.Resource_Up) {
                dateResourceName = "上级下发";
            }else if (getDateResource() == AppConsts.Resource_Second) {
                dateResourceName = "二次回复";
            }
        }
        return dateResourceName;
    }
}
