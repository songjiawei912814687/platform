package com.api.service;

import com.api.domain.output.MeetingApply;
import com.api.domain.output.MeetingRoom;
import com.api.mapper.mybatis.MeetingApplyMapper;
import com.api.mapper.mybatis.MeetingRoomMapper;
import com.common.model.PageData;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class MeetingService {
    @Autowired
    private MeetingApplyMapper meetingApplyMapper;

    @Autowired
    private MeetingRoomMapper meetingRoomMapper;

    public MeetingRoom getByIp(String ip){
        return meetingRoomMapper.selectByIp(ip);
    }

    public MeetingApply getByTimeAndIp(String ip){
        PageData pageData=new PageData();
        MeetingRoom meetingRoom=meetingRoomMapper.selectByIp(ip);
        Date date=new Date();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        String tp = simpleDateFormat.format(date);
        pageData.put("date",tp);
        if(meetingRoom!=null){
            pageData.put("meetingRoomId",meetingRoom.getId()+"");
        }else {
            pageData.put("meetingRoomId","-1");
        }
        Calendar c = Calendar.getInstance();
        double hour = c.get(Calendar.HOUR_OF_DAY);
        int min=c.get(Calendar.MINUTE);
        if(min>=30){
            hour=hour+1;
        }else{
            hour=hour+0.5;
        }
        pageData.put("startDateTime",""+((int)(2*hour-16)));
        pageData.put("endDateTime",""+((int)(2*hour-17)));
        List<MeetingApply> meetingApplyList=meetingApplyMapper.selectPage(pageData);
        if(meetingApplyList!=null&&meetingApplyList.size()>0){
            return meetingApplyList.get(0);
        }
        return null;
    }

    public MeetingApply getNextByIp(String ip){
       PageData pageData=new PageData();
        MeetingRoom meetingRoom=getByIp(ip);
        Date date=new Date();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        String tp = simpleDateFormat.format(date);
        pageData.put("date",tp);
        if(meetingRoom!=null){
            pageData.put("meetingRoomId",meetingRoom.getId()+"");
        }else {
            pageData.put("meetingRoomId","-1");
        }
        Calendar c = Calendar.getInstance();
        double hour = c.get(Calendar.HOUR_OF_DAY);
        int min=c.get(Calendar.MINUTE);
        if(min>=30){
            hour=hour+1;
        }else{
            hour=hour+0.5;
        }
        pageData.put("startDateTime",""+((int)(2*hour-17)));
        List<MeetingApply> meetingApplyList=meetingApplyMapper.selectNextMeeting(pageData);
        if(meetingApplyList!=null&&meetingApplyList.size()>0){
            return meetingApplyList.get(0);
        }
        return null;
    }


}
