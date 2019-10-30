package com.api.service;

import com.alibaba.fastjson.JSONArray;
import com.api.config.RedisComponent;
import com.api.domain.output.VisitOutput;
import com.api.domain.output.VisitorsNumberOutput;
import com.api.mapper.mybatis.StationPeopleMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class VisitorsNumberService {

    @Autowired
    private TimeService timeService;
    @Autowired
    private StationPeopleMapper stationPeopleMapper;
    @Autowired
    private RedisComponent redisComponent;

    public VisitOutput findPageList() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        List<VisitorsNumberOutput> visitorsNumberOutputList= Lists.newArrayList();

        //先去redis中取前19个工作日的来访人数
        Object obj = redisComponent.getHash("visitorsNumberOutputList","visitorsNumberOutputList");
        VisitOutput visitOutput = new VisitOutput() ;
        int allTakeNumber = 0;
        int allCompNumber = 0;

        if(obj!=null){
            visitorsNumberOutputList = JSONArray.parseArray(obj.toString(),VisitorsNumberOutput.class);
        } else {
        //如果没有从redis中取到值
        try {
            List<String> dateStringList = timeService.getWorkDays(new Date(),-20);
            if(dateStringList == null||dateStringList.size() == 0){
                return null;
            }

            for(String date:dateStringList){
                VisitorsNumberOutput visitorsNumberOutput = new VisitorsNumberOutput();
                visitorsNumberOutput.setDate(date);

                //查询出取号数据
                Integer fetchNumber = stationPeopleMapper.selectByTake(date)==null?0:stationPeopleMapper.selectByTake(date);

                //设置取号数
                visitorsNumberOutput.setFetchNumber(fetchNumber);

                //查询出办结数
                Integer handleNumber = stationPeopleMapper.selectByComp(date)==null?0:stationPeopleMapper.selectByComp(date);

                //设置办结数
                visitorsNumberOutput.setHandleNumber(handleNumber);
                //总取号数
                allTakeNumber+=visitorsNumberOutput.getFetchNumber();
                //总办结数
                allCompNumber+=visitorsNumberOutput.getHandleNumber();

                visitorsNumberOutputList.add(visitorsNumberOutput);
            }
            //将前19个工作日的来访人数存入到redis
            redisComponent.setHash("visitorsNumberOutputList","visitorsNumberOutputList", JSONArray.toJSON(visitorsNumberOutputList).toString(),3600*8L);
            }catch (Exception e) {
                return null;
            }
        }

        allTakeNumber = visitorsNumberOutputList.stream().mapToInt(VisitorsNumberOutput::getFetchNumber).sum();

        allCompNumber = visitorsNumberOutputList.stream().mapToInt(VisitorsNumberOutput::getHandleNumber).sum();

        //统计出今天的办理人数
        VisitorsNumberOutput visitorsNumberOutput = new VisitorsNumberOutput();
        visitorsNumberOutput.setDate(sdf.format(new Date()));

        //查询出取号数据
        Integer todayFec = stationPeopleMapper.selectByTake(sdf.format(new Date()))==null?0:stationPeopleMapper.selectByTake(sdf.format(new Date()));
        visitorsNumberOutput.setFetchNumber(todayFec);

        //查询出办结数
        Integer todayHand = stationPeopleMapper.selectByComp(sdf.format(new Date()))==null?0:stationPeopleMapper.selectByComp(sdf.format(new Date()));

        //设置办结数
        visitorsNumberOutput.setHandleNumber(todayHand);

        visitorsNumberOutputList.add(visitorsNumberOutput);

        visitOutput.setVisitorsNumberOutputList(visitorsNumberOutputList);

        visitOutput.setTakeNumber(visitorsNumberOutput.getFetchNumber()+allTakeNumber);
        visitOutput.setCompleteNumber(visitorsNumberOutput.getHandleNumber()+allCompNumber);
        return visitOutput;
    }



}
