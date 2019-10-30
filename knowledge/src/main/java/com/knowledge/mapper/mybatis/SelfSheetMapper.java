package com.knowledge.mapper.mybatis;

import com.knowledge.core.base.MybatisBaseMapper;
import com.knowledge.domain.output.SelfSheetOutput;
import com.knowledge.model.SelfSheet;
import org.springframework.stereotype.Repository;

@Repository
public interface SelfSheetMapper extends MybatisBaseMapper<SelfSheetOutput> {
    int deleteByPrimaryKey(Long id);

    int insert(SelfSheet record);

    int insertSelective(SelfSheet record);

    SelfSheetOutput selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SelfSheet record);

    int updateByPrimaryKey(SelfSheet record);
}