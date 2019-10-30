package com.personnel.mapper.mybatis;

import com.personnel.core.base.MybatisBaseMapper;
import com.personnel.domian.output.DimissionOutput;
import com.personnel.model.Dimission;
import org.springframework.stereotype.Repository;

@Repository
public interface DimissionMapper extends MybatisBaseMapper<DimissionOutput> {
    int deleteByPrimaryKey(Integer id);

    int insert(Dimission record);

    int insertSelective(Dimission record);

    @Override
    DimissionOutput selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Dimission record);

    DimissionOutput selectByEmployeeId(Integer id);
}