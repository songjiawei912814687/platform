package com.assessment.service;

import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleCreateTableStatement;
import com.assessment.core.util.DbUtil;
import com.assessment.domian.output.AppraisalOrganizationOutput;
import com.assessment.mapper.jpa.PreApasInfoRepository;
import com.assessment.mapper.mybatis.OrganizationMapper;
import com.assessment.mapper.mybatis.PreApasInfoMapper;
import com.assessment.model.Config;
import com.assessment.model.PreApasInfo;
import com.common.model.PageData;
import com.common.request.GetConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
@Configuration
public class SynchronizeBjsbService {
    // 数据库连接地址
    @Value("${spring.datasource.bjsb.dbName}")
    public  String dbName;

    // 数据库连接地址
    @Value("${spring.datasource.bjsb.ip}")
    public  String dbIp;
    // 用户名
    @Value("${spring.datasource.bjsb.username}")
    public  String dbUser;
    // 密码
    @Value("${spring.datasource.bjsb.password}")
    public  String dbPassword;
    // mysql的驱动类
    @Value("${spring.datasource.bjsb.port}")
    public  String dbPort;

    @Autowired
    private PreApasInfoMapper preApasInfoMapper;

    @Autowired
    private PreApasInfoRepository preApasInfoRepository;

    @Autowired
    private OrganizationMapper organizationMapper;

    @Autowired
    private ConfigService configService;

    public List<PreApasInfo> synchronizeDate() throws Exception{
        DbUtil dbUtil = new DbUtil();
        String sql = "SELECT * FROM pre_apasinfo WHERE APPLYFROM=1 AND DATASTATE=1 AND SYNC_STATUS!='D'";
        List<PreApasInfo> preApasInfos = dbUtil.queryList(dbIp, dbPort, dbName, dbUser, dbPassword, sql, PreApasInfo.class);
        List<Config> list = configService.getListByKey("bjsb");
        HashMap<String, String> map = new HashMap<>();
        for (Config config:list) {
            map.put(config.getConfigKey(),config.getConfigValue());
        }
        //从本系统里获得
        List<String> infos = preApasInfoMapper.getUnionVal();
       //从本系统获得组织名称和组织id
        List<AppraisalOrganizationOutput> orgs = organizationMapper.getNameAndId();
        for (PreApasInfo preApasInfo:preApasInfos) {
            if(!this.appear(infos,preApasInfo)){
                if(preApasInfo.getDeptName()!=null){
                    String deptName = "";
                    if(map.containsKey(preApasInfo.getDeptName())){
                        deptName = map.get(preApasInfo.getDeptName());
                    }else{
                        deptName = preApasInfo.getDeptName();
                    }
                    AppraisalOrganizationOutput org = this.getIdByName(orgs,deptName);
                    if(org!=null){
                        preApasInfo.setOrganizationId(org.getOrganizationId());
                        preApasInfo.setOrganizationName(org.getOrganizationName());
                    }
                }
                preApasInfoRepository.save(preApasInfo);
            }
        }
        return preApasInfos;
    }

    private AppraisalOrganizationOutput getIdByName(List<AppraisalOrganizationOutput> orgs, String deptName) {
        if(deptName==null||deptName.equals("")){
            return  null;
        }
        for (AppraisalOrganizationOutput org:orgs) {
            if(org.getOrganizationName().equals(deptName)){
                return org;
            }
        }
        return null;
    }

    private boolean appear(List<String> infos, PreApasInfo preApasInfo) {
        String unionVal = "";
        if(preApasInfo.getTongID()!=null&&preApasInfo.getProjId()!=null){
            unionVal = preApasInfo.getTongID()+preApasInfo.getProjId();
        }
        for (String str :infos) {
            if (str!=null&&!"".equals(str)){
                if(str.equals(unionVal)){
                    return true;
                }
            }
        }
        return false;
    }
}
