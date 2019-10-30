package com.selfservice.mapper.mybatis;


import com.common.model.PageData;
import com.selfservice.core.base.MybatisBaseMapper;
import com.selfservice.domain.output.HandlingGuide;
import com.selfservice.domain.output.MinimumParticleOutput;
import com.selfservice.domain.output.QltQlsxOutput;
import com.selfservice.model.MinimumParticle;
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


    List<MinimumParticleOutput> selectByQlId(String qlId);


    int selectCountByQullId(String qlId);
    int selectParentId(String id);

    List<MinimumParticleOutput> findByHappenTypeList(PageData pageData);

    List<MinimumParticleOutput> selectByQlIdAndParentId(PageData pageData);

    List<MinimumParticleOutput> selectByParentId(MinimumParticleOutput minimumParticleOutput1);

    List<MinimumParticleOutput> selectByOrgaNoAndParentId(PageData pageData);

    List<MinimumParticleOutput> selectByHangyeTypeAndParentId(PageData pageData);

    List<MinimumParticleOutput> selectInfoIncludeChildByParentId(Integer id);

    HandlingGuide getMessageDetail(Integer id);

    MinimumParticleOutput selectByPrimaryKeyAndName(PageData pageData);

    List<MinimumParticleOutput> selectState();
}
