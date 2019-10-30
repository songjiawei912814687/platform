package com.knowledge.mapper.jpa;


import com.knowledge.core.base.BaseMapper;
import com.knowledge.model.Materials;

import java.util.List;


public interface MaterialsRepository extends BaseMapper<Materials,Integer> {
    List<Materials> findAllByMaterialGuIdAndQlInnerCode(String materialGuId, String qlInnerCode);


    List<Materials> findAllByQitQlsxIdIn(List<String> qlsxIdList);
}
