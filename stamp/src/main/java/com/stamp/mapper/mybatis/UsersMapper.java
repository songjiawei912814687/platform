package com.stamp.mapper.mybatis;

import com.common.model.PageData;
import com.github.pagehelper.Page;
import com.stamp.core.base.MybatisBaseMapper;
import com.stamp.domain.output.UsersOutput;
import com.stamp.model.Users;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersMapper extends MybatisBaseMapper<UsersOutput> {
    int deleteByPrimaryKey(Long id);

    int insert(Users record);

    int insertSelective(Users record);

    Users selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Users record);

    int updateByPrimaryKey(Users record);

    Page<UsersOutput> findByRoleId(PageData pageData);

    Page<UsersOutput> findByRoleIdNotIn(PageData pageData);

    UsersOutput selectByUserName(@Param("userName") String userName);

    UsersOutput selectByEmployeeId(@Param("employeeId") Integer employeeId);

    int updatePassword(Users users);

    int updatePasswordByUserName(Users users);

    Page<UsersOutput> selectPageListWithinAuthority(PageData pageData);

    Integer selectOrganIdByEmpId(Integer employeeId);

    Integer selectOrganIdByParentId(Integer organId);
}
