package com.meeting.mapper.jpa;

import com.meeting.core.base.BaseMapper;
import com.meeting.model.Employees;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeesRepository extends BaseMapper<Employees, Integer> {
    @Query("select max(employeeNo) from Employees where employeeNo>'10000'")
    String  findmaxno();


    Employees findEmployeesById(Integer id);


    Employees findEmployeesByEmployeeNo(String empNo);

}
