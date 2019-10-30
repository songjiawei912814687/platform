package com.message.service;

import com.google.common.collect.Lists;
import com.message.mapper.jpa.QlsxRepository;
import com.message.model.Qlsx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

/**
 * @author: young
 * @description:
 * @date: Created in 2019-07-24  10:04
 */
@Service
public class QlsxService {

    @Autowired
    private QlsxRepository qlsxRepository;

    private final String className = "com.mysql.jdbc.Driver";

    private final String url = "jdbc:mysql://10.32.250.45:3306/qlsx_fy?characterEncoding=utf-8&useSSL=false";

    @Value("${spring.datasource.qlsx.username}")
    private String user;

    @Value("${spring.datasource.qlsx.password}")
    private String password;

    /**同步权力事项库源代码*/
    @Transactional(rollbackFor = Exception.class)
    public Integer sysQlsxSource() throws Exception {
        String sql = "SELECT tongID,ROWGUID,UPDATE_DATE,UPDATE_TYPE,QL_KIND,QL_Full_ID,QL_MAINITEM_ID,QL_SUBITEM_ID,ITEMSOURCE,OUGUID,QL_NAME ," +
                "QL_STATE,LAWBASIS,ANTICIPATE_DAY,ANTICIPATE_TYPE,PROMISE_DAY,FeeBasis,APPLYERMIN_COUNT,QL_DEP,Acp_institution," +
                "Dec_institution,LEAD_DEPT,BJTYPE,Content_involve,XINGZHENXDRXZ,Applicable_object,APPLY_CONDITION," +
                "Count_limit,Count_note,Ban_requirement,SHIXIANGSCtype,SHIXIANGSCLX,BANJIAN_FINISHFILES," +
                "LINK_TEL,SUPERVISE_TEL,Apply_type,Apply_type_tel,Apply_type_mail,Apply_type_fax,Handle_type, " +
                "WEBAPPLYURL,WEBCONSULTURL,CHARGE_FLAG,CHARGE_BASIS,HangYeClassType,QL_Sub_Kind,IM_FLOW_url," +
                "OUT_FLOW_url,MATERIAL_INFO,CHARGEITEM_INFO,QA_INFO,ACCEPT_ADDRESS_INFO,SYNC_DATE,SYNC_ERROR_DESC," +
                "Service_mode, Service_day,FaRenUrl,GeRenFlag,APPWEBAPPLYURL,APPAPPOINTMENTURL,APPOINTMENTURL," +
                "IS_WEBAPPOINTMENT,WEBAPPOINTMENTPERIOD,IS_EXPRESS,ISPYC,MbFarenAdd,MbGerenFlag,MbGerenFlag," +
                "Bak6,Bak7,Bak8," +
                "OUT_FLOW_DESC FROM qlt_qlsx";

        ResultSet re = null;
        Connection conn = null ;
        Statement statement = null;


        try {

            Class.forName(className);
            conn= DriverManager.getConnection(url,user,password);
            statement = conn.createStatement();
            re = statement.executeQuery(sql);

            List<Qlsx> qlsxList = Lists.newArrayList();
            while (re.next()){
                Qlsx qlsx = new Qlsx();
                qlsx.setTongId(re.getInt("tongID"));
                qlsx.setRowguid(re.getString("ROWGUID"));
                qlsx.setUpdateDate(re.getTimestamp("UPDATE_DATE"));
                qlsx.setUpdateType(re.getString("UPDATE_TYPE"));
                qlsx.setQlKind(re.getString("QL_KIND"));
                qlsx.setQlFullId(re.getString("QL_Full_ID"));
                qlsx.setQlMainitemId(re.getString("QL_MAINITEM_ID"));
                qlsx.setQlSubitemId(re.getString("QL_SUBITEM_ID"));
                qlsx.setItemsource(re.getString("ITEMSOURCE"));
                qlsx.setOuguid(re.getString("OUGUID"));
                qlsx.setQlName(re.getString("QL_NAME"));
                qlsx.setQlState(re.getString("QL_STATE"));
                qlsx.setLawbasis(re.getString("LAWBASIS"));
                qlsx.setAnticipateDay(re.getInt("ANTICIPATE_DAY"));
                qlsx.setAnticipateType(re.getString("ANTICIPATE_TYPE"));
                qlsx.setPromiseDay(re.getInt("PROMISE_DAY"));
                qlsx.setFeeBasis(re.getString("FeeBasis"));
                qlsx.setApplyerminCount(re.getInt("APPLYERMIN_COUNT"));
                qlsx.setQlDep(re.getString("QL_DEP"));
                qlsx.setAcpInstitution(re.getString("Acp_institution"));
                qlsx.setDecInstitution(re.getString("Dec_institution"));
                qlsx.setLeadDept(re.getString("LEAD_DEPT"));
                qlsx.setBjtype(re.getString("BJTYPE"));
                qlsx.setContentInvolve(re.getString("Content_involve"));
                qlsx.setXingzhenxdrxy(re.getString("XINGZHENXDRXZ"));
                qlsx.setApplicableObject(re.getString("Applicable_object"));
                qlsx.setApplyCondition(re.getString("APPLY_CONDITION"));
                qlsx.setCountLimit(re.getString("Count_limit"));
                qlsx.setCountNote(re.getString("Count_note"));
                qlsx.setBanRequirement(re.getString("Ban_requirement"));
                qlsx.setShixiangsctype(re.getString("SHIXIANGSCtype"));
                qlsx.setShixiangsclx(re.getString("SHIXIANGSCLX"));
                qlsx.setBanjianFinishfiles(re.getString("BANJIAN_FINISHFILES"));
                qlsx.setLinkTel(re.getString("LINK_TEL"));
                qlsx.setSuperviseTel(re.getString("SUPERVISE_TEL"));
                qlsx.setApplyType(re.getString("Apply_type"));
                qlsx.setApplyTypeFax(re.getString("Apply_type_fax"));
                qlsx.setHandleType(re.getString("Handle_type"));
                qlsx.setApplyTypeTel(re.getString("Apply_type_tel"));
                qlsx.setApplyTypeMail(re.getString("Apply_type_mail"));
                qlsx.setWebapplyurl(re.getString("WEBAPPLYURL"));
                qlsx.setWebconsulturl(re.getString("WEBCONSULTURL"));
                qlsx.setChargeFlag(re.getString("CHARGE_FLAG"));
                qlsx.setChargeBasis(re.getString("CHARGE_BASIS"));
                qlsx.setHangYeClassType(re.getString("HangYeClassType"));
                qlsx.setQlSubKind(re.getString("QL_Sub_Kind"));
                qlsx.setImFlowUrl(re.getString("IM_FLOW_url"));
                qlsx.setOutFlowUrl(re.getString("OUT_FLOW_url"));
                qlsx.setMaterialInfo(re.getString("MATERIAL_INFO"));
                qlsx.setChargeitemInfo(re.getString("CHARGEITEM_INFO"));
                qlsx.setQaInfo(re.getString("QA_INFO"));
                qlsx.setAcceptAddressInfo(re.getString("ACCEPT_ADDRESS_INFO"));
                qlsx.setSyncDate(re.getTimestamp("SYNC_DATE"));
                qlsx.setSyncErrorDesc(re.getString("SYNC_ERROR_DESC"));
                qlsx.setServiceMode(re.getString("Service_mode"));
                qlsx.setServiceDay(re.getString("Service_day"));
                qlsx.setOutFlowDesc(re.getString("OUT_FLOW_DESC"));
                qlsx.setFarenurl(re.getString("FaRenUrl"));
                qlsx.setGeRenFlag(re.getString("GeRenFlag"));
                qlsx.setAppwebapplyurl(re.getString("APPWEBAPPLYURL"));
                qlsx.setAppappointmenturl(re.getString("APPAPPOINTMENTURL"));
                qlsx.setAppointmenturl(re.getString("APPOINTMENTURL"));
                qlsx.setIsWebappointment(re.getString("IS_WEBAPPOINTMENT"));
                qlsx.setWebappointmentperiod(re.getString("WEBAPPOINTMENTPERIOD"));
                qlsx.setIsExpress(re.getString("IS_EXPRESS"));
                qlsx.setIspyc(re.getString("ISPYC"));
                qlsx.setMbfarenadd(re.getString("MbFarenAdd"));
                qlsx.setMbgerenflag(re.getString("MbGerenFlag"));
                qlsx.setBak6(re.getString("Bak6"));
                qlsx.setBak7(re.getString("Bak7"));
                qlsx.setBak8(re.getString("Bak8"));
                qlsxList.add(qlsx);
            }
            Integer size = qlsxRepository.saveAll(qlsxList).size();
            return size;
        } finally {
            re.close();
            conn.close();
            statement.close();
        }
    }
}
