package com.attendance.mapper.jpa;


import com.attendance.core.base.BaseMapper;
import com.attendance.model.LeaveApplication;

import java.util.Date;
import java.util.List;

/**
 * @author: Young
 * @description:
 * @date: Created in 22:40 2018/9/11
 * @modified by:
 */
public interface LeaveApplicationRepository extends BaseMapper<LeaveApplication,Integer> {

    List<LeaveApplication>
    findByEmployeesIdAndStartDateAndEndDateAndStartTimeAndEndTimeAndAmputatedAndStatus(Integer employeesId,
                                                                                           Date startDate,
                                                                                           Date endDate,
                                                                                           String startTime,
                                                                                           String endTime, Integer amputated,
                                                                                            Integer status);
    LeaveApplication findLeaveApplicationById(Integer id);
}
