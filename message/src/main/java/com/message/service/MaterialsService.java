package com.message.service;

import com.message.core.base.BaseMapper;
import com.message.core.base.BaseService;
import com.message.core.base.MybatisBaseMapper;
import com.message.domain.output.MaterialsOutput;
import com.message.mapper.jpa.MaterialsRepository;
import com.message.mapper.mybatis.MaterialsMapper;
import com.message.model.Materials;
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

    public List<Materials> findByGuIdAndQitId(String guId,String qlInnerCode){
        return materialsRepository.findAllByMaterialGuIdAndQlInnerCode(guId,qlInnerCode);
    }

    public List<Materials> saveAll(List<Materials> list){
        var s = materialsRepository.saveAll(list);
        return s;
    }

    public void deleteByQlInnerCode(String qlInnerCode){
        var s = materialsRepository.deleteByQlInnerCode(qlInnerCode);
    }
}
