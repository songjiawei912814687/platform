package com.attendance.mapper.mybatis;

import com.attendance.core.base.MybatisBaseMapper;
import com.attendance.domian.output.AttendanceRuleNewOutput;
import com.attendance.model.AttendanceRuleNew;
import com.common.model.PageData;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceRuleNewMapper extends MybatisBaseMapper<AttendanceRuleNewOutput> {
    int deleteByPrimaryKey(Integer id);

    int insert(AttendanceRuleNew record);

    int insertSelective(AttendanceRuleNew record);

    AttendanceRuleNewOutput selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AttendanceRuleNew record);

    int updateByPrimaryKey(AttendanceRuleNew record);


    List<AttendanceRuleNewOutput> selectInUse();

    List<AttendanceRuleNewOutput> findByTypeAndAttendanceRuleConfigId(PageData pageData);

    List<AttendanceRuleNewOutput> selectInUseByuser(PageData pageData);

    Integer selectTypeByValueAndEmployeeId(PageData pageData);

    String findByTypeAndEmployeeId(PageData pageData);


    List<String> selectNameByValue(String dateTime);
}
