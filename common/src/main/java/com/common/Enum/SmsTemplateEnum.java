package com.common.Enum;

import com.common.model.Data;

import java.util.ArrayList;
import java.util.List;

public enum SmsTemplateEnum {
    FQSQ_TYPE("fqsq","发起申请","template"),
    SPBTG_TYPE("spbtg","审批不通过","template"),
    RYRZSPTG_TYPE("ryrzsptg","人员入职审批通过","template"),
    RYRZSPBTG_TYPE("ryrzspbtg","人员入职审批不通过","template"),
    RYLZSPTG_TYPE("rylzsptg","人员离职审批通过","template"),
    RYLZSPBTG_TYPE("rylzspbtg","人员离职审批不通过","template"),
    CKPJ_TYPE("ckpj","窗口评价","template"),
    QJSQSPTG_TYPE("qjsqsptg","请假申请审批通过","template"),
    QJSQSPBTG_TYPE("qjsqspbtg","请假申请审批不通过","template"),
    JBSQSPTG_TYPE("jbsqsptg","加班申请审批通过","template"),
    JBSQSPBTG_TYPE("jbsqspbtg","加班申请审批不通过","template"),
    TXSQSPTG_TYPE("txsqsptg","调休申请审批通过","template"),
    TXSQSPBTG_TYPE("txsqspbtg","调休申请审批不通过","template"),
    HYTX_TYPE("hytx","会议提醒","template"),
    TSJY_TYPE("tsjy","投诉建议","template"),
    CKHF_TYPE("ckhf","窗口回访","template"),
    HYSYYSPTG_TYPE("hysyysptg","会议室预约审批通过","template"),
    HYSYYSPBTG_TYPE("hysyyspbtg","会议室预约审批不通过","template"),
    HYSYYCXTXSQR_TYPE("hysyycxtx","会议室预约撤销提醒申请人","template"),
    HYSYYTGTXYHRY_TYPE("hysyytgtxyhry","会议室预约通过提醒与会人员","template"),
    HYSYYTGTXHW_TYPE("hysyytgtxhw","会议室预约通过提醒会务","template"),
    HYSYYTGTXWG_TYPE("hysyytgtxwg","会议室预约通过提醒网管","template"),
    HYSYYCXTXYHRY_TYPE("hysyycxtxyhry","会议室预约撤销提醒与会人员","template"),
//    HYSYYCXTXHW_TYPE("hysyytcxxhw","会议室预约撤销提醒会务","template"),
    HYSYYCXTXHW_TYPE("hysyycxtxhw","会议室预约撤销提醒会务","template"),
    HYSYYCXTXWG_TYPE("hysyycxtxwg","会议室预约撤销提醒网管","template"),
    KQCDTX_TYPE("kqcdtx","考勤迟到提醒","template"),
    KQZTTX_TYPE("kqzttx","考勤早退提醒","template"),
    KQZSDKTX_TYPE("kqzsdktx","考勤早上打卡提醒","template"),
    KQXWDKTX_TYPE("kqxwdktx","考勤下午打卡提醒","template"),
    KQWDKTX_TYPE("kqwdktx","考勤未打卡提醒","template"),
    CKZCPJ_TYPE("ckzcpj","窗口再次评价","template"),
    CKZCHF_TYPE("ckzchf","窗口再次回访","template"),
    WTTSBMFZR_TYPE("wttsbmfzr","问题推送部门负责人","template"),
    FSDA_TYPE("fsda","发送答案给提问人","template"),


