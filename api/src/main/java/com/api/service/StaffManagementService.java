package com.api.service;

import com.api.core.base.BaseMapper;
import com.api.core.base.BaseService;
import com.api.core.base.MybatisBaseMapper;
import com.api.domain.output.AttendanceStatistics;
import com.api.domain.output.OrganizationOutput;
import com.api.domain.output.StaffManagementOutput;
import com.api.mapper.mybatis.AttendanceStatisticsMapper;
import com.common.model.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StaffManagementService extends BaseService<AttendanceStatistics, AttendanceStatistics,Integer> {

    @Autowired
    private AttendanceStatisticsMapper attendanceStatisticsMapper;
    @Autowired
    private OrganizationService organizationService;
    @Override
    public BaseMapper<AttendanceStatistics, Integer> getMapper() {
        return null;
    }

    @Override
    public MybatisBaseMapper<AttendanceStatistics> getMybatisBaseMapper() {
        return null;
    }

    public StaffManagementOutput getByOrganizationId(Integer organizationId){
        String date=attendanceStatisticsMapper.selectMaxDate();
        PageData pageData=new PageData();
        pageData.put("date",date);
        if(organizationId!=null&&!organizationId.equals("")){
            pageData.put("path",","+organizationId+",");
        }
        return attendanceStatisticsMapper.selectByDate(pageData);
    }

    public List<StaffManagementOutput> getByOrganization(){
        String date=attendanceStatisticsMapper.selectMaxDate();
        List<StaffManagementOutput> list=new ArrayList<>();
        PageData pageData=new PageData();
        pageData.put("date",date);
        StaffManagementOutput output=new StaffManagementOutput();
        output=attendanceStatisticsMapper.selectByDate(pageData);
        output.setOrganizationName("全部");
        list.add(output);
        List<OrganizationOutput> organizationOutputs=organizationService.getByParentId(0);
        if(organizationOutputs!=null&&organizationOutputs.size()>0){
            for (OrganizationOutput organizationOutput:organizationOutputs){
                pageData.put("path",","+organizationOutput.getId()+",");
                output=attendanceStatisticsMapper.selectByDate(pageData);
                output.setOrganizationName(organizationOutput.getName());
                list.add(output);
            }
        }
        return list;
    }




}
