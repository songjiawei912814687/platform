package com.personnel.mapper.jpa;

import com.personnel.core.base.BaseMapper;
import com.personnel.model.Employees;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeesRepository extends BaseMapper<Employees, Integer> {
    @Query("select max(employeeNo) from Employees where employeeNo>'10000'")
    String  findmaxno();


    Employees findEmployeesById(Integer id);


    Employees findEmployeesByEmployeeNo(String empNo);

}
