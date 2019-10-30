package com.stamp.mapper.jpa;



import com.stamp.core.base.BaseRepository;
import com.stamp.model.Employees;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeesRepository extends BaseRepository<Employees, Integer> {
    @Query("select max(employeeNo) from Employees where employeeNo>'10000'")
    String  findmaxno();

    Employees findEmployeesByIdAndAmputated(Integer id,Integer amputated);

    Employees findEmployeesByEmployeeNoAndAmputated(String empNo,Integer amputated);

}
