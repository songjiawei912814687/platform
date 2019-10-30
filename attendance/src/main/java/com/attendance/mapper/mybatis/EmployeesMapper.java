package com.attendance.mapper.mybatis;

import com.attendance.core.base.MybatisBaseMapper;
import com.attendance.domian.output.EmployeesOutput;
import com.attendance.model.Employees;
import com.common.model.PageData;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeesMapper extends MybatisBaseMapper<EmployeesOutput> {
    int deleteByPrimaryKey(Integer id);

    int insert(Employees record);

    int insertSelective(Employees record);

    @Override
    EmployeesOutput selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Employees record);

    int updateByPrimaryKey(Employees record);

    int selectCountName(String name);

    Integer selectEmployeeIdByEmployeeName(String name);

     Page<EmployeesOutput> selectByPath(PageData t);

    String  selectMaxNo();

    List<EmployeesOutput> selectAttendanceAll();


    EmployeesOutput selectByEmployeesNo(String employeesNo);

    int selectOrganizationByName(String name);

    Integer selectOrgId(Integer empId);
}
