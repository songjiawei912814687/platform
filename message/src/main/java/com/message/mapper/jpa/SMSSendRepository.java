package com.message.mapper.jpa;


import com.message.core.base.BaseMapper;
import com.message.model.SMSSend;

import java.util.List;

public interface SMSSendRepository extends BaseMapper<SMSSend, Integer> {
    List<SMSSend> findAllByState(Integer state);

    List<SMSSend> findAllByType(Integer type);

    List<SMSSend> findAllByStateIn(List<Integer>state);

}
