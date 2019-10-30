package com.knowledge.service;


import com.knowledge.core.base.BaseMapper;
import com.knowledge.core.base.BaseService;
import com.knowledge.core.base.MybatisBaseMapper;
import com.knowledge.domain.output.MaterialsOutput;
import com.knowledge.mapper.jpa.MaterialsRepository;
import com.knowledge.mapper.mybatis.MaterialsMapper;
import com.knowledge.model.Materials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MaterialsService extends BaseService<MaterialsOutput, Materials,Integer> {
    @Autowired
    private MaterialsMapper materialsMapper;

    @Autowired
    private MaterialsRepository materialsRepository;

    @Override
    public BaseMapper<Materials, Integer> getMapper() {
        return materialsRepository;
    }

    @Override
    public MybatisBaseMapper<MaterialsOutput> getMybatisBaseMapper() {
        return materialsMapper;
    }

    public List<Materials> findByGuIdAndQlInnerCode(String guId, String qlInnerCode){
        return materialsRepository.findAllByMaterialGuIdAndQlInnerCode(guId,qlInnerCode);
    }

    public List<Materials> saveAll(List<Materials> list){
        var s = materialsRepository.saveAll(list);
        return s;
    }
}
