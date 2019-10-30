package com.stamp.service;


import com.stamp.core.base.BaseRepository;
import com.stamp.core.base.BaseService;
import com.stamp.core.base.MybatisBaseMapper;
import com.stamp.domain.output.ActionOutput;
import com.stamp.mapper.jpa.ActionRepository;
import com.stamp.mapper.mybatis.ActionMapper;
import com.stamp.model.Action;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Service
public class ActionService extends BaseService<ActionOutput, Action,Integer> {

    @Autowired
    private ActionMapper actionMapper;

    @Autowired
    private ActionRepository repository;

    @Override
    public BaseRepository<Action, Integer> getMapper() {
        return repository;
    }

    @Override
    public MybatisBaseMapper<ActionOutput> getMybatisBaseMapper() {
        return actionMapper;
    }

    public Map<String, List<String>> getActionOutput(List<ActionOutput> actions) {
        Map<String, List<String>> map = new HashMap<>();
        if (actions.size() <= 0) {
            return null;
        }
        List<Action> action = actions.stream().filter(Action -> Action.getCode().indexOf("A_SYSTEM") >= 0).collect(toList());
        List<String> idList = action.stream().map(Action::getCode).collect(toList());
        for (var str : idList) {
            map.put(str, new ArrayList<>());
        }

        return map;
    }
    public List<ActionOutput> getByIdIn(List<Integer> ids){
        return actionMapper.findByRoles(ids);
    }

    public List<ActionOutput> findByRoleId(Integer roleId){
        return actionMapper.findByRoleId(roleId);
    }


    public List<ActionOutput> selectAllTow(){
        return actionMapper.selectAllTow();
    }


}
