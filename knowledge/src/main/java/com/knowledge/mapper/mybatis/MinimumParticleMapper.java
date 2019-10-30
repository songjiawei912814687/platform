package com.knowledge.mapper.mybatis;

import com.knowledge.core.base.MybatisBaseMapper;
import com.knowledge.domain.output.MinimumParticleOutput;
import com.knowledge.model.Answer;
import com.knowledge.model.MinimumParticle;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MinimumParticleMapper extends MybatisBaseMapper<MinimumParticleOutput> {
    int deleteByPrimaryKey(Long id);

    int insert(MinimumParticle record);

    int insertSelective(MinimumParticle record);

    MinimumParticle selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MinimumParticle record);

    int updateByPrimaryKey(MinimumParticle record);

    List<MinimumParticleOutput> isRepeat(MinimumParticle minimumParticle);

    MinimumParticleOutput selectByQlId(String qlId);

    int selectCountByQullId(String qlId);

    List<MinimumParticle> findByQlsxIdAndParentId(String id);

    List<MinimumParticleOutput> getByQlNameAndHappeningType(MinimumParticleOutput minimumParticleOutput);

    List<MinimumParticleOutput> getByPath(@Param(value = "path") String path);

    List<MinimumParticleOutput> getSonEmlemrnt(Integer id);

    List<MinimumParticleOutput> selectParList(@Param("orgId") Integer orgId);
}
