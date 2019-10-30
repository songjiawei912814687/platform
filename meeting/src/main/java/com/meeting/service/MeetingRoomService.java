package com.meeting.service;

import com.common.model.PageData;
import com.common.utils.ExportExcel;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.meeting.core.base.BaseMapper;
import com.meeting.core.base.BaseService;
import com.meeting.core.base.MybatisBaseMapper;
import com.meeting.domain.output.MeetingRoomOutput;
import com.meeting.mapper.jpa.MeetingRoomRepository;
import com.meeting.mapper.mybatis.MeetingRoomMapper;
import com.meeting.model.MeetingRoom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Service
public class MeetingRoomService extends BaseService<MeetingRoomOutput, MeetingRoom,Integer> {

    @Autowired
    private MeetingRoomRepository repository;

    @Autowired
    private MeetingRoomMapper meetingRoomMapper;

    @Override
    public BaseMapper<MeetingRoom, Integer> getMapper() {
        return repository;
    }

    @Override
    public MybatisBaseMapper<MeetingRoomOutput> getMybatisBaseMapper() {
        return meetingRoomMapper;
    }

    public List<MeetingRoomOutput> getByName(String name){
        return meetingRoomMapper.selectByName(name);
    }


    public List<MeetingRoomOutput> getByTime(PageData pageData){
        Integer pagesize = pageData.getRows();
        Integer page = pageData.getPageIndex();
        PageHelper.startPage(page, pagesize);
        Page<MeetingRoomOutput> pageList = meetingRoomMapper.selectByTime(pageData);
        return pageList;
    }

    public List<MeetingRoomOutput> getByApply(PageData pageData){
        if(getUsers().getAdministratorLevel()!=9){
            pageData.put("orgId",getUsers().getOrganizationId());
        }
        List<MeetingRoomOutput> pageList = getMybatisBaseMapper().selectPage(pageData);
        return pageList;
    }

    public List<MeetingRoomOutput> selectAll(boolean isPage, PageData pageData){
        if(isPage){
            Integer pagesize = pageData.getRows();
            Integer page = pageData.getPageIndex();
            PageHelper.startPage(page, pagesize);

            if(getUsers().getAdministratorLevel()!=9){
                pageData.put("orgId",getUsers().getOrganizationId());
            }
            Page<MeetingRoomOutput> pageList = getMybatisBaseMapper().selectPage(pageData);
            return pageList;
        }
        return getMybatisBaseMapper().selectAll(pageData);
    }


    public String export(HttpServletResponse response, HttpServletRequest request) throws Exception {
        String title = "会议室";
        String excelName = "会议室";
        String[] rowsName = new String[]{"序号","会议室名字","可容纳人数","描述"};
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        PageData pageData=new PageData(request);
        List<MeetingRoomOutput> list=selectAll(false,pageData);
        if(list.size()>0){
            int i=1;
            for(MeetingRoomOutput room:list){
                objs = new Object[rowsName.length];
                objs[0]=i;
                objs[1]=room.getName();
                objs[2]=room.getContainNumber();
                objs[3]=room.getDescription();
                dataList.add(objs);
                i++;
            }
        }
        ExportExcel ex = new ExportExcel(title, rowsName, dataList, excelName);
        return ex.export(response, request);
    }


    public List<MeetingRoomOutput> getByIp(String ip){
        return meetingRoomMapper.selectByIp(ip);
    }

}
