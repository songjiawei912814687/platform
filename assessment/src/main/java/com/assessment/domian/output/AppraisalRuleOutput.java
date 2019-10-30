package com.assessment.domian.output;

import com.assessment.core.util.AppConsts;
import com.assessment.model.AppraisalIndex;
import com.assessment.model.AppraisalRule;

public class AppraisalRuleOutput extends AppraisalRule {
    private String indexName;
    //计分方式
    private  String scoreTypeName;
    //状态
    private  String stateName;
    //打分设置
    private String scoreSourceName;
    //模板关联指标Id
    private Integer templateIndexId;
    //模板关联规则Id
    private Integer templateRuleId;

    public String getScoreTypeName() {
        if (getScoreType() != null) {
            if (getScoreType() == AppConsts.Add_Points) {
                scoreTypeName = "直接加分";
            } else if (getScoreType() == AppConsts.Reduce_Points) {
                scoreTypeName = "直接减分";
            }
        }
        return scoreTypeName;
    }

    public String getStateName() {
        if (getState() != null) {
            if (getState() == AppConsts.start) {
                stateName = "启用";
            } else if (getState() == AppConsts.stop) {
                stateName = "停用";
            }
        }
        return stateName;
    }

    public String getScoreSourceName() {
        if (getScoreSource() != null) {
            if (getScoreSource() == AppConsts.System_Default) {
                scoreSourceName = "系统默认值";
            } else if (getScoreSource() == AppConsts.Manual_Adjustment) {
                scoreSourceName = "手动调整";
            }else if(getScoreSource() == AppConsts.Automatic_Calculation){
                scoreSourceName = "自动计算";
            }
        }
        return scoreSourceName;
    }

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public Integer getTemplateIndexId() {
        return templateIndexId;
    }

    public void setTemplateIndexId(Integer templateIndexId) {
        this.templateIndexId = templateIndexId;
    }

    public Integer getTemplateRuleId() {
        return templateRuleId;
    }

    public void setTemplateRuleId(Integer templateRuleId) {
        this.templateRuleId = templateRuleId;
    }
}
