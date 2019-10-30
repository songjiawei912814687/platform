package com.feedback.mapper.mybatis;


import com.feedback.core.base.MybatisBaseMapper;
import com.feedback.domain.output.HolidayOutput;
import com.feedback.model.Holiday;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Repository
public interface HolidayMapper extends MybatisBaseMapper<HolidayOutput> {
    int deleteByPrimaryKey(Integer id);

    int insert(Holiday record);

    int insertSelective(Holiday record);

    @Override
    HolidayOutput selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Holiday record);

    int updateByPrimaryKey(Holiday record);

    Integer selectStatusById(Integer id);

    //查询非工作日的天数的开始日期和结束日期
    List<Map> findByIsWorkDay();

    //根据输入的日期判断是否为节假日  条件：非工作日：1  通过状态：3  未删除：0
    List<HolidayOutput> isHoliday(String days);

    //根据输入的日期判断是否为节假日  条件：工作日：3  通过状态：3  未删除：0
    List<HolidayOutput> isWorkDay(String days);

    List<HolidayOutput> holidayAll(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("isWorkday") int isWorkday);


}
