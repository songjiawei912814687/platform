package com.stamp.mapper.jpa;


import com.stamp.core.base.BaseRepository;
import com.stamp.model.SMSSend;

import java.util.List;

public interface SMSSendRepository extends BaseRepository<SMSSend, Integer> {
    List<SMSSend> findAllByState(Integer state);

    List<SMSSend> findAllByType(Integer type);

    List<SMSSend> findAllByStateIn(List<Integer> state);

}
