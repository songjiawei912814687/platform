package com.selfservice.mapper.mybatis;

import com.selfservice.domain.output.MaterialsOutput;
import com.selfservice.model.Materials;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterialsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Materials record);

    int insertSelective(Materials record);

    MaterialsOutput selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Materials record);

    int updateByPrimaryKey(Materials record);

    List<MaterialsOutput> selectByQlId(String qlId);

}