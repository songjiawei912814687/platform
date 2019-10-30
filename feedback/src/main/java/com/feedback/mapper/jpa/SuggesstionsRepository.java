package com.feedback.mapper.jpa;


import com.feedback.core.base.BaseMapper;
import com.feedback.model.Suggestions;

import java.util.List;

public interface SuggesstionsRepository extends BaseMapper<Suggestions, Integer> {

    List<Suggestions> findAllByReplyTypeAndAmputatedAndDateResource(Integer replyType, Integer amputated, Integer resource);
}
