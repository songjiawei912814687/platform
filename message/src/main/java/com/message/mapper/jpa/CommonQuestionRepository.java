package com.message.mapper.jpa;

import com.message.core.base.BaseMapper;
import com.message.model.CommonQuestion;
import org.springframework.transaction.annotation.Transactional;

public interface CommonQuestionRepository extends BaseMapper<CommonQuestion,Integer> {

    @Transactional(rollbackFor = Exception.class)
    void deleteByQlInnerCode(String qlInnerCode);
}
