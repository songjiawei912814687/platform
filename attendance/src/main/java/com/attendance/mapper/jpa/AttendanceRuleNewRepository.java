package com.attendance.mapper.jpa;

import com.attendance.core.base.BaseMapper;
import com.attendance.model.AttendanceRuleNew;

import java.util.List;

/**
 * @author: young
 * @project_name: svn
 * @description:
 * @date: Created in 2019-03-13  09:50
 * @modified by:
 */
public interface AttendanceRuleNewRepository extends BaseMapper<AttendanceRuleNew,Integer> {

    List<AttendanceRuleNew> findAllByNameAndAmputated(String name,Integer amputated);

    List<AttendanceRuleNew> findAllByAttendanceRuleConfigIdAndAmputatedAndState(Integer attendanceRuleConfigId,Integer amputated,Integer state);

    AttendanceRuleNew findAllByAttendanceRuleConfigIdAndAmputatedAndTypeAndState(Integer attendanceRuleConfigId,Integer amputated,Integer type,Integer state);
}
