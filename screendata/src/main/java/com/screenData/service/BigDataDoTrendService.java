package com.screenData.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.common.response.ResponseResult;
import com.google.common.collect.Lists;
import com.screenData.config.RedisComponent;
import com.screenData.domain.output.BigDataDoTrendOutput;
import com.screenData.mapper.mybatis.AssessmentDepartReportMapper;
import com.screenData.mapper.mybatis.DepartmentNumbersMapper;
import com.screenData.mapper.mybatis.OrganizationMapper;
import com.screenData.model.Config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: Young
 * @description: 办件趋势
 * @date: Created in 21:58 2018/10/30
 * @modified by:
 */
@Service
@Slf4j
public class BigDataDoTrendService {

    @Autowired
    private OrganizationMapper organizationMapper;
    @Autowired
    private AssessmentDepartReportMapper assessmentDepartReportMapper;
    @Autowired
    private DepartmentNumbersMapper departmentNumbersMapper;
    @Autowired
    private ConfigService configService;
    @Autowired
    private RedisComponent redisComponent;

    public ResponseResult findDoTrend(){

        //计算出当前的年份
        Integer year = Calendar.getInstance().get(Calendar.YEAR);
        //计算出当前的月份
        Integer month = Calendar.getInstance().get(Calendar.MONTH)+1;

        Object obj = redisComponent.getHash("bigDataDoTrendOutputs","bigDataDoTrendOutputs");
        if(obj!=null){
            return ResponseResult.success(JSONObject.parseArray(obj.toString(), BigDataDoTrendOutput.class));
        }

        try {
            List<Config> configList = configService.getListByParentId(111);

            List<String> valueList = configList.stream()
                    .map(Config::getConfigValue)
                    .collect(Collectors.toList());

            List<Integer> idList = Lists.newArrayList();
            for(String value:valueList){
                idList.add(Integer.valueOf(value));
            }

            List<BigDataDoTrendOutput> bigDataDoTrendOutputs = Lists.newArrayList();

            for(Integer childId:idList){
                //查询出子id集合
                List<Integer> childIdList = organizationMapper.selectChildById(childId);
                //如果月份大于10月小于12月
                if(month>10) {
                    for (Integer i=month-10;i<month ;i++) {
                        bigDataDoTrendOutputs.add(assemmble(childIdList,childId,i,year));
                    }
                }
                //如果小于10月份先查出当前年份的数据
                else {
                    year = Calendar.getInstance().get(Calendar.YEAR)-1;
                    Integer startMonth = month+2;
                    for(int i = startMonth;i<=12;i++){
                        bigDataDoTrendOutputs.add(assemmble(childIdList,childId,i,year));
                    }
                    year = Calendar.getInstance().get(Calendar.YEAR);
                    for(int i=1;i<month;i++){
                        //如果当前是1月份不添加今年的数据
                        if(month == 1){
                            continue;
                        }
                        bigDataDoTrendOutputs.add(assemmble(childIdList,childId,i,year));
                    }
                }
            }
            redisComponent.setHash("bigDataDoTrendOutputs","bigDataDoTrendOutputs", JSONArray.toJSON(bigDataDoTrendOutputs).toString(),8*60*60L);

            return ResponseResult.success(bigDataDoTrendOutputs);

        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseResult.success();
        }
    }


    /**
     *
     * @param childIdList
     * @param childId
     * @param i
     * @param year
     * @return
     */
    public BigDataDoTrendOutput assemmble(List<Integer> childIdList,Integer childId,Integer i,Integer year){
        Integer totalAmount = 0;
        BigDataDoTrendOutput bigDataDoTrendOutput = null;
        for (Integer id : childIdList) {
            //该月该组织人均总办件量
            Integer avageAmount = assessmentDepartReportMapper.selectDoingThingById(id,year,i)==null?
                    0:assessmentDepartReportMapper.selectDoingThingById(id,year,i);
            //该月该组织总人数
            Integer peronNumber = departmentNumbersMapper.selectNumberById(id,year+"-"+i)==null?
                    0:departmentNumbersMapper.selectNumberById(id,year+"-"+i);

            totalAmount = avageAmount*peronNumber+totalAmount;
            bigDataDoTrendOutput = new BigDataDoTrendOutput();
            bigDataDoTrendOutput.setOrganizationId(childId);
            bigDataDoTrendOutput.setYearAndMonth(year+"-"+i);
            bigDataDoTrendOutput.setTotalAmount(totalAmount);
        }
        return bigDataDoTrendOutput;
    }
}

