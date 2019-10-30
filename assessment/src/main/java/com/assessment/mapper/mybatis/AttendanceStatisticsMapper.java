package com.assessment.mapper.mybatis;

import com.assessment.core.base.MybatisBaseMapper;
import com.assessment.model.AttendanceStatistics;
import com.common.model.PageData;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceStatisticsMapper extends MybatisBaseMapper<AttendanceStatistics>{
    int deleteByPrimaryKey(Integer id);

    int insert(AttendanceStatistics record);

    int insertSelective(AttendanceStatistics record);

    @Override
    AttendanceStatistics selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AttendanceStatistics record);

    int updateByPrimaryKey(AttendanceStatistics record);

    List<AttendanceStatistics> selectByDate(String  date);
}