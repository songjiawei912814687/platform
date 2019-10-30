package com.personnel.mapper.mybatis;

import com.personnel.core.base.MybatisBaseMapper;
import com.personnel.domian.output.DepartmentNumbersOutput;
import com.personnel.model.DepartmentNumbers;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentNumbersMapper extends MybatisBaseMapper<DepartmentNumbersOutput> {
    int deleteByPrimaryKey(Long id);

    int insert(DepartmentNumbers record);

    int insertSelective(DepartmentNumbers record);

    DepartmentNumbers selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DepartmentNumbers record);

    int updateByPrimaryKey(DepartmentNumbers record);

    void deleteByDate(String s);
}
