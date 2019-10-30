package com.attendance.service;

import com.attendance.core.base.BaseMapper;
import com.attendance.core.base.BaseService;
import com.attendance.core.base.MybatisBaseMapper;
import com.attendance.domian.output.AttendanceRuleConfigOutput;
import com.attendance.mapper.jpa.AttendanceRuleConfigRepository;
import com.attendance.mapper.mybatis.AttendanceRuleConfigMapper;
import com.attendance.mapper.mybatis.OrganizationMapper;
import com.attendance.model.AttendanceRuleConfig;
import com.attendance.model.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author: young
 * @project_name: svn
 * @description:
 * @date: Created in 2019-03-13  09:51
 * @modified by:
 */
@Service
public class AttendanceRuleConfigService extends BaseService<AttendanceRuleConfigOutput, AttendanceRuleConfig,Integer> {

    @Autowired
    private AttendanceRuleConfigRepository attendanceRuleConfigRepository;
    @Autowired
    private AttendanceRuleConfigMapper attendanceRuleConfigMapper;
    @Autowired
    private OrganizationMapper organizationMapper;

    @Override
    public BaseMapper<AttendanceRuleConfig, Integer> getMapper() {
        return attendanceRuleConfigRepository;
    }

    @Override
    public MybatisBaseMapper<AttendanceRuleConfigOutput> getMybatisBaseMapper() {
        return attendanceRuleConfigMapper;
    }

    public List<Organization> getByRuleId(Integer ruleId){
        return organizationMapper.selectOrganizationByRuleId(ruleId);
    }

}
