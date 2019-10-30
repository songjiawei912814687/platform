package com.attendance.mapper.jpa;

import com.attendance.core.base.BaseMapper;
import com.attendance.model.AttendanceRuleConfig;

import java.util.List;

/**
 * @author: young
 * @project_name: svn
 * @description:
 * @date: Created in 2019-03-13  09:49
 * @modified by:
 */
public interface AttendanceRuleConfigRepository extends BaseMapper<AttendanceRuleConfig,Integer> {

    List<AttendanceRuleConfig> findAllByNameAndAmputated(String name,Integer amputated);

    List<AttendanceRuleConfig> findAllByAmputated(Integer amputated);
}
