package com.api.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.api.config.RedisComponent;
import com.api.domain.output.DoConditionOutput;
import com.api.domain.output.DoingOutput;
import com.api.mapper.mybatis.FeedbackInfoMapper;
import com.api.mapper.mybatis.StationPeopleMapper;
import com.api.mapper.mybatis.WindowMapper;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author: Young
 * @description: 前20个工作日的办事情形
 * @date: Created in 15:26 2018/11/20
 * @modified by:
 */
@Service
public class DoConditionService {

    @Autowired
    private TimeService timeService;
    @Autowired
    private FeedbackInfoMapper feedbackInfoMapper;
    @Autowired
    private RedisComponent redisComponent;


    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public DoingOutput findPageList() {

        List<DoConditionOutput> doConditionOutputList = Lists.newArrayList();
        DoingOutput doingOutput = new DoingOutput();
        int allCount = 0;
        //先从redis中获取前19个工作日的办事情形
        Object object = redisComponent.getHash("doConditionOutputList","doConditionOutputList");
        if(object!=null){
            //从redis中取出前19个工作日的办事情形
            doConditionOutputList = JSONArray.parseArray(object.toString(),DoConditionOutput.class);
        }else {
            try {
                //如果没取到值，就开始进行算法计算
                List<String> dateStringList = timeService.getWorkDays(new Date(),-20);

                if(dateStringList ==null||dateStringList.size() == 0){
                    return null;
                }
                for(String date:dateStringList){
                    DoConditionOutput doConditionOutput = new DoConditionOutput();
                    Integer feedbackCount  = feedbackInfoMapper.selectByFeedbackTime(date);
                    if(feedbackCount==null){
                        doConditionOutput.setCount(0);
                    }else {
                        doConditionOutput.setCount(feedbackCount);
                    }
                    allCount+=doConditionOutput.getCount();
                    doConditionOutput.setData(date);
                    doConditionOutputList.add(doConditionOutput);
                }

                   redisComponent.setHash("doConditionOutputList","doConditionOutputList", JSONArray.toJSON(doConditionOutputList).toString(),3600*8L);
                }catch (Exception e) {
                    e.printStackTrace();
            }

        }

        allCount = doConditionOutputList.stream().mapToInt(DoConditionOutput::getCount).sum();
        //统计今天的
        DoConditionOutput doConditionOutput = new DoConditionOutput();
        doConditionOutput.setData(sdf.format(new Date()));

        Integer feedbackCount = feedbackInfoMapper.selectByFeedbackTime(sdf.format(new Date()));
        if(feedbackCount==null){
            doConditionOutput.setCount(0);
        }else {
            doConditionOutput.setCount(feedbackCount);
        }

        doConditionOutputList.add(doConditionOutput);

        doingOutput.setDoConditionOutputList(doConditionOutputList);
        doingOutput.setTodayCount(doConditionOutput.getCount());
        doingOutput.setAllCount(doConditionOutput.getCount()+allCount);
        return doingOutput;
    }
}
