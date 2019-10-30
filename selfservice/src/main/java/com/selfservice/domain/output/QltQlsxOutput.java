package com.selfservice.domain.output;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.common.Enum.*;
import com.common.utils.JsonUtils;
import com.selfservice.model.QltQlsx;
import org.apache.commons.lang.StringUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class QltQlsxOutput extends QltQlsx {


    /**热门事项名字**/
    private String hotName;
    /**是否是最小颗粒名字**/
    private String particlesName;
    /**办件类型名称**/
    private String bjtypeName;
    /**权力事项类型**/
    private String qlKindName;
    /**权力来源**/
    private String itemsourceName;
    /**事项省查类型**/
    private String shixiangsctypeName;
    /**申请方式**/
    private String applyTypeName;
    /**办公地址**/
    private String addressInfo;
    /**行业主题分类**/
    private String hangYeClassTypeName;
    /**权力名称**/
    private String qlNames;
    /**送达方式名称**/
    private String serviceModeName;
    /**组织名称*/
    private String organizationName;

    /**最小颗粒id*/
    private Integer particleId;


    private boolean hashChild;

    private  Integer child;


    public Integer getChild() {
        return child;
    }

    public void setChild(Integer child) {
        this.child = child;
    }

    public boolean getHashChild() {
        if (getChild() != null) {
            if (getChild()>0) {
                hashChild = true;
            } else {
                hashChild = false;
            }
        }
        return hashChild;
    }

    public void setHashChild(boolean hashChild) {
        this.hashChild = hashChild;
    }

    public Integer getParticleId() {
        return particleId;
    }

    public void setParticleId(Integer particleId) {
        this.particleId = particleId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public boolean isHashChild() {
        return hashChild;
    }

    public String getQlNames() {
        if(getQlName()!=null){
            if(getQlName().contains("$")){
                qlNames = getQlName().replace("$","");
            }else {
                qlNames = getQlName();
            }
        }
        return qlNames;
    }

    public void setQlNames(String qlNames) {
        this.qlNames = qlNames;
    }

    public String getHangYeClassTypeName() {
        if(getHangYeClassType()!=null){
            hangYeClassTypeName = HangYeTypeEnum.getDesc(getHangYeClassType());
        }
        return hangYeClassTypeName;
    }

    public void setHangYeClassTypeName(String hangYeClassTypeName) {
        this.hangYeClassTypeName = hangYeClassTypeName;
    }

    public QltQlsxOutput() {
    }
    public String getAddressInfo()  {
        if (getAcceptAddressInfo() != null) {
            try {
                String addressJson = JsonUtils.xml2Json(getAcceptAddressInfo()).toString();
                Map<String, Object> jsonObject = JSONObject.parseObject(addressJson);
                Map<String, Object> str = (Map<String, Object>) jsonObject.get("DATAAREA");
                Map<String, Object> addresss = (Map<String, Object>) str.get("ACCEPT_ADDRESSS");
                try {
                    com.alibaba.fastjson.JSONArray address = (JSONArray) addresss.get("ACCEPT_ADDRESS");
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 0; i < address.size(); i++) {
                        //address.get(int i)是获取json格式”[]”中的值，i是下标
                        Map<String, Object> map = (Map<String, Object>) address.get(i);
                        if (map.get("ADDRESS") != null) {
                            stringBuilder.append(map.get("ADDRESS")).append(" ");
                        }
                        if (map.get("PHONE") != null) {
                            stringBuilder.append(map.get("PHONE")).append(" ");
                        }
                        if (map.get("ACCEPT_TIMEDESC") != null) {
                            stringBuilder.append(map.get("ACCEPT_TIMEDESC")).append(" ");
                        }
                    }
                    addressInfo = stringBuilder.toString();
                } catch (RuntimeException e) {
                    Map<String, Object> address = (Map<String, Object>) addresss.get("ACCEPT_ADDRESS");
                    StringBuilder stringBuilder = new StringBuilder();
                    if (address.get("ADDRESS") != null) {
                        stringBuilder.append(address.get("ADDRESS")).append(" ");
                    }
                    if (address.get("PHONE") != null) {
                        stringBuilder.append(address.get("PHONE")).append(" ");
                    }
                    if (address.get("ACCEPT_TIMEDESC") != null) {
                        stringBuilder.append(address.get("ACCEPT_TIMEDESC")).append(" ");
                    }
                    addressInfo = stringBuilder.toString();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return addressInfo;
    }
    public void setAddressInfo(String addressInfo) {
        this.addressInfo = addressInfo;
    }

    public String getBjtypeName() {
        if(getBjtype()!=null){
            switch (getBjtype().trim()){
                case "1":
                    bjtypeName = BjtypeNameEnum.IMMEDIATELY_DO.getDesc();
                    break;
                case "2":
                    bjtypeName = BjtypeNameEnum.PROMISE_DO.getDesc();
                    break;
                case "3":
                    bjtypeName = BjtypeNameEnum.OTHER_DO.getDesc();
                    break;
                    default:
            }
        }
        return bjtypeName;
    }

    public void setBjtypeName(String bjtypeName) {
        this.bjtypeName = bjtypeName;
    }

    public String getQlKindName() {
        if(getQlKind()!=null){
            switch (getQlKind().trim()){
                case "01":
                    qlKindName = QlkindNameEnum.ADMINISTRATION_PERMISSION.getDesc();
                    break;
                case "03":
                    qlKindName = QlkindNameEnum.ADMINISTRATION_PUNISH.getDesc();
                    break;
                case "04":
                    qlKindName = QlkindNameEnum.ADMINISTRATION_FORCE.getDesc();
                    break;
                case "05":
                    qlKindName = QlkindNameEnum.ADMINISTRATION_LEVY.getDesc();
                    break;
                case "06":
                    qlKindName = QlkindNameEnum.ADMINISTRATION_PAY.getDesc();
                    break;
                case "07":
                    qlKindName = QlkindNameEnum.ADMINISTRATION_DECIDE.getDesc();
                    break;
                case "08":
                    qlKindName = QlkindNameEnum.ADMINISTRATION_SURE.getDesc();
                    break;
                case "09":
                    qlKindName = QlkindNameEnum.ADMINISTRATION_AWARD.getDesc();
                    break;
                case "10":
                    qlKindName = QlkindNameEnum.ADMINISTRATION_OTHER_POWER.getDesc();
                    break;
                case "13":
                    qlKindName = QlkindNameEnum.AUDIT_INFORMATION.getDesc();
                    break;
                case "14":
                    qlKindName = QlkindNameEnum.PUBLIC_SERVICE.getDesc();
                    break;
                case "15":
                    qlKindName = QlkindNameEnum.INNER_MANAGEMENT.getDesc();
                    break;
                case "16":
                    qlKindName = QlkindNameEnum.SEEC_MATTERS.getDesc();
                    break;
                    default:
            }
        }
        return qlKindName;
    }

    public void setQlKindName(String qlKindName) {
        this.qlKindName = qlKindName;
    }

    public String getItemsourceName() {
        if(getItemsource()!=null){
            switch (getItemsource().trim()){
                case "1":
                    itemsourceName = ItemSourceNameEnum.LAW_SAME_LEVEL.getDesc();
                    break;
                case "2":
                    itemsourceName = ItemSourceNameEnum.CENTER_PROVINCIAL_LEVEL.getDesc();
                    break;
                case "3":
                    itemsourceName = ItemSourceNameEnum.PROVINCIAL_LEVEL_CITIES.getDesc();
                    break;
                case "4":
                    itemsourceName = ItemSourceNameEnum.PROVINCIAL_LEVEL_TOWN.getDesc();
                    break;
                case "5":
                    itemsourceName = ItemSourceNameEnum.CITIES_LEVEL_TOWN.getDesc();
                    break;
                case "6":
                    itemsourceName = ItemSourceNameEnum.CENTER_LEVEL_CITIES.getDesc();
                    break;
                case "7":
                    itemsourceName = ItemSourceNameEnum.CENTER_LEVEL_TOWN.getDesc();
                    break;
                    default:

            }
        }
        return itemsourceName;
    }

    public void setItemsourceName(String itemsourceName) {
        this.itemsourceName = itemsourceName;
    }

    public String getShixiangsctypeName() {
        if(getShixiangsctype()!=null){
            switch (getShixiangsctype()){
                case "0":
                    shixiangsctypeName = ShixiangsctypeNameEnum.NONE.getDesc();
                    break;
                case "1":
                    shixiangsctypeName = ShixiangsctypeNameEnum.BEFORE_SHIPMENT.getDesc();
                    break;
                case "2":
                    shixiangsctypeName = ShixiangsctypeNameEnum.ON_RUN.getDesc();
                    break;
                case "3":
                    shixiangsctypeName = ShixiangsctypeNameEnum.APPROVAL_PARALLEL.getDesc();
                    break;
                case "4":
                    shixiangsctypeName = ShixiangsctypeNameEnum.OTHER.getDesc();
                default:
            }
        }
        return shixiangsctypeName;
    }

    public String getServiceModeName() {
        if(getServiceMode()!=null){
            List<String>serviceMode = Arrays.asList(getServiceMode().split(";"));
            List<String> str = new ArrayList<>();
            for(String serviceModeName1:serviceMode){
                switch (serviceModeName1){
                    case "1":
                        serviceModeName1 = ServiceModeNameEnum.ARRIVE_SPOT.getDesc()+" ";
                        break;
                    case "2":
                        serviceModeName1 = ServiceModeNameEnum.ARRIVE_EXPRESS.getDesc()+" ";
                        break;
                    case "3":
                        serviceModeName1 = ServiceModeNameEnum.ARRIVE_NET.getDesc()+" ";
                        break;
                    case "4":
                        serviceModeName1 = ServiceModeNameEnum.ARRIVE_STAFF.getDesc()+" ";
                        break;
                        default:
                }
                str.add(serviceModeName1);
                serviceModeName = StringUtils.join(str.toArray());
            }
        }
        return serviceModeName;
    }

    public void setServiceModeName(String serviceModeName) {
        this.serviceModeName = serviceModeName;
    }
    public void setShixiangsctypeName(String shixiangsctypeName) {
        this.shixiangsctypeName = shixiangsctypeName;
    }
    //注意申请方式有多种
    public String getApplyTypeName() {
        if(getApplyType()!=null){
            List<String>applyType = Arrays.asList(getApplyType().split(";"));
            List<String> str = new ArrayList<>();
            for(String applyTypeName1:applyType){
                switch (applyTypeName1){
                    case "1":
                        applyTypeName1 = ApplyTypeNameEnum.NET_APPLY.getDesc()+" ";
                        break;
                    case "2":
                        applyTypeName1 = ApplyTypeNameEnum.WINDOW_APPLY.getDesc()+" ";
                        break;
                    case "3":
                        applyTypeName1 = ApplyTypeNameEnum.POST_APPLY.getDesc()+" ";
                        break;
                    case "4":
                        applyTypeName1 = ApplyTypeNameEnum.MACHINE_APPLY.getDesc()+" ";
                        break;
                        default:
                }
                str.add(applyTypeName1);
                applyTypeName = StringUtils.join(str.toArray());
            }
        }
        return applyTypeName;
    }

    public void setApplyTypeName(String applyTypeName) {
        this.applyTypeName = applyTypeName;
    }

    public String getHotName() {
        if(getHot()!=null){
            if(getHot()==0){
                hotName = "不是热门事项";
            }else {
                hotName = "是热门事项";
            }
        }
        return hotName;
    }

    public void setHotName(String hotName) {
        this.hotName = hotName;
    }

    public String getParticlesName() {
        if(getParticles()!=null){
            if(getParticles()==0){
                particlesName = "不是最小颗粒";
            }else {
                particlesName = "是最小颗粒";
            }
        }
        return particlesName;
    }

    public void setParticlesName(String particlesName) {
        this.particlesName = particlesName;
    }

    private String QA_Info;

    public String getQA_Info() {
        if (getQaInfo() != null) {
            try {
                String QA_Json = JsonUtils.xml2Json(getQaInfo()).toString();
                Map<String, Object> jsonObject = JSONObject.parseObject(QA_Json);
                Map<String, Object> str = (Map<String, Object>) jsonObject.get("DATAAREA");
                Map<String, Object> questions = (Map<String, Object>) str.get("QAS");
                try {
                    com.alibaba.fastjson.JSONArray question = (JSONArray) questions.get("QA");
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 0; i < question.size(); i++) {
                        //address.get(int i)是获取json格式”[]”中的值，i是下标
                        Map<String, Object> map = (Map<String, Object>) question.get(i);
                        if (map.get("QUESTION") != null) {
                            stringBuilder.append(map.get("QUESTION")).append(" ");
                        }
                        if (map.get("ANSWER") != null) {
                            stringBuilder.append(map.get("ANSWER")).append(" ");
                        }
                    }
                    QA_Info = stringBuilder.toString();
                } catch (RuntimeException e) {
                    Map<String, Object> question = (Map<String, Object>) questions.get("QA");
                    StringBuilder stringBuilder = new StringBuilder();
                    if (question.get("QUESTION") != null) {
                        stringBuilder.append(question.get("QUESTION")).append(" ");
                    }
                    if (question.get("ANSWER") != null) {
                        stringBuilder.append(question.get("ANSWER")).append(" ");
                    }
                    QA_Info = stringBuilder.toString();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return QA_Info;
    }

    public void setQA_Info(String QA_Info) {
        this.QA_Info = QA_Info;
    }
}