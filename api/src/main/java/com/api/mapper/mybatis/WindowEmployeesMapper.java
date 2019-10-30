package com.api.mapper.mybatis;

import com.api.core.base.MybatisBaseMapper;
import com.api.domain.output.WindowEmployeesOutput;
import com.api.model.WindowEmployees;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WindowEmployeesMapper extends MybatisBaseMapper<WindowEmployeesOutput>{
    int deleteByPrimaryKey(Integer id);

    int insert(WindowEmployees record);

    int insertSelective(WindowEmployees record);

    @Override
    WindowEmployeesOutput selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WindowEmployees record);

    int updateByPrimaryKey(WindowEmployees record);

    List<WindowEmployeesOutput> selectByEmployeeNo(String  employeeNo);

    List<WindowEmployeesOutput> selectOrganizationId(Integer  organizationId);

    List<WindowEmployeesOutput> selectOrganizationIdList(@Param("organizationIdList") List<Integer> organizationIdList);

    int delete();
}