package com.attendance.mapper.jpa;

import com.attendance.core.base.BaseMapper;
import com.attendance.model.AttendanceRule;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceRuleRepository extends BaseMapper<AttendanceRule, Integer> {

    List<AttendanceRule> findByName(String name);

    AttendanceRule getByDescription(String name);

    /**
     * 根据上班的类型type，以及启用状态
     * @param type 上班类型的type 1-上午上班时间 2-下午上班时间 3-上午下班时间 4-下午下班时间
     * @param state 启用状态
     * @return
     */
    List<AttendanceRule> findByTypeAndState(Integer type,Integer state);
}
