package com.knowledge.mapper.mybatis;

import com.common.model.PageData;
import com.knowledge.core.base.MybatisBaseMapper;
import com.knowledge.domain.output.MaterialListOutput;
import com.knowledge.model.Answer;
import com.knowledge.model.MaterialList;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterialListMapper  extends MybatisBaseMapper<MaterialListOutput> {
    int deleteByPrimaryKey(Long id);

    int insert(MaterialList record);

    int insertSelective(MaterialList record);

    MaterialList selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MaterialList record);

    int updateByPrimaryKey(MaterialList record);

    List<MaterialListOutput> isRepeat(MaterialList materialList);

    List<MaterialListOutput> selectByMinId(Integer miniId);

    List<MaterialListOutput> getParentMaterialLIst(MaterialList materialList);

    List<MaterialListOutput> selectByPrimaryKeyInBatch(@Param(value = "materialList") String materialList);
}
