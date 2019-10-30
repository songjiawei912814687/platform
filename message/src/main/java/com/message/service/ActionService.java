package com.message.service;

import com.message.core.base.BaseMapper;
import com.message.core.base.BaseService;
import com.message.core.base.MybatisBaseMapper;
import com.message.domain.output.ActionOutput;
import com.message.mapper.jpa.ActionRepository;
import com.message.mapper.mybatis.ActionMapper;
import com.message.model.Action;
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
