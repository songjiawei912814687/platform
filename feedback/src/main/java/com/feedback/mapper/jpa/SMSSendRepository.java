package com.feedback.mapper.jpa;

import com.feedback.core.base.BaseMapper;
import com.feedback.model.SMSSend;

import java.util.List;

public interface SMSSendRepository extends BaseMapper<SMSSend, Integer> {
    List<SMSSend> findAllByState(Integer state);

    List<SMSSend> findAllByType(Integer type);

}
