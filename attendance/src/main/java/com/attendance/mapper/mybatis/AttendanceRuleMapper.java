package com.attendance.mapper.mybatis;

import com.attendance.core.base.MybatisBaseMapper;
import com.attendance.domian.output.AttendanceRuleOutput;
import com.attendance.model.AttendanceRule;
import com.common.model.PageData;
import org.apache.ibatis.annotations.Param;
import org.apache.xmlbeans.impl.values.XmlIntegerRestriction;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceRuleMapper extends MybatisBaseMapper<AttendanceRuleOutput> {
    int deleteByPrimaryKey(Integer id);

    int insert(AttendanceRule record);

    int insertSelective(AttendanceRule record);

    @Override
    AttendanceRuleOutput selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AttendanceRule record);

    int updateByPrimaryKey(AttendanceRule record);

    String selectNameByValue(String dateTime);

    List<AttendanceRuleOutput> selectInUse();

    List<AttendanceRuleOutput> selectInUseByUser(PageData pageData);
}
