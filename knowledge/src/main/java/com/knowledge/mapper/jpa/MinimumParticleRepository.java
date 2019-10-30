package com.knowledge.mapper.jpa;

import com.knowledge.core.base.BaseMapper;
import com.knowledge.model.MaterialList;
import com.knowledge.model.MinimumParticle;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MinimumParticleRepository extends BaseMapper<MinimumParticle,Integer>{

    List<MinimumParticle> findByParentIdAndAmputated(Integer id, int i);

    List<MinimumParticle> findByParentId(Integer id);

    List<MinimumParticle> findByQlInnerCodeAndAmputated(String qlInnerCode,Integer amputated);

    List<MinimumParticle> findByQlInnerCodeAndAmputatedAndParentId(String qlInnerCode,Integer amputated,Integer parentId);


    List<MinimumParticle> findAllByIdIn(List<Integer> miniIdList);
}
