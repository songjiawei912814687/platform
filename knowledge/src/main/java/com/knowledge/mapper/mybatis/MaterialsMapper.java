package com.knowledge.mapper.mybatis;


import com.knowledge.core.base.MybatisBaseMapper;
import com.knowledge.domain.output.MaterialsOutput;
import com.knowledge.model.Materials;
import com.knowledge.model.MaterialsWithBLOBs;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface MaterialsMapper extends MybatisBaseMapper<MaterialsOutput> {
    int deleteByPrimaryKey(Integer id);

    int insert(Materials record);

    int insertSelective(Materials record);

    @Override
    MaterialsOutput selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Materials record);

    int updateByPrimaryKey(Materials record);

    List<MaterialsWithBLOBs> selectByQlInnerCode(String qlInnerCode);

    List<MaterialsWithBLOBs> selectByQlInnerCodeList(@Param("qlInnerCodeList") List<String> qlInnerCodeList);
}
