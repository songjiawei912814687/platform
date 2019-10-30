package com.assistdecision.service;


import com.assistdecision.core.base.BaseMapper;
import com.assistdecision.core.base.BaseService;
import com.assistdecision.core.base.MybatisBaseMapper;
import com.assistdecision.domain.output.ActionOutput;
import com.assistdecision.mapper.jpa.ActionRepository;
import com.assistdecision.mapper.mybatis.ActionMapper;
import com.assistdecision.model.Action;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class ActionService extends BaseService<ActionOutput, Action,Integer> {

    @Autowired
    private ActionMapper actionMapper;

    @Autowired
    private ActionRepository repository;



    public List<ActionOutput> getByIdIn(List<Integer> ids){
        return actionMapper.findByRoles(ids);
    }

    @Override
    public BaseMapper<Action, Integer> getMapper() {
        return repository;
    }




    public MybatisBaseMapper<ActionOutput> getMybatisBaseMapper() {
        return actionMapper;
    }




}
