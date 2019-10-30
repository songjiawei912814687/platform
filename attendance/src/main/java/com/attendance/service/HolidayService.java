package com.attendance.service;

import com.attendance.core.base.BaseMapper;
import com.attendance.core.base.BaseService;
import com.attendance.core.base.MybatisBaseMapper;
import com.attendance.domian.output.HolidayOutput;
import com.attendance.mapper.jpa.HolidayRepository;
import com.attendance.mapper.mybatis.HolidayMapper;
import com.attendance.model.Holiday;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: Young
 * @description:
 * @date: Created in 22:57 2018/9/11
 * @modified by:
 */
@Service("holidayService")
public class HolidayService extends BaseService<HolidayOutput, Holiday,Integer> {

    @Autowired
    private HolidayRepository holidayRepository;

    @Autowired
    private HolidayMapper holidayMapper;

    @Override
    public BaseMapper<Holiday, Integer> getMapper() {
        return holidayRepository;
    }

    @Override
    public MybatisBaseMapper<HolidayOutput> getMybatisBaseMapper() {
        return holidayMapper;
    }
}
