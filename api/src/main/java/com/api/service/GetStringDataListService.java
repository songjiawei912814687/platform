package com.api.service;

import com.alibaba.fastjson.JSONObject;
import com.api.config.RedisComponent;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author: young
 * @project_name: platform
 * @description: 获取前20个工作日的集合
 * @date: Created in 2019-05-12  13:16
 * @modified by:
 */
@Component
public class GetStringDataListService {

    @Autowired
    private TimeService timeService;
    @Autowired
    private RedisComponent redisComponent;

    public List<String> getDateStringList() throws Exception {

        List<String> dateStringList = Lists.newArrayList();

        dateStringList  = (List<String>)redisComponent.getHash("dateStringList",dateStringList);
        if(dateStringList==null||dateStringList.size() == 0){
            dateStringList = timeService.getWorkDays(new Date(),-20);
            if(dateStringList==null||dateStringList.size()==0){
                return null;
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            dateStringList.add(sdf.format(new Date()));
            redisComponent.setHash("dateStringList",dateStringList, JSONObject.toJSON(dateStringList).toString(),60*60*2L);
        }
        return dateStringList;
    }
}
