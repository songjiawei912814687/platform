package com.knowledge.domain.output;

import com.common.Enum.FormatNameEnum;
import com.common.Enum.NecessityNameEnum;
import com.knowledge.model.Materials;
import io.swagger.annotations.ApiModelProperty;


public class MaterialsOutput extends Materials {


    /**"材料形式名称,长度最大50"*/
    private String materialFormName;

    /**材料详细要求**/
    private String requestDetail;

    /**必要性描述*/
    private String necessarilyDescription;

    /**材料出具单位*/
    private String meteriaOrganization;

    /**备注*/
    private String remark;

    /**材料详细要求*/
    public String getRequestDetail() {
        if(getDetailRequirement()!=null){
            requestDetail = getDetailRequirement();
        }
        return requestDetail;
    }

    public void setRequestDetail(String requestDetail) {
        this.requestDetail = requestDetail;
    }

    /**材料出具单位*/
    public String getMeteriaOrganization() {
        if(getMaterialRes()!=null){
            meteriaOrganization = getMaterialRes();
        }
        return meteriaOrganization;
    }

    public void setMeteriaOrganization(String meteriaOrganization) {
        this.meteriaOrganization = meteriaOrganization;
    }

    /**备注*/
    public String getRemark() {
        if(getBak()!=null){
            remark = getBak();
        }
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**材料形式名称*/
    public String getMaterialFormName() {
        if(getFormat()!=null){
            switch (getFormat()){
                case "1":
                    materialFormName = FormatNameEnum.ORIGINAL.getDesc();
                    break;
                case "2":
                    materialFormName = FormatNameEnum.COPY.getDesc();
                    break;
                case "3":
                    materialFormName = FormatNameEnum.ORIGINAL_OR_COPY.getDesc();
                    break;
                case "4":
                    materialFormName = FormatNameEnum.PAPER.getDesc();
                    break;
                case "5":
                    materialFormName = FormatNameEnum.ELECTRONIC_DOCUMENT.getDesc();
                    break;
                case "6":
                    materialFormName = FormatNameEnum.NO_SUBMIT.getDesc();
                    break;
                case "7":
                    materialFormName = FormatNameEnum.DATA_SCARCITY.getDesc();
                    break;
                case "8":
                    materialFormName = FormatNameEnum.SCAN_ORIGINAL.getDesc();
                    break;
                case "9":
                    materialFormName = FormatNameEnum.PDF.getDesc();
                    break;
                    default:
            }
        }
        return materialFormName;
    }

    public void setMaterialFormName(String materialFormName) {
        this.materialFormName = materialFormName;
    }

    /**重要性*/
    public String getNecessarilyDescription() {
        if(getNecessity()!=null){
            switch (getNecessity()){
                case 1:
                    necessarilyDescription = NecessityNameEnum.NECESSARY.getDesc();
                    break;
                case 2:
                    necessarilyDescription = NecessityNameEnum.UN_NECESSARY.getDesc();
                    break;
                case 3:
                    necessarilyDescription = NecessityNameEnum.AFTER_REPAIR.getDesc();
                    break;
                    default:
            }
        }
        return necessarilyDescription;
    }

    public void setNecessarilyDescription(String necessarilyDescription) {
        this.necessarilyDescription = necessarilyDescription;
    }
}
