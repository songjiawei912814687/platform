package com.knowledge.mapper.jpa;

import com.knowledge.core.base.BaseMapper;
import com.knowledge.model.Answer;
import com.knowledge.model.MaterialList;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterialListRepository extends BaseMapper<MaterialList,Integer>{

    List<MaterialList> findAllByMinimumParticleIdIn(List<Integer> miniPartIdList);


    void deleteByMinimumParticleId(Integer miniId);


    List<MaterialList> findAllByIsMini(Integer isMini);
}
