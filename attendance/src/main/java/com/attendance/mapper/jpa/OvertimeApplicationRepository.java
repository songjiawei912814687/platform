package com.attendance.mapper.jpa;


import com.attendance.core.base.BaseMapper;
import com.attendance.model.OvertimeApplication;

import java.util.Date;
import java.util.List;

/**
 * @author: Young
 * @description:
 * @date: Created in 23:06 2018/9/11
 * @modified by:
 */
public interface OvertimeApplicationRepository extends BaseMapper<OvertimeApplication,Integer> {

    List<OvertimeApplication> findByEmployeesIdAndOverTimeDateAndAmputatedAndStatusNotLike(Integer employeesId, Date overTimeDate,Integer amputated,Integer status);

    OvertimeApplication findOvertimeApplicationById(Integer id);
}
