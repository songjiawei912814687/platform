package com.api.mapper.mybatis;

import com.api.domain.output.AppointmentNumberOutput;
import com.api.model.AppointmentNumber;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentNumberMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(AppointmentNumber record);

    int insertSelective(AppointmentNumber record);

    AppointmentNumberOutput selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AppointmentNumber record);

    int updateByPrimaryKey(AppointmentNumber record);

    /***
     * 根据身份证号码和手机号码查询出预约列表
     * @param code
     * @param phone
     * @return
     */
    List<AppointmentNumberOutput> selectByCodeAndPhone(@Param("code") String code,@Param("phone") String phone);

}