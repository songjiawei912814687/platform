package com.selfservice.mapper.mybatis;


import com.selfservice.core.base.MybatisBaseMapper;
import com.selfservice.domain.output.MaterialListOutput;
import com.selfservice.model.MaterialList;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterialListMapper extends MybatisBaseMapper<MaterialListOutput> {
    int deleteByPrimaryKey(Integer id);

    int insert(MaterialList record);

    int insertSelective(MaterialList record);

    @Override
    MaterialListOutput selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MaterialList record);

    int updateByPrimaryKey(MaterialList record);

    List<MaterialListOutput> isRepeat(MaterialList materialList);

    List<MaterialListOutput> selectByMinId(Integer miniId);

    List<MaterialListOutput> selectByMiniParticalId(Integer id);
}
