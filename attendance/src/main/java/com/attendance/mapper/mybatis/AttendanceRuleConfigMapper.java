package com.attendance.mapper.mybatis;

import com.attendance.core.base.MybatisBaseMapper;
import com.attendance.domian.output.AttendanceRuleConfigOutput;
import com.attendance.model.AttendanceRuleConfig;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceRuleConfigMapper extends MybatisBaseMapper<AttendanceRuleConfigOutput> {
    int deleteByPrimaryKey(Integer id);

    int insert(AttendanceRuleConfig record);

    int insertSelective(AttendanceRuleConfig record);

    AttendanceRuleConfigOutput selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AttendanceRuleConfig record);

    int updateByPrimaryKey(AttendanceRuleConfig record);
}