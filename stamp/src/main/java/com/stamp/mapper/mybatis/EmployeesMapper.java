package com.stamp.mapper.mybatis;

import com.common.model.PageData;
import com.github.pagehelper.Page;
import com.stamp.core.base.MybatisBaseMapper;
import com.stamp.domain.output.EmployeesOutput;
import com.stamp.model.Employees;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeesMapper extends MybatisBaseMapper<EmployeesOutput> {
    int deleteByPrimaryKey(Integer id);

    int insert(Employees record);

    int insertSelective(Employees record);

    EmployeesOutput selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Employees record);

    int updateByPrimaryKey(Employees record);

    Page<EmployeesOutput> selectByPath(PageData t);

    Page<EmployeesOutput> selectByOrgId(PageData pageData);
}
