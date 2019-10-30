package com.knowledge.service;

import com.common.model.PageData;
import com.common.response.ResponseResult;
import com.common.utils.HttpRequestUtil;
import com.knowledge.core.base.BaseMapper;
import com.knowledge.core.base.BaseService;
import com.knowledge.core.base.MybatisBaseMapper;
import com.knowledge.domain.output.MaterialListOutput;
import com.knowledge.domain.output.MinimumParticleOutput;
import com.knowledge.mapper.jpa.MaterialListRepository;
import com.knowledge.mapper.jpa.MaterialsRepository;
import com.knowledge.mapper.jpa.MinimumParticleRepository;
import com.knowledge.mapper.mybatis.MaterialListMapper;
import com.knowledge.mapper.mybatis.MaterialsMapper;
import com.knowledge.mapper.mybatis.MinimumParticleMapper;
import com.knowledge.model.MaterialList;
import com.knowledge.model.Materials;
import com.knowledge.model.MaterialsWithBLOBs;
import com.knowledge.model.MinimumParticle;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MaterialListService extends BaseService<MaterialListOutput, MaterialList,Integer>{

    @Autowired
    private MaterialListRepository repository;
    @Autowired
    private MaterialListMapper materialListMapper;
    @Autowired
    private MinimumParticleMapper minimumParticleMapper;
    @Autowired
    private MinimumParticleRepository minimumParticleRepository;
    @Autowired
    private MaterialsRepository materialsRepository;
    @Autowired
    private MaterialsMapper materialsMapper;

    @Override
    public BaseMapper<MaterialList, Integer> getMapper() {
        return repository;
    }

    @Override
    public MybatisBaseMapper<MaterialListOutput> getMybatisBaseMapper() {
        return materialListMapper;
    }

    public boolean isRepeat(MaterialList materialList) {
        materialList.setAmputated(0);
        List<MaterialListOutput> list = materialListMapper.isRepeat(materialList);
        if(list!=null&&list.size()>0){
            return true;
        }
        return  false;
    }

    public List<MaterialListOutput> getParentMaterialLIst(PageData pageData) {
        Integer minimumParticleId = Integer.valueOf(pageData.getString("minimumParticleId"));
        MinimumParticleOutput minimumParticleOutput = minimumParticleMapper.selectByPrimaryKey(minimumParticleId);
        MaterialList materialList = new MaterialList();
        materialList.setName(pageData.getString("name"));
        materialList.setMinimumParticleId(minimumParticleOutput.getParentId());
        List<MaterialListOutput> list = materialListMapper.getParentMaterialLIst(materialList);
        List<MaterialListOutput> materialListOutputs = materialListMapper.selectByMinId(minimumParticleId);
        List<MaterialListOutput> returnList  = new ArrayList<>();
        for (MaterialListOutput MaterialListOutput:list) {
            boolean flag  =true;
            for (MaterialListOutput current:materialListOutputs ) {
                if(current.getName().equals(MaterialListOutput.getName())){
                    flag = false;
                    break;
                }
            }
            if(flag){
                returnList.add(MaterialListOutput);
            }
        }
        return returnList;
    }

    public ResponseResult addInBatch(Integer id, String materialList) {
        List<MaterialListOutput> list = materialListMapper.selectByPrimaryKeyInBatch(materialList);
        ArrayList<MaterialList> materialLists = new ArrayList<>();
        for (MaterialListOutput materialListOutput:list ) {
            MaterialList materialList1 = new MaterialList();
            BeanUtils.copyProperties(materialListOutput,materialList1);
            materialList1.setMinimumParticleId(id);
            materialList1.setId(null);
            materialLists.add(materialList1);
        }
        List<MaterialList> materialLists1 = repository.saveAll(materialLists);
        if(materialLists1!=null&&materialLists1.size()>0){
            return  ResponseResult.success("批量添加材料清单成功");
        }else{
            return  ResponseResult.success("批量添加材料清单失败");
        }
    }


    @Transactional(rollbackFor = Exception.class)
    public void updatematerList() {
        List<MaterialList> materialListList = repository.findAllByIsMini(0);
        if(CollectionUtils.isEmpty(materialListList)){
            return;
        }
        List<Integer> miniIdList = materialListList.stream()
                .map(MaterialList::getMinimumParticleId)
                .collect(Collectors.toList());

        List<MinimumParticle> minimumParticleList = minimumParticleRepository.findAllByIdIn(miniIdList);
        HashMap<String, String> minimumParticleMap = new HashMap<>();
        for (MinimumParticle material:minimumParticleList) {

            minimumParticleMap.put(material.getQlsxId(),material.getId()+"-"+(material.getOrgId()==null?"000":material.getOrgId().toString()));
        }
        List<String> qlInnerCodeList = minimumParticleList.stream().map(MinimumParticle::getQlInnerCode).collect(Collectors.toList());
        List<MaterialsWithBLOBs> materialsOutputs = materialsMapper.selectByQlInnerCodeList(qlInnerCodeList);
        if(CollectionUtils.isEmpty(materialsOutputs)){
            return;
        }
        List<MaterialList> list = new ArrayList<>();
        for(MaterialsWithBLOBs materialsItem : materialsOutputs){
            if (materialsItem.getEmptyTableFileName() != null && !"".equals(materialsItem.getEmptyTableFileName()) && materialsItem.getEmptyTableFileUrl() != null && !"".equals(materialsItem.getEmptyTableFileUrl()) && materialsItem.getEmptyTableFileName().indexOf(".doc") >= 0 && materialsItem.getEmptyTableFileUrl().indexOf("http") >= 0) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("url", materialsItem.getEmptyTableFileUrl());
                    map.put("fileName", materialsItem.getEmptyTableFileName());
                        ResponseResult result = HttpRequestUtil.sendPostRequest("http://10.32.250.84:8762/changeToPdf", map);
//                        ResponseResult result = HttpRequestUtil.sendPostRequest("http://118.31.3.79:8762/changeToPdf", map);
//                    ResponseResult result = HttpRequestUtil.sendPostRequest("http://192.168.2.130:8762/changeToPdf", map);
                    if (result.isSuccess()) {
                        materialsItem.setEmptyTableFileUrl(result.getData().toString());
                        if (materialsItem.getEmptyTableFileName().indexOf(".docx") >= 0) {
                            materialsItem.setEmptyTableFileName(materialsItem.getEmptyTableFileName().replace(".docx", "pdf"));
                        } else {
                            materialsItem.setEmptyTableFileName(materialsItem.getEmptyTableFileName().replace(".doc", "pdf"));
                        }
                    }
                }
                if (materialsItem.getExampleTableFileName() != null && !"".equals(materialsItem.getExampleTableFileName()) && materialsItem.getExampleTableFileUrl() != null && !"".equals(materialsItem.getExampleTableFileUrl()) && materialsItem.getExampleTableFileName().indexOf(".doc") >= 0 && materialsItem.getExampleTableFileUrl().indexOf("http") >= 0) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("url", materialsItem.getExampleTableFileUrl());
                    map.put("fileName", materialsItem.getExampleTableFileName());
                        ResponseResult result = HttpRequestUtil.sendPostRequest("http://10.32.250.84:8762/changeToPdf", map);
//                        ResponseResult result = HttpRequestUtil.sendPostRequest("http://118.31.3.79:8762/changeToPdf", map);
//                    ResponseResult result = HttpRequestUtil.sendPostRequest("http://192.168.2.130:8762/changeToPdf", map);
                    if (result.isSuccess()) {
                        materialsItem.setExampleTableFileUrl(result.getData().toString());
                        if (materialsItem.getExampleTableFileName().indexOf(".docx") >= 0) {
                            materialsItem.setExampleTableFileName(materialsItem.getExampleTableFileName().replace(".docx", "pdf"));
                        } else {
                            materialsItem.setExampleTableFileName(materialsItem.getExampleTableFileName().replace(".doc", "pdf"));
                        }
                    }
            }
            MaterialList materialList = new MaterialList();
            BeanUtils.copyProperties(materialsItem,materialList);
            materialList.setAmputated(0);
            materialList.setIsMini(0);
            materialList.setCreatedDateTime(new Date());
            materialList.setLastUpdateDateTime(new Date());
            materialList.setCreatedUserId(0);
            materialList.setLastUpdateUserId(0);
            materialList.setLastUpdateUserName("SYS");
            materialList.setCreatedUserName("SYS");
            materialList.setMaterialForm(materialsItem.getFormat());
            materialList.setMinimumParticleId(Integer.valueOf(minimumParticleMap.get(materialsItem.getQitQlsxId()).split("-")[0]));
            if(!minimumParticleMap.get(materialsItem.getQitQlsxId()).split("-")[1].equals("000")){
                materialList.setOrgId(Integer.valueOf(minimumParticleMap.get(materialsItem.getQitQlsxId()).split("-")[1]));
            }
            list.add(materialList);
        }
        List<MaterialList> materialLists = repository.saveAll(list);
        if(materialLists!=null&&materialLists.size()>0){
            //删除之前所有的材料清单
            repository.deleteAll(materialListList);
            for (MaterialList material: materialListList) {
                String path = (material.getEmptyTableFileUrl()==null||material.getEmptyTableFileUrl()=="")?material.getEmptyTableFileUrl():material.getExampleTableFileUrl();
                HttpRequestUtil.sendPostRequest("http://10.32.250.84:8762/deleteFile?destFilePath="+path, null);
            }
        }
    }
}
