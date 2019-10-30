package com.api.domain.output;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.api.model.QltQlsx;
import com.common.Enum.*;
import com.common.utils.JsonUtils;
import org.apache.commons.lang.StringUtils;
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
            switch (getHangYeClassType()){
                case "01":
                    hangYeClassTypeName = HangYeTypeEnum.EDUCATION.getDesc();
                    break;
                case "02":
                    hangYeClassTypeName = HangYeTypeEnum.MEDICAL_TREATMENT.getDesc();
                    break;
                case "03":
                    hangYeClassTypeName = HangYeTypeEnum.MEDICAL_BORN.getDesc();
                    break;
                case "04":
                    hangYeClassTypeName = HangYeTypeEnum.CULTURE_SPORTS.getDesc();
                    break;
                case "05":
                    hangYeClassTypeName = HangYeTypeEnum.RADIO_PUBLICATION.getDesc();
                    break;
                case "06":
                    hangYeClassTypeName = HangYeTypeEnum.SCIENCE_INNOVATE.getDesc();
                    break;
                case "07":
                    hangYeClassTypeName = HangYeTypeEnum.CIVIL_WEDDING.getDesc();
                    break;
                case "08":
                    hangYeClassTypeName = HangYeTypeEnum.CIVIL_ADOPTION.getDesc();
                    break;
                case "09":
                    hangYeClassTypeName = HangYeTypeEnum.CIVIL_WELFARE.getDesc();
                    break;
                case "10":
                    hangYeClassTypeName = HangYeTypeEnum.CIVIL_LEAVE_ARMY.getDesc();
                    break;
                case "11":
                    hangYeClassTypeName = HangYeTypeEnum.CIVIL_DEAD.getDesc();
                    break;
                case "12":
                    hangYeClassTypeName = HangYeTypeEnum.CIVIL_SOCIAL_ORGANIZATION.getDesc();
                    break;
                case "13":
                    hangYeClassTypeName = HangYeTypeEnum.CIVIL_OTHER.getDesc();
                    break;
                case "14":
                    hangYeClassTypeName = HangYeTypeEnum.PERSONNEL.getDesc();
                    break;
                case "15":
                    hangYeClassTypeName = HangYeTypeEnum.SOCIAL_SECURITY.getDesc();
                    break;
                case "16":
                    hangYeClassTypeName = HangYeTypeEnum.TRAVEL_SERVICE.getDesc();
                    break;
                case "17":
                    hangYeClassTypeName = HangYeTypeEnum.LAND_RESOURCE.getDesc();
                    break;
                case "18":
                    hangYeClassTypeName = HangYeTypeEnum.HOUSE_CONSTRUCTION.getDesc();
                    break;
                case "19":
                    hangYeClassTypeName = HangYeTypeEnum.TRANSPORTATION.getDesc();
                    break;
                case "20":
                    hangYeClassTypeName = HangYeTypeEnum.ENVIRONMENTAL_METEOROLOGICAL.getDesc();
                    break;
                case "21":
                    hangYeClassTypeName = HangYeTypeEnum.MARKET_INDUSTRY.getDesc();
                    break;
                case "22":
                    hangYeClassTypeName = HangYeTypeEnum.MARKET_TECHNOLOGY.getDesc();
                    break;
                case "23":
                    hangYeClassTypeName = HangYeTypeEnum.MARKET_FOOD.getDesc();
                    break;
                case "24":
                    hangYeClassTypeName = HangYeTypeEnum.SAFETY_PRODUCTION.getDesc();
                    break;
                case "25":
                    hangYeClassTypeName = HangYeTypeEnum.SECURITY_FIRE.getDesc();
                    break;
                case "26":
                    hangYeClassTypeName = HangYeTypeEnum.ULTIMATE_JUSTICE.getDesc();
                    break;
                case "27":
                    hangYeClassTypeName = HangYeTypeEnum.PLANNING_SURVEYING.getDesc();
                    break;
                case "28":
                    hangYeClassTypeName = HangYeTypeEnum.ECONOMIC_DEVELOPMENT.getDesc();
                    break;
                case "29":
                    hangYeClassTypeName = HangYeTypeEnum.FINANCE_AFFAIRS.getDesc();
                    break;
                case "30":
                    hangYeClassTypeName = HangYeTypeEnum.NATIONAL_RELIGION.getDesc();
                    break;
                case "31":
                    hangYeClassTypeName = HangYeTypeEnum.FOREIGN_AFFAIRS.getDesc();
                    break;
                case "32":
                    hangYeClassTypeName = HangYeTypeEnum.ANIMAL_WATER.getDesc();
                    break;
                case "33":
                    hangYeClassTypeName = HangYeTypeEnum.MARINE_FISHERY.getDesc();
                    break;
                case "34":
                    hangYeClassTypeName = HangYeTypeEnum.PUBLIC_UTILITY.getDesc();
                    break;
                case "35":
                    hangYeClassTypeName = HangYeTypeEnum.INTERMEDIARY_SERVICES.getDesc();
                    break;
                case "36":
                    hangYeClassTypeName = HangYeTypeEnum.OTHER.getDesc();
                    break;
                case "37":
                    hangYeClassTypeName = HangYeTypeEnum.CUSTOMS_INSPECTION.getDesc();
                    break;
                case "38":
                    hangYeClassTypeName = HangYeTypeEnum.FINANCE_SECURITIES.getDesc();
                    break;
                case "39":
                    hangYeClassTypeName = HangYeTypeEnum.POSTAL_COMMUNICATION.getDesc();
                    break;
                default:
            }
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
                    JSONArray address = (JSONArray) addresss.get("ACCEPT_ADDRESS");
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
}