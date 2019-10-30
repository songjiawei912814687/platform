package com.api.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.api.config.RedisComponent;
import com.api.domain.output.BigDataDoTrendOutput;
import com.api.domain.output.BigDataInvokeOutput;
import com.api.mapper.mybatis.HchenStatisticalMapper;
import com.common.response.ResponseResult;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;

/**
 * @author: Young
 * @description: 大数据调用量
 * @date: Created in 18:14 2018/10/30
 * @modified by:
 */
@Service
public class BigDataInvokeService {

    @Autowired
    private HchenStatisticalMapper hchenStatisticalMapper;
    @Autowired
    private RedisComponent redisComponent;

    public ResponseResult findBigData()  {

        Object obj = redisComponent.getHash("bigDataInvokeOutputList","bigDataInvokeOutputList");
        if(obj!=null){
            return ResponseResult.success(JSONObject.parseArray(obj.toString(), BigDataInvokeOutput.class));
        }

        //获取当前年份
        Integer year = Calendar.getInstance().get(Calendar.YEAR);
        //获取当前月份
        Integer month = Calendar.getInstance().get(Calendar.MONTH)+1;
        List<BigDataInvokeOutput> bigDataInvokeOutputList = Lists.newArrayList();
        //如果当前月份大于10月
        if(month-10>0){
            for(int i=month-10;i<month ;i++){
                //从鸿程中拉取的数据是拉取i+1月份的数据作为当月数据
                Integer count = hchenStatisticalMapper.selectByDate(year,i+1);
                BigDataInvokeOutput bigDataInvokeOutput = new BigDataInvokeOutput();
                bigDataInvokeOutput.setDate(year+"-"+i);
                bigDataInvokeOutput.setCount(count);
                bigDataInvokeOutputList.add(bigDataInvokeOutput);
            }
            return ResponseResult.success(bigDataInvokeOutputList);
        }else {
            //小于10月，查询出两部分数据，
            //查询上年待补的数据
            year = year-1;
            int startMonth = month+2;
            for(int i=startMonth;i<=12;i++){
                //如果是上年12月份时
                if(i==12){
                    //重新设置年份+1
                    year = year+1;
                    //月份设置成1
                    i =1;
                    //查询1月份的数据
                    Integer count = hchenStatisticalMapper.selectByDate(year,1);
                    BigDataInvokeOutput bigDataInvokeOutput = new BigDataInvokeOutput();
                    //设置成时间为上年12月份
                    bigDataInvokeOutput.setDate((year-1)+"-"+12);
                    bigDataInvokeOutput.setCount(count);
                    bigDataInvokeOutputList.add(bigDataInvokeOutput);
                    break;
                }
                //如果是小于12月份设置查询是上年
                Integer count = hchenStatisticalMapper.selectByDate(year,i+1);
                BigDataInvokeOutput bigDataInvokeOutput = new BigDataInvokeOutput();
                bigDataInvokeOutput.setDate(year+"-"+i);
                bigDataInvokeOutput.setCount(count);
                bigDataInvokeOutputList.add(bigDataInvokeOutput);
            }

            // 查出当前年份的数据
            year = Calendar.getInstance().get(Calendar.YEAR);
            for (int i=1;i<month;i++) {
                //如果当前是1月份不添加今年的数据
                if(month == 1){
                    continue;
                }else {
                    //从鸿程中拉取的数据是拉取i+1月份的数据作为当月数据
                    Integer count = hchenStatisticalMapper.selectByDate(year,i+1);
                    BigDataInvokeOutput bigDataInvokeOutput = new BigDataInvokeOutput();
                    bigDataInvokeOutput.setDate(year+"-"+i);
                    bigDataInvokeOutput.setCount(count);
                    bigDataInvokeOutputList.add(bigDataInvokeOutput);
                }
            }
            redisComponent.setHash("bigDataInvokeOutputList","bigDataInvokeOutputList", JSONArray.toJSON(bigDataInvokeOutputList).toString(),8*60*60L);
            return ResponseResult.success(bigDataInvokeOutputList);
        }
    }
}
