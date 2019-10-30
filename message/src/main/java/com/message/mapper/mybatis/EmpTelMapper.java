package com.message.mapper.mybatis;

import com.message.core.base.MybatisBaseMapper;
import com.message.model.EmpTel;
import org.springframework.stereotype.Repository;

@Repository
public interface EmpTelMapper extends MybatisBaseMapper<EmpTel> {

    int deleteByPrimaryKey(Long id);

    int insert(EmpTel record);

    int insertSelective(EmpTel record);

    EmpTel selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(EmpTel record);

    int updateByPrimaryKey(EmpTel record);

}
