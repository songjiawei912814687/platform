package com.knowledge.domain.output;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.common.Enum.*;
import com.common.utils.JsonUtils;
import com.knowledge.model.QltQlsx;
import org.apache.commons.lang.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;

public class QltQlsxOutput extends QltQlsx {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

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

    private String  mainItemName;

    private String subItemName;

    private String organizationName;

    private Integer orgId;

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    private List<QltQlsxOutput> qltQlsxOutputList;

    public String getMainItemName() {
        return mainItemName;
    }

    public void setMainItemName(String mainItemName) {
        this.mainItemName = mainItemName;
    }

    public List<QltQlsxOutput> getQltQlsxOutputList() {
        return qltQlsxOutputList;
    }

    public void setQltQlsxOutputList(List<QltQlsxOutput> qltQlsxOutputList) {
        this.qltQlsxOutputList = qltQlsxOutputList;
    }

    public String getSubItemName() {
        return subItemName;
    }

    public void setSubItemName(String subItemName) {
        this.subItemName = subItemName;
    }

    public String getQlNames() {
        if(getQlName()!=null){
            if(getQlName().contains("$")){
                qlNames = getQlName().replace("$","");
            }else {
                qlNames = super.getQlName();
            }
        }
        return qlNames;
    }

    public void setQlNames(String qlNames) {
        this.qlNames = qlNames;
    }

    public String getHangYeClassTypeName() {
        if(getHangYeClassType()!=null){
           return HangYeTypeEnum.getDesc(getHangYeClassType());
        }
        return "";
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
                } catch (Exception e) {
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
            return BjtypeNameEnum.getDesc(getBjtype());
        }
        return "";
    }

    public void setBjtypeName(String bjtypeName) {
        this.bjtypeName = bjtypeName;
    }

    public String getQlKindName() {
        if(getQlKind()!=null){
            return QlkindNameEnum.getDesc(getQlKind());
        }
        return qlKindName;
    }

    public void setQlKindName(String qlKindName) {
        this.qlKindName = qlKindName;
    }

    public String getItemsourceName() {
        if(getItemsource()!=null){
           return ItemSourceNameEnum.getDesc(getItemsource());
        }
        return "";
    }

    public void setItemsourceName(String itemsourceName) {
        this.itemsourceName = itemsourceName;
    }

    public String getShixiangsctypeName() {
        if(getShixiangsctype()!=null){
          return ShixiangsctypeNameEnum.getDesc(getShixiangsctype());
        }

        return "";
    }

    public String getServiceModeName() {
        if(getServiceMode()!=null){
            List<String>serviceMode = Arrays.asList(getServiceMode().split(";"));
            List<String> str = new ArrayList<>();
            for(String serviceModeName1:serviceMode){
                serviceModeName1 = ServiceModeNameEnum.getDesc(serviceModeName1)+" ";
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

                applyTypeName1 = ApplyTypeNameEnum.getDesc(applyTypeName1)+" ";

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