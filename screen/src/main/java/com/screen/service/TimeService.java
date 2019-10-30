package com.screen.service;

import com.common.Enum.IsWorkDayStatusEnum;
import com.common.utils.HolidayUtils;
import com.screen.domain.output.HolidayOutput;
import com.screen.mapper.mybatis.HolidayMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TimeService {
    @Autowired
    private HolidayMapper holidayMapper;

    public List<String> getWorkDays(Date date,int days) throws Exception{
        //当前时间加工作日，若中间存在节假日，则剔除直到间隔为准确的工作日
        //获得所有的节假日
        //存放holiday的每一天
//        ResponseResult responseResult = ServiceCall.getResult(loadBalancerClient, "/holiday/findAll", "attendance", null);
        List<HolidayOutput> list = holidayMapper.selectAll(null);
//        List<LinkedHashMap> list = (List<LinkedHashMap> )responseResult.getData();
        List<String> holidayList = new ArrayList();
        List<String> workDayList = new ArrayList();
        //遍历holidayList
        if(list!=null&&list.size()>0){
            for(HolidayOutput holidayOutput : list){

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String sstartTime = sdf.format(holidayOutput.getStartDate());
                String sendTime = sdf.format(holidayOutput.getEndDate());

                Date startTime =holidayOutput.getStartDate();
                Date tomorrow = HolidayUtils.getTomorrow(startTime);
                if(!sstartTime.equals(sendTime)){
                    if(Integer.valueOf(holidayOutput.getIsWorkDay().toString()).equals(IsWorkDayStatusEnum.NOT_WORKDAY.getCode())){
                        holidayList.add(sstartTime);
                        holidayList.add(sdf.format(tomorrow));
                    }else{
                        workDayList.add(sstartTime);
                        workDayList.add(sdf.format(tomorrow));
                    }
                    while(!sdf.format(tomorrow).equals(sendTime)){
                        tomorrow = HolidayUtils.getTomorrow(startTime);
                        if(Integer.valueOf(holidayOutput.getIsWorkDay().toString()).equals(IsWorkDayStatusEnum.NOT_WORKDAY.getCode())){
                            holidayList.add(sdf.format(tomorrow));
                        }else{
                            workDayList.add(sdf.format(tomorrow));
                        }
                        startTime = tomorrow;
                    }
                }else{
                    if(Integer.valueOf(holidayOutput.getIsWorkDay().toString()).equals(IsWorkDayStatusEnum.NOT_WORKDAY.getCode())){
                        holidayList.add(sdf.format(startTime));
                    }else{
                        workDayList.add(sdf.format(startTime));
                    }
                }
            }
        }
        date = date==null?new Date():date;
        return  HolidayUtils.getWorkDays(date, holidayList, workDayList, days);
    }
}
