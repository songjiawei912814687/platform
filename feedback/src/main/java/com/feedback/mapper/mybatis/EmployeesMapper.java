package com.feedback.mapper.mybatis;

import com.common.model.PageData;

import com.feedback.core.base.MybatisBaseMapper;
import com.feedback.domain.output.EmployeesOutput;
import com.feedback.model.Employees;
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

    List<EmployeesOutput> selectAllAndOrgPath();

    /**根据工号查询出姓名和id*/
    EmployeesOutput selectByEmpNo(String empNo);

    /**
     * 查询是否有该员工号
     * @param employeesNo
     * @return
     */
    int selectCountEmployeesNo(String employeesNo);

    /**
     * 根据员工工号查询出员工姓名
     * @param employeesNo
     * @return
     */
    String selectByNo(String employeesNo);


    EmployeesOutput selectByPhone(String phoneNumber);
}
