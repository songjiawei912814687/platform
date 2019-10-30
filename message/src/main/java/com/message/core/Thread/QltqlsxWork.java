package com.message.core.Thread;

import com.alibaba.fastjson.JSONObject;
import com.common.utils.JsonUtils;
import com.common.utils.Valid;
import com.message.mapper.jpa.*;
import com.message.model.*;
import com.message.service.MaterialsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import java.util.*;


@Component
public class QltqlsxWork {

    @Autowired
    private MaterialsService materialsService;

    @Autowired
    private QltQlsxRepository repository;
    @Autowired
    private ChargeItemRepository chargeItemRepository;

    @Autowired
    private CommonQuestionRepository commonQuestionRepository;

    @Autowired
    private AcceptAddressRepository acceptAddressRepository;

    /**
     * @param qltQlsxes 同步来的权力事项
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveQltQlsx(List<QltQlsx> qltQlsxes){
        repository.saveAll(qltQlsxes);
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveDetail(List<QltQlsx> qltQlsxes){
        for(var qltQlsx : qltQlsxes){
            //同步材料清单
            var list = getMaterials(qltQlsx.getMATERIAL_INFO(),qltQlsx.getQL_INNER_CODE());
            if(!CollectionUtils.isEmpty(list)){
                materialsService.deleteByQlInnerCode(qltQlsx.getQL_INNER_CODE());
                materialsService.saveAll(list);
            }
            //同步收费项目
            var chargeItems = getChargeItems(qltQlsx.getCHARGEITEM_INFO(),qltQlsx.getQL_INNER_CODE());
            if(chargeItems != null && chargeItems.size() > 0){
                chargeItemRepository.deleteByQlInnerCode(qltQlsx.getQL_INNER_CODE());
                chargeItemRepository.saveAll(chargeItems);
            }
            //同步常见问题解答格式说明
            var qus = getCommonQuestion(qltQlsx.getQA_INFO(),qltQlsx.getQL_INNER_CODE());
            if(qus != null && qus.size() > 0){
                commonQuestionRepository.deleteByQlInnerCode(qltQlsx.getQL_INNER_CODE());
                commonQuestionRepository.saveAll(qus);
            }
            //同步受理地点格式说明
            var acceptAddresses = getAcceptAddress(qltQlsx.getACCEPT_ADDRESS_INFO(),qltQlsx.getQL_INNER_CODE());
            if(acceptAddresses != null && acceptAddresses.size() > 0){
                acceptAddressRepository.deleteByQlInnerCode(qltQlsx.getQL_INNER_CODE());
                acceptAddressRepository.saveAll(acceptAddresses);
            }
        }
    }

    private List<AcceptAddress> getAcceptAddress(String xml, String qlInnerCode) {
        if(xml.equals("<?xml version=\"1.0\" encoding=\"utf-8\"?><DATAAREA><CHANGE_FLAG></CHANGE_FLAG><ACCEPT_ADDRESSS><ACCEPT_ADDRESS><ADDRESS_KIND>3;</ADDRESS_KIND><ADDRESS>富阳市富春街道体育场路237号</ADDRESS><ACCEPT_TIMEDESC>上午8:00-11:00，      下午13:00-16:30；</ACCEPT_TIMEDESC></ACCEPT_ADDRESS></ACCEPT_ADDRESSS></DATAAREA>")){
            System.out.println("---------------");
        }
        List<AcceptAddress> acceptAddress = null;
        try {
            if(xml == null || "".equals(xml)){
                return null;
            }
            acceptAddress = new ArrayList<>();
            List<Map<String,Object>> list = new ArrayList<>();
            var jsonObject = JsonUtils.xml2Json(xml);

            var mapsStr = JSONObject.parseObject(jsonObject.get("DATAAREA").toString()).get("ACCEPT_ADDRESSS");
            if(mapsStr.toString() == null || "".equals(mapsStr.toString())){
                return null;
            }
            Map maps = (Map)mapsStr;
            try{
                list = (List<Map<String, Object>>) maps.get("ACCEPT_ADDRESS");

            }catch (ClassCastException e){
                Map<String, Object> m = (Map<String, Object>) maps.get("ACCEPT_ADDRESS");
                if(m != null || m.size() > 0){
                    list.add(m);
                }
            }
            if(list.size() <= 0){
                return null;
            }
            for(var map : list){
                String address = map.get("ADDRESS")==null?"":map.get("ADDRESS").toString();
                String addressKind = map.get("ADDRESS_KIND")==null?"":map.get("ADDRESS_KIND").toString();
                String acceptTimedesc = map.get("ACCEPT_TIMEDESC")==null?"":map.get("ACCEPT_TIMEDESC").toString();
                String phone = map.get("PHONE")==null?"":map.get("PHONE").toString();
                String uuid = map.get("UUID")==null?"":map.get("UUID").toString();
                AcceptAddress chargeItem = new AcceptAddress(qlInnerCode,address,addressKind,acceptTimedesc,phone,uuid);
                acceptAddress.add(chargeItem);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return acceptAddress;
    }

    private List<CommonQuestion> getCommonQuestion(String xml, String qlInnerCode){
        List<CommonQuestion> commonQuestions = null;
        try {
            if(xml == null || "".equals(xml)){
                return null;
            }
            commonQuestions = new ArrayList<>();
            List<Map<String,Object>> list = new ArrayList<>();
            var jsonObject = JsonUtils.xml2Json(xml);

            var mapsStr = JSONObject.parseObject(jsonObject.get("DATAAREA").toString()).get("QAS");
            if(mapsStr.toString() == null || "".equals(mapsStr.toString())){
                return null;
            }
            Map maps = (Map)mapsStr;
            try{
                list = (List<Map<String, Object>>) maps.get("QA");

            }catch (ClassCastException e){
                Map<String, Object> m = (Map<String, Object>) maps.get("QA");
                if(m != null || m.size() > 0){
                    list.add(m);
                }
            }
            if(list.size() <= 0){
                return null;
            }
            for(var map : list){
                String question = map.get("QUESTION")==null?"":map.get("QUESTION").toString();
                question = question.replace("<![CDATA[","");
                question = question.replace("]]>","");
                String answer = map.get("ANSWER")==null?"":map.get("ANSWER").toString();
                answer = answer.replace("<![CDATA[","");
                answer = answer.replace("]]>","");
                CommonQuestion chargeItem = new CommonQuestion(qlInnerCode,question,answer);
                commonQuestions.add(chargeItem);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return commonQuestions;
    }

    private List<ChargeItem> getChargeItems(String xml, String qlInnerCode){
        List<ChargeItem> chargeItems = null;
        try {
            if(xml == null || "".equals(xml)){
                return null;
            }
            chargeItems = new ArrayList<>();
            List<Map<String,Object>> list = new ArrayList<>();
            var jsonObject = JsonUtils.xml2Json(xml);

            var mapsStr = JSONObject.parseObject(jsonObject.get("DATAAREA").toString()).get("CHARGEITEMS");
            if(mapsStr.toString() == null || "".equals(mapsStr.toString())){
                return null;
            }
            Map maps = (Map)mapsStr;
            try{
                list = (List<Map<String, Object>>) maps.get("CHARGEITEM");

            }catch (ClassCastException e){
                Map<String, Object> m = (Map<String, Object>) maps.get("CHARGEITEM");
                if(m != null || m.size() > 0){
                    list.add(m);
                }
            }
            if(list.size() <= 0){
                return null;
            }
            for(var map : list){
                String guId = map.get("CHARGEITEMGUID")==null?"":map.get("CHARGEITEMGUID").toString();
                String basis = map.get("BASIS")==null?"":map.get("BASIS").toString();;
                String reductionDesc = map.get("REDUCTION_DESC")==null?"":map.get("REDUCTION_DESC").toString();
                ChargeItem chargeItem = new ChargeItem(qlInnerCode,guId,map.get("NAME").toString(),
                        basis,reductionDesc);
                chargeItems.add(chargeItem);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return chargeItems;
    }




    public  List<Materials> getMaterials(String xml, String qlInnerCode){
        List<Materials> materials = null;
        try {
            if(StringUtils.isBlank(xml)){
                return null;
            }
            materials = new ArrayList<>();
            List<Map<String,Object>> list = new ArrayList<>();
            var jsonObject = JsonUtils.xml2Json(xml);

            var mapsStr = JSONObject.parseObject(jsonObject.get("DATAAREA").toString()).get("MATERIALS");
            if(mapsStr.toString() == null || "".equals(mapsStr.toString())){
                return null;
            }
            Map maps = (Map)mapsStr;
            try{
                list = (List<Map<String, Object>>) maps.get("MATERIAL");

            }catch (ClassCastException e){
                Map<String, Object> m = (Map<String, Object>) maps.get("MATERIAL");
                if(m != null || m.size() > 0){
                    list.add(m);
                }
            }
            if(list.size() <= 0){
                return null;
            }
            for(var map : list){
                Map exampleTable = (Map) map.get("EXAMPLETABLE");
                Map emptyTable = (Map) map.get("EMPTYTABLE");
                String format = map.get("FORMAT")==null?"":map.get("FORMAT").toString();
                Integer isRq = getInteger(map.get("IS_RQ").toString());
                Integer necessity = getInteger(map.get("NECESSITY").toString());
                String guId = map.get("MATERIALGUID").toString();
                Materials materials1 = new Materials(qlInnerCode,guId,
                        format,map.get("NAME").toString(),isRq,necessity,
                        map.get("BAK")==null?"":map.get("BAK").toString(),map.get("DETAIL_REQUIREMENT")==null?"":map.get("DETAIL_REQUIREMENT").toString(),
                        map.get("NECESSITY_DESC")==null?"":map.get("NECESSITY_DESC").toString(),
                        map.get("MATERIAL_RES")==null?"":map.get("MATERIAL_RES").toString(),exampleTable.get("FILEURL")==null?"":exampleTable.get("FILEURL").toString(),
                        exampleTable.get("FILENAME")==null?"":exampleTable.get("FILENAME").toString(),
                        emptyTable.get("FILEURL")==null?"":emptyTable.get("FILEURL").toString(),emptyTable.get("FILENAME")==null?"":emptyTable.get("FILENAME").toString());
                materials.add(materials1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return materials;
    }

    private Integer getInteger(String str){
        if(str == null || "".equals(str)){
            return null;
        }
        if(Valid.isNumeric(str)){
            return Integer.parseInt(str);
        }
        return null;
    }
//
//    public void updateMinimum(List<QltQlsx> qltQlsxList) {
//
//        //先查询现在所有未删除的最小颗粒
//        List<MinimumParticle> minimumNowList = minimumParticleRepository.findAllByAmputated(0);
//        for(MinimumParticle minimumParticle:minimumNowList){
//            minimumParticle.setAmputated(1);
//            minimumParticleRepository.save(minimumParticle);
//        }
//        List<String> miniQlIdList = minimumNowList.stream().map(MinimumParticle::getQlsxId).collect(Collectors.toList());
//
//        List<String> sysQLIdList = qltQlsxList.stream().map(QltQlsx::getQL_Full_ID).collect(Collectors.toList());
//
//        //获取相同的权力id
//        sysQLIdList.retainAll(miniQlIdList);
//
//        List<MinimumParticle> minimumRetainList = minimumParticleRepository.findAllByAmputatedAndQlsxIdIn(1,sysQLIdList);
//
//        for(MinimumParticle minimumParticle:minimumRetainList){
//            minimumParticle.setAmputated(0);
//            minimumParticleRepository.save(minimumParticle);
//        }
//    }
}
