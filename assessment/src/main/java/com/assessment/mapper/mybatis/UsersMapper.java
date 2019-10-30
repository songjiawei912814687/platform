package com.assessment.mapper.mybatis;

import com.assessment.core.base.MybatisBaseMapper;
import com.assessment.domian.output.UsersOutput;
import com.common.model.PageData;
import com.github.pagehelper.Page;
import com.assessment.model.Users;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersMapper extends MybatisBaseMapper<UsersOutput> {
    int deleteByPrimaryKey(Integer id);

    int insert(Users record);

    int insertSelective(Users record);

    @Override
    UsersOutput selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Users record);

    int updateByPrimaryKey(Users record);

    Page<UsersOutput> findByRoleId(@Param(value = "roleId") Integer roleId);

    Page<UsersOutput> findByRoleIdNotIn(@Param(value = "roleId") Integer roleId);


    List<UsersOutput> findByOrganAndJob(PageData pageData);

    int updateEmployeeState(PageData pageData);

    int updateByEmployeeId(@Param(value = "employeeId") Integer employeeId);

    UsersOutput selectByUserName( String userName);

    UsersOutput selectByEmployeeId(@Param("employeeId") Integer employeeId);
}
