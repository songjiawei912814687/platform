package com.message.mapper.jpa;

import com.message.core.base.BaseMapper;
import com.message.model.Materials;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MaterialsRepository extends BaseMapper<Materials,Integer> {

    List<Materials> findAllByMaterialGuIdAndQlInnerCode(String materialGuId,String qlInnerCode);

    @Transactional(rollbackFor = RuntimeException.class)
    int deleteByQlInnerCode(String qlInnerCode);
}
