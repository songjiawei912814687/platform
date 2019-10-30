package com.screenData.service;

import com.screenData.mapper.jpa.ConfigRepository;
import com.screenData.mapper.mybatis.ConfigMapper;
import com.screenData.model.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConfigService {

    @Autowired
    private ConfigRepository configRepository;
    @Autowired
    private ConfigMapper configMapper;


    public Config getByKey(String key) {
        return configRepository.findByConfigKey(key);
    }

    public List<Config> getListByKey(String key) {
        var config = getByKey(key);
        return configRepository.findByParentId(config.getId());
    }

    public List<Config> getListByParentId(Integer id) {
        return configRepository.findByParentId(id);
    }

    public List<Config> getListByParentIdAndState(Integer id,Integer state){
        return configMapper.selectByIdAndState(id,state);
    }

}
