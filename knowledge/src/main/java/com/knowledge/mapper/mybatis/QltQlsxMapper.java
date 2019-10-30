package com.knowledge.mapper.mybatis;

import com.common.model.PageData;
import com.knowledge.core.base.MybatisBaseMapper;
import com.knowledge.domain.output.MinimumParticleOutput;
import com.knowledge.domain.output.QltQlsxOutput;
import com.knowledge.model.QltQlsx;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QltQlsxMapper  extends MybatisBaseMapper<QltQlsxOutput> {
    int deleteByPrimaryKey(String qlFullId);

    int insert(QltQlsxOutput record);

    int insertSelective(QltQlsxOutput record);

    QltQlsxOutput selectByPrimaryKey(String qlInnerCode);

    int updateByPrimaryKeySelective(QltQlsxOutput record);

    int updateByPrimaryKey(QltQlsx record);

    int selectCount();

    /**查询所有受理机构*/
    List<String> selectAllAccInstitution();

    String selectOuoCode(Integer orgId);

    Integer selectOrgIdByQlId(@Param("qlId") String  qlId);
}
