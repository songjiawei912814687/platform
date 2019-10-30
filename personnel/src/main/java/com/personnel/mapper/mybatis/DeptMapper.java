package com.personnel.mapper.mybatis;

import com.personnel.core.base.MybatisBaseMapper;
import com.personnel.model.Dept;
import org.springframework.stereotype.Repository;

@Repository
public interface DeptMapper extends MybatisBaseMapper<Dept> {
    int deleteByPrimaryKey(Long id);

    int insert(Dept record);

    int insertSelective(Dept record);

    Dept selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Dept record);

    int updateByPrimaryKey(Dept record);
}