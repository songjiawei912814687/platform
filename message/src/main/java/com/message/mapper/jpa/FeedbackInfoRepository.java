package com.message.mapper.jpa;

import com.message.core.base.BaseMapper;
import com.message.model.FeedbackInfo;

import java.util.List;


/**
 * @author: Young
 * @description:
 * @date: Created in 15:27 2018/11/5
 * @modified by:
 */
public interface FeedbackInfoRepository extends BaseMapper<FeedbackInfo,Integer> {

    List<FeedbackInfo> findAllBySendStateAndAppraiseState(Integer sendState, Integer appraiseState);
}
