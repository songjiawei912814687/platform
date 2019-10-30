package com.wechatsug.domain.output;


import com.wechatsug.model.AppConsts;
import com.wechatsug.model.Suggestions;

public class SuggesstionsOutput extends Suggestions {

    private String userName;

    private String outDays;

    private String departId;

    public String getDepartId() {
        return departId;
    }

    public void setDepartId(String departId) {
        this.departId = departId;
    }

    public void setPublishName(String publishName) {
        this.publishName = publishName;
    }

    public void setOutOfDateName(String outOfDateName) {
        this.outOfDateName = outOfDateName;
    }

    public void setDealStateName(String dealStateName) {
        this.dealStateName = dealStateName;
    }

    public void setDateResourceName(String dateResourceName) {
        this.dateResourceName = dateResourceName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    private String phoneNumber;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    private String publishName;

    private Integer repId;

    public Integer getRepId() {
        if(getReplyUserId()!=null){
            repId = getReplyUserId();
        }
        return repId;
    }

    public void setRepId(Integer repId) {
        this.repId = repId;
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

    private String dealStateName;

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

    private String dateResourceName;

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

    public String getOutDays() {
        return outDays;
    }

    public void setOutDays(String outDays) {
        this.outDays = outDays;
    }



}