    SPR_TYPE("{spr}","审批人","fqsq"),
    SQR_TYPE("{sqr}","申请人","fqsq,spbtg,ryrzsptg,ryrzspbtg,rylzsptg,rylzspbtg,ckpj,qjsqsptg,qjsqspbtg,jbsqsptg,jbsqspbtg,txsqsptg,txsqspbtg,hytx,tsjy,ckhf,hysyysptg,hysyyspbtg,hysyycxtx,hysyycxtxyhry,kqzsdktx,kqxwdktx,kqcdtx,kqzttx,kqwdktx"),
    SQYWLX_TYPE("{sqywlx}","申请业务类型","fqsq,spbtg,ryrzsptg,ryrzspbtg,rylzsptg,rylzspbtg,ckpj,qjsqsptg,qjsqspbtg,jbsqsptg,jbsqspbtg,txsqsptg,txsqspbtg,hytx,tsjy,ckhf,hysyysptg,hysyyspbtg,hysyycxtx,hysyycxtxyhry,kqzsdktx,kqxwdktx,kqdkyctx,ckzcpj,ckzchf"),
    CKH_TYPE("{ckh}","窗口号","ckpj,ckzcpj,ckzchf"),
    BLYWMC_TYPE("{blywmc}","办理业务名称","ckpj"),
    QJLX_TYPE("{qjlx}","请假类型","qjsqsptg,qjsqspbtg"),
    YHRY_TYPE("{yhry}","与会人员","hytx,hysyycxtxyhry"),
    HYMC_TYPE("{hymc}","会议名称","hytx,hysyysptg,hysyyspbtg,hysyycxtx,hysyycxtxyhry,hysyytgtxhw,hysyytgtxwg,hysyycxtxhw,hysyycxtxwg,hysyytgtxyhry"),
    TSJY_BMGLY_TYPE("{tsjy_bmgly}","部门管理员","tsjy"),
    N_TYPE("{n}","年","hytx,hysyysptg,hysyyspbtg,hysyycxtx,hysyycxtxyhry,kqzsdktx,kqxwdktx,kqcdtx,kqzttx,kqwdktx,hysyytgtxyhry,hysyytgtxhw,hysyytgtxwg,hysyycxtxhw,hysyycxtxwg"),
    Y_TYPE("{y}","月","hytx,hysyysptg,hysyyspbtg,hysyycxtx,hysyycxtxyhry,kqzsdktx,kqxwdktx,kqcdtx,kqzttx,kqwdktx,hysyytgtxyhry,hysyytgtxhw,hysyytgtxwg,hysyycxtxhw,hysyycxtxwg"),
    R_TYPE("{r}","日","hytx,hysyysptg,hysyyspbtg,hysyycxtx,hysyycxtxyhry,kqzsdktx,kqxwdktx,kqcdtx,kqzttx,kqwdktx,hysyytgtxyhry,hysyytgtxhw,hysyytgtxwg,hysyycxtxhw,hysyycxtxwg"),
    D_TYPE("{d}","点","hytx,hysyysptg,hysyyspbtg,hysyycxtx,hysyycxtxyhry,kqzsdktx,kqxwdktx,kqcdtx,kqzttx,kqwdktx,hysyytgtxyhry,hysyytgtxhw,hysyytgtxwg,hysyycxtxhw,hysyycxtxwg"),
    HYS_TYPE("{hys}","会议室名称","hytx,hysyysptg,hysyyspbtg,hysyycxtx,hysyycxtxyhry,hysyytgtxyhry,hysyytgtxhw,hysyytgtxwg,hysyycxtxhw,hysyycxtxwg"),
    BMFZR_TYPE("{bmfzr}","部门负责人","wttsbmfzr"),
    TWR_TYPE("{twr}","提问人","fsda"),
    ZBDW_TYPE("{zbdw}","主办单位","hysyytgtxhw，hysyytcxxhw"),
    ;

    private String code;
    private String msg;
    private String model;


    SmsTemplateEnum(String code, String msg,String model) {
        this.code = code;
        this.msg = msg;
        this.model = model;
    }



    public static String getMsg(String code){
        for(var e : SmsTemplateEnum.values()){
            if(e.code .equals(code)){
                return e.msg;
            }
        }
        return "";
    }

    public static List<Data> getBymodel(String model){
        List<Data> list= new ArrayList<Data>();
        for(var e : SmsTemplateEnum.values()){
            if(e.model.indexOf(model)>=0){
                Data data=new Data();
                data.setCode(e.code);
                data.setMessage(e.msg);
                data.setModel(e.model);
                list.add(data);
            }
        }
        return list;
    }

    public static String getModel(String code){
        for(var e : SmsTemplateEnum.values()){
            if(e.code.equals(code)){
                return e.model;
            }
        }
        return "";
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {

        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
