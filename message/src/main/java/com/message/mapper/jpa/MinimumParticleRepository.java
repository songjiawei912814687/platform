package com.message.mapper.jpa;

import com.message.core.base.BaseMapper;
import com.message.model.MinimumParticle;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MinimumParticleRepository extends BaseMapper<MinimumParticle,Integer> {

    List<MinimumParticle> findAllByAmputatedAndQlsxIdIn(int amputated, List<String>qlsxId);

    List<MinimumParticle> findAllByAmputated(int amputated);

    List<MinimumParticle> deleteAllByQlsxId(String qlId);


}
