package com.assessment.service;

import com.assessment.mapper.jpa.ConfigRepository;
import com.assessment.model.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ConfigService {

    @Autowired
    private ConfigRepository configRepository;


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

}
