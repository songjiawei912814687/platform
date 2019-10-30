package com.message.mapper.mybatis;

import com.message.core.base.MybatisBaseMapper;
import com.message.domain.output.MaterialsOutput;
import com.message.model.Materials;
import com.message.model.MaterialsWithBLOBs;
import org.springframework.stereotype.Repository;

@Repository
public interface MaterialsMapper extends MybatisBaseMapper<MaterialsOutput> {
    int deleteByPrimaryKey(Long id);

    int insert(MaterialsWithBLOBs record);

    int insertSelective(MaterialsWithBLOBs record);

    MaterialsWithBLOBs selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MaterialsWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(MaterialsWithBLOBs record);

    int updateByPrimaryKey(Materials record);
}
