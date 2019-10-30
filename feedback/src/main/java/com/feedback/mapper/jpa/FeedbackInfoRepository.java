package com.feedback.mapper.jpa;

import com.feedback.core.base.BaseMapper;
import com.feedback.model.FeedbackInfo;

import java.util.List;

/**
 * @author: Young
 * @description:
 * @date: Created in 15:27 2018/11/5
 * @modified by:
 */
public interface FeedbackInfoRepository  extends BaseMapper<FeedbackInfo,Integer> {

    List<FeedbackInfo> findAllBySendState(Integer sendState);

    //根据主键查询
    FeedbackInfo findFeedbackInfoById(Integer id);
}
