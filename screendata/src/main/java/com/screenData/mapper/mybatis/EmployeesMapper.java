package com.screenData.mapper.mybatis;

import com.common.model.PageData;
import com.screenData.core.base.MybatisBaseMapper;
import com.screenData.domain.output.EmployeesOutput;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeesMapper extends MybatisBaseMapper<EmployeesOutput> {
    List<EmployeesOutput>  selectByWindowState(PageData pageData);


    @Override
    EmployeesOutput selectByPrimaryKey(Integer id);

    //根据员工工号查询出员工信息
    EmployeesOutput selectByEmpNo(String empNo);

    EmployeesOutput selectDepartManager(Integer orId);

    List<EmployeesOutput> selectByIdList(@Param("idList") List<Integer> idList);

    //根据组织id查询出员工
    List<EmployeesOutput> selectByOrgId(Integer orId);
}
