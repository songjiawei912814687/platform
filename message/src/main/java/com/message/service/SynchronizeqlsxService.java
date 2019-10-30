package com.message.service;

import com.common.response.ResponseResult;
import com.message.core.Thread.QltqlsxWork;
import com.message.model.QltQlsx;
import com.message.util.DbUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.sql.SQLException;
import java.util.List;

/**
 * @author: Young
 * @description:
 * @date: Created in 18:35 2018/10/23
 * @modified by:
 */
@Configuration
@Service
public class SynchronizeqlsxService {

    // 数据库连接地址
    @Value("${spring.datasource.qlsx.dbName}")
    public String dbName;

    // 数据库连接地址
    @Value("${spring.datasource.qlsx.ip}")
    public String dbIp;
    // 用户名
    @Value("${spring.datasource.qlsx.username}")
    public String dbUser;
    // 密码
    @Value("${spring.datasource.qlsx.password}")
    public String dbPassword;
    // mysql的驱动类
    @Value("${spring.datasource.qlsx.port}")
    public String dbPort;

    @Autowired
    private QltqlsxWork qltqlsxWork;


    public ResponseResult synchronizeDate() throws SQLException {
        DbUtil dbUtil = new DbUtil();
        var count = 0;
        Long star = System.currentTimeMillis();
        //从第几条数据开始取多少条数据
        for (int i = 0; i >= 0; i += 1000) {
            String sql = "\n" +
                    "select a.* from(\n"+
                    "SELECT\n" +
                    "\tQL_INNER_CODE,\n" +// 权力内部编码
                    "\tQL_Full_ID,\n" +// 权力编码
                    "\tQL_STATE,\n" +// 权力状态
                    "\tQL_MAINITEM_ID,\n"+ //主项编码
                    "\tQL_SUBITEM_ID,\n"+//子项编码
                    "\tQL_NAME,\n" + //权利名称
                    "\tBJTYPE,\n" +//办件类型
                    "\tContent_involve,\n" +// 涉及内容
                    "\tApplicable_object,\n" +// 适用对象
                    "\tQL_KIND,\n" +// 权力事项类型
                    "\tITEMSOURCE,\n" +// 权力来源
                    "\tWEBAPPLYURL,\n"+//网上办理地址
                    "\tAcp_institution,\n" +// 受理机构
                    "\tDec_institution,\n" +// 决定机构
                    "\tLEAD_DEPT,\n" +// 责任科室
                    "\tSHIXIANGSCtype,\n" +// 事项审查类型
                    "\tApply_type,\n" +// 申请方式
                    "\tApply_type_tel,\n" +// 联系电话
                    "\tACCEPT_ADDRESS_INFO,\n" +// 受理地点信息
                    "\tLINK_TEL,\n" +// 咨询电话
                    "\tSUPERVISE_TEL,\n" +// 监督投诉电话
                    "\tBANJIAN_FINISHFILES,\n" +// 审批结果
                    "\tPROMISE_DAY,\n" +// 承诺期限
                    "\tANTICIPATE_DAY,\n" +// 法定期限
                    "\tANTICIPATE_TYPE,\n" +// 法定期限单位
                    "\tService_mode,\n" +// 送达方式
                    "\tService_day,\n" +// 送达时限
                    "\tAPPLYERMIN_COUNT,\n" +// 办事者到办事地点最少次数
                    "\tAPPLY_CONDITION,\n" +// 审批条件
                    "\tCount_limit,\n" +// 有无数量限制
                    "\tBan_requirement,\n" +// 禁止性要求
                    "\tRELATED,\n" +// 相关附件信息
                    "\tOUT_FLOW_DESC,\n" +// 办理程序
                    "\tOUT_FLOW_url,\n" +// 内部流程图
                    "\tCHARGE_FLAG,\n" +// 是否收费
                    "\tCHARGE_BASIS,\n" +// 收费依据
                    "\tCHARGEITEM_INFO,\n" +// 收费项目
                    "\tLAWBASIS,\n" +// 法定依据
                    "\tXINGZHENXDRXY,\n" +// 事项者权利和义务
                    "\tXINGZHENXDRXZ,\n" +// 行政相对人性质（适用对象）
                    "\tQA_INFO,\n" +// 常见问题解答
                    "\tMATERIAL_INFO,\n" +// 业务申报材料
                    "\tHangYeClassType,\n" +//行业类型
                    "\tIM_FLOW_url,\n" +//内部流程图
                    "\tROWGUID,\n" +//权力唯一标识
                    "\tbusiness_regulate,\n" +//业务审查规范
                    "\tUPDATE_DATE,\n" +
                    "OUORGCODE,\n"+ //部门编码(废弃)
                    "OUGUID,\n"+ //统一部门组织编码（正式使用）
                    "tongID\n" +
                    "FROM\n" +
                    "\tqlt_qlsx\n" +
                    "where \n" +
                    "QL_KIND  IN (\n" +
                    "\t'01',\n" +
                    "\t'08',\n" +
                    "\t'10',\n" +
                    "\t'13',\n" +
                    "\t'14'\n" +
                    "\t) \n" +
                    "\tAND QL_STATE = 1\n" +
                    "\tAND ROWGUID NOT LIKE '%测试%' \n" +
                    "AND QL_Full_ID is not null\n" +
                    "AND QL_Full_ID != ''\n" +
                    "AND ACCEPT_ADDRESS_INFO is not null\n" +
                    "AND ACCEPT_ADDRESS_INFO != ''\n" +
                    "AND MATERIAL_INFO is not null\n" +
                    "AND MATERIAL_INFO != ''\n" +
                    "AND CHARGEITEM_INFO is not null\n" +
                    "AND CHARGEITEM_INFO != ''\n" +
                    "AND QA_INFO is not null\n" +
                    "AND QA_INFO != ''\n" +
                    "order BY\n" +
                    "\tUPDATE_DATE desc" +
                    ") a " +
                    "group by a.QL_INNER_CODE " +
                    "  LIMIT " + i + ",1000";
            List<QltQlsx> qltQlsxList = dbUtil.queryList(dbIp, dbPort, dbName, dbUser, dbPassword, sql, QltQlsx.class);
            if (CollectionUtils.isEmpty(qltQlsxList)) {
                return ResponseResult.success("同步完成,耗时:"+(System.currentTimeMillis() - star)/(1000*3600)+"分");
            }
                qltqlsxWork.saveQltQlsx(qltQlsxList);
                qltqlsxWork.saveDetail(qltQlsxList);
//            qltqlsxWork.updateMinimum(qltQlsxList);
        }
        return ResponseResult.success("同步完成,耗时:"+(System.currentTimeMillis() - star)/(1000*3600)+"分");
    }
}
