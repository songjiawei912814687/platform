package com.personnel.service;

import com.personnel.core.base.BaseMapper;
import com.personnel.core.base.BaseService;
import com.personnel.core.base.MybatisBaseMapper;
import com.personnel.domian.output.ActionOutput;
import com.personnel.mapper.jpa.ActionRepository;
import com.personnel.mapper.mybatis.ActionMapper;
import com.personnel.model.Action;
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
