//package com.knowledge.core.Thread;
//
//import com.alibaba.fastjson.JSONObject;
//import com.common.utils.JsonUtils;
//import com.common.utils.Valid;
//import com.knowledge.mapper.jpa.QltQlsxRepository;
//import com.knowledge.model.Materials;
//import com.knowledge.model.QltQlsx;
//import com.knowledge.service.MaterialsService;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//
//@Component
//public class QltqlsxWork {
//    private final Logger logger = LoggerFactory.getLogger(QltqlsxWork.class);
//    @Autowired
//    private MaterialsService materialsService;
//
//    @Autowired
//    private QltQlsxRepository repository;
//
//    @Async
//    public void saveMaterilas(List<QltQlsx> qltQlsxes){
//        for(var qltQlsx : qltQlsxes){
//            try{
//                var list = getMaterials(true,qltQlsx.getMaterialInfo(),qltQlsx.getQlInnerCode());
//                if(list != null && list.size() > 0){
//                    materialsService.saveAll(list);
//                }
//            }catch (Exception e){
//                logger.error("解析material失败");
//            }
//        }
//    }
//
//    @Async
//    public void saveQltQlsx(List<QltQlsx> qltQlsxes){
//        try{
//            repository.saveAll(qltQlsxes);
//        }catch (Exception e){
//            logger.error("插入失败");
//        }
//    }
//
//
//    public  List<Materials> getMaterials(boolean flag, String xml, String qlInnerCode) throws IOException {
//        if(xml == null || "".equals(xml)){
//            return null;
//        }
//        List<Materials> materials = new ArrayList<>();
//        List<Map<String,Object>> list = new ArrayList<>();
//        var jsonObject = JsonUtils.xml2Json(xml);
//
//        var mapsStr = JSONObject.parseObject(jsonObject.get("DATAAREA").toString()).get("MATERIALS");
//        if(mapsStr.toString() == null || "".equals(mapsStr.toString())){
//            return null;
//        }
//        Map maps = (Map)mapsStr;
//
//        try{
//            list = (List<Map<String, Object>>) maps.get("MATERIAL");
//
//        }catch (ClassCastException e){
//            Map<String, Object> m = (Map<String, Object>) maps.get("MATERIAL");
//            if(m != null || m.size() > 0){
//                list.add(m);
//            }
//        }
//        if(list.size() <= 0){
//            return null;
//        }
//        for(var map : list){
//
//            Map exampleTable = (Map) list.get(0).get("EXAMPLETABLE");
//            Map emptyTable = (Map) list.get(0).get("EMPTYTABLE");
//
//            String format = map.get("FORMAT").toString();
//
//            Integer isRq = getInteger(map.get("IS_RQ").toString());
//
//
//            Integer necessity = getInteger(map.get("NECESSITY").toString());
//            String guId = map.get("MATERIALGUID").toString();
//
//            Materials materials1 = new Materials(qlInnerCode,guId,
//                    format,map.get("NAME").toString(),isRq,necessity,
//                    map.get("BAK").toString(),map.get("DETAIL_REQUIREMENT").toString(),
//                    map.get("NECESSITY_DESC").toString(),
//                    map.get("MATERIAL_RES").toString(),exampleTable.get("FILEURL").toString(),
//                    exampleTable.get("FILENAME").toString(),
//                    emptyTable.get("FILEURL").toString(),emptyTable.get("FILENAME").toString());
//            if(flag){
//                List<Materials> materList = null;
//                materList = materialsService.findByGuIdAndQlInnerCode(guId,qlInnerCode);
//                if(materList.size() > 0){
//                    materials1.setId(materList.get(0).getId());
//                }
//            }
//            materials.add(materials1);
//        }
//        return materials;
//    }
//
//    private Integer getInteger(String str){
//        if(str == null || "".equals(str)){
//            return null;
//        }
//        if(Valid.isNumeric(str)){
//            return Integer.parseInt(str);
//        }
//        return null;
//    }
//}
