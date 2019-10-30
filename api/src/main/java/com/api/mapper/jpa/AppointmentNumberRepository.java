package com.api.mapper.jpa;

import com.api.model.AppointmentNumber;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author: young
 * @project_name: svn
 * @description:
 * @date: Created in 2018-12-29  08:57
 * @modified by:
 */
public interface AppointmentNumberRepository extends JpaRepository<AppointmentNumber,Integer> {

    /**
     * 根据身份证号和手机号码查询预约列表
     * @param code 身份证号码
     * @param phone 手机号码
     * @return 预约列表
     */
    List<AppointmentNumber> findAllByCodeAndMobile(String code,String phone);

}
