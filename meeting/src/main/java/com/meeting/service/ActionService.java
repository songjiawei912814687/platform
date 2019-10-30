package com.meeting.service;

import com.meeting.core.base.BaseMapper;
import com.meeting.core.base.BaseService;
import com.meeting.core.base.MybatisBaseMapper;
import com.meeting.domain.output.ActionOutput;
import com.meeting.mapper.jpa.ActionRepository;
import com.meeting.mapper.mybatis.ActionMapper;
import com.meeting.model.Action;
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
