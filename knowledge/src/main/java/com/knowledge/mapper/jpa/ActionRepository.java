package com.knowledge.mapper.jpa;


import com.knowledge.core.base.BaseMapper;
import com.knowledge.model.Action;

public interface ActionRepository extends BaseMapper<Action, Integer> {
    Action getById(Integer id);

    void deleteById(Integer id);

}
