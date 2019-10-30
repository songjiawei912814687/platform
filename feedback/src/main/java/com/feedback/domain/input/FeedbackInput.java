package com.feedback.domain.input;

import com.feedback.model.FeedbackInfo;

import java.util.List;

/**
 * @author: young
 * @project_name: svn
 * @description:
 * @date: Created in 2018-12-17  08:53
 * @modified by:
 */
public class FeedbackInput  {

    List<FeedbackInfo> feedbackInfoList;

    public List<FeedbackInfo> getFeedbackInfoList() {
        return feedbackInfoList;
    }

    public void setFeedbackInfoList(List<FeedbackInfo> feedbackInfoList) {
        this.feedbackInfoList = feedbackInfoList;
    }
}
