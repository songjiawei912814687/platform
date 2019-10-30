package com.assessment.service;


import com.assessment.core.base.BaseMapper;
import com.assessment.core.base.BaseService;
import com.assessment.core.base.MybatisBaseMapper;
import com.assessment.domian.output.AssessmentDepartReportOutput;
import com.assessment.domian.output.HchenStatisticalOutput;
import com.assessment.mapper.jpa.AssessmentDepartReportRepository;
import com.assessment.mapper.mybatis.AssessmentDepartReportMapper;
import com.assessment.mapper.mybatis.HchenStatisticalMapper;
import com.assessment.mapper.mybatis.OrganizationMapper;
import com.assessment.model.AssessmentDepartReport;
import com.assessment.model.Config;
import com.common.model.PageData;
import com.common.response.ResponseResult;
import com.common.utils.ExportExcel;
import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Young
 * @description:
 * @date: Created in 15:40 2018/10/11
 * @modified by:
 */
@Service("assessmentDepartReportService")
public class AssessmentDepartReportService extends BaseService<AssessmentDepartReportOutput, AssessmentDepartReport,Integer> {

    @Autowired
    private AssessmentDepartReportRepository assessmentDepartReportRepository;
    @Autowired
    private AssessmentDepartReportMapper assessmentDepartReportMapper;
    @Autowired
    private HchenStatisticalMapper hchenStatisticalMapper;
    @Autowired
    private OrganizationMapper organizationMapper;
    @Autowired
    private LoadBalancerClient loadBalancerClient;
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private ConfigService configService;

    @Override
    public BaseMapper<AssessmentDepartReport, Integer> getMapper() {
        return assessmentDepartReportRepository;
    }

    @Override
    public MybatisBaseMapper<AssessmentDepartReportOutput> getMybatisBaseMapper() {
        return assessmentDepartReportMapper;
    }

    /**
     * 根据年月的条件查询出鸿查数据中的组织名称和对应的查询次数
     * @param year 年
     * @param month 月
     * @return
     */
    public ResponseResult getCountList(Integer year,Integer month) {
        if(month.toString().length()>4||month.toString().length()>3){
            return ResponseResult.error("请输入正确的年份或者月份");
        }
        month = month+1;
        if(month> 12){
            year = year+1;
            month =1;
        }
        //根据用户传入的年和月获取鸿程数据的list集合
        List<HchenStatisticalOutput> hchenStatisticalOutputList = hchenStatisticalMapper.selectCountListByReportDate(year, month);
        //获取系统参数//hcMap: {区发改局(区物价局)=区发改局, 区档案局(馆)=区档案局, 公安富阳分局=公安富阳区分局, 区城管执法局=区城管局}
        List<Config> hcsj = configService.getListByKey("hcsj");
        Map<String,String> hcMap = new HashMap<>();
        for (Config config:hcsj) {
            hcMap.put(config.getConfigKey(),config.getConfigValue());
        }
        //遍历出得到的鸿程数据的集合
        for(HchenStatisticalOutput hchenStatisticalOutput : hchenStatisticalOutputList){
                //如果系统参数包含
            String departName ;
            if (hcMap.containsKey(hchenStatisticalOutput.getDepartment().trim())) {
                departName = hcMap.get(hchenStatisticalOutput.getDepartment().trim());
            }else {
                departName = hchenStatisticalOutput.getDepartment();
            }

            String orgaNo = organizationMapper.selectByName(departName.trim());
            Integer orgaId = organizationMapper.selectByOrgaNo(orgaNo);
            hchenStatisticalOutput.setOrganizationId(orgaId);
        }
        return ResponseResult.success(hchenStatisticalOutputList);
    }


    /**
     * 新增或者跟新方法
     * @param assessmentDepartReportList 部门考核报表集合
     * @param idList 主键id集合
     * @return
     * @throws InvocationTargetException
     * @throws IntrospectionException
     * @throws MethodArgumentNotValidException
     * @throws IllegalAccessException
     */
    public ResponseResult addOrUpdateGenerator(List<AssessmentDepartReport> assessmentDepartReportList, String idList) throws InvocationTargetException, IntrospectionException, MethodArgumentNotValidException, IllegalAccessException {
        List<Integer> resultList = Lists.newArrayList();
        //新增方法
        if (idList == null) {
            for (AssessmentDepartReport assessmentDepartReport : assessmentDepartReportList) {

                Integer result = this.add(assessmentDepartReport);
                resultList.add(result);
            }
            return ResponseResult.success(resultList);
        }
        //跟新方法
        var strs = idList.split(",");
        for (var str : strs) {
            var id = Integer.parseInt(str);
            for (AssessmentDepartReport assessmentDepartReportItem : assessmentDepartReportList) {
                AssessmentDepartReport assessmentDepartReport = getMapper().getById(id);
                BeanUtils.copyProperties(assessmentDepartReportItem, assessmentDepartReport);
                Integer result = this.update(id, assessmentDepartReport);
                resultList.add(result);
            }
            return  ResponseResult.success(resultList);
        }
        return ResponseResult.error("新增或者跟新方法错误");
    }

    /**
     * 条件搜索
     * @param pageData 搜索条件
     * @return
     */
    public ResponseResult findDepartList(PageData pageData){
        //先获得权限内的部门考核报表
        if(getUsers().getAdministratorLevel()!=9){
            pageData.put("userId",getUsers().getId());
            if(getUsers().getUserType()==1){//部门账号为部门本身和部门账号权限内的组织
                pageData.put("orgId",getUsers().getOrganizationId());
            }
        }
        List<AssessmentDepartReportOutput> assessmentDepartReportOutputList = assessmentDepartReportMapper.selectDepartReportByCondition(pageData);
 /*       List<AssessmentDepartReportOutput> averagessessmentDepartReportOutputList = assessmentDepartReportMapper.selectAverageDepartReportByCondition(pageData);
        HashMap map = new HashMap<String,AssessmentDepartReportOutput>();
        for (AssessmentDepartReportOutput o :averagessessmentDepartReportOutputList) {
            map.put(o.getOrganizationCode(),o);
        }*/
        // doThings windowAccept  onlineReporting  matterSeec  dataCompaction  intermediaryService  peopleSatisfaction  jobLogging
        // transactNorm  innovationWork  mouthIndex
        for (AssessmentDepartReportOutput output:assessmentDepartReportOutputList) {
//            if(map.containsKey(output.getOrganizationCode())){
//                AssessmentDepartReportOutput output1 =  (AssessmentDepartReportOutput)map.get(output.getOrganizationCode());
                output.setDoThings(output.getDoThings()==null?null:output.getDoThings().setScale(2, BigDecimal.ROUND_HALF_UP));
                output.setWindowAccept(output.getWindowAccept()==null?null:output.getWindowAccept().setScale(2, BigDecimal.ROUND_HALF_UP));
                output.setOnlineReporting(output.getOnlineReporting()==null?null:output.getOnlineReporting().setScale(2, BigDecimal.ROUND_HALF_UP));
                output.setMatterSeec(output.getMatterSeec()==null?null:output.getMatterSeec().setScale(2, BigDecimal.ROUND_HALF_UP));
                output.setDataCompaction(output.getDataCompaction()==null?null:output.getDataCompaction().setScale(2, BigDecimal.ROUND_HALF_UP));
                output.setIntermediaryService(output.getIntermediaryService()==null?null:output.getIntermediaryService().setScale(2, BigDecimal.ROUND_HALF_UP));
                output.setPeopleSatisfaction(output.getPeopleSatisfaction()==null?null:output.getPeopleSatisfaction().setScale(2, BigDecimal.ROUND_HALF_UP));
                output.setJobLogging(output.getJobLogging()==null?null:output.getJobLogging().setScale(2, BigDecimal.ROUND_HALF_UP));
                output.setTransactNorm(output.getTransactNorm()==null?null:output.getTransactNorm().setScale(2, BigDecimal.ROUND_HALF_UP));
                output.setInnovationWork(output.getInnovationWork()==null?null:output.getInnovationWork().setScale(2, BigDecimal.ROUND_HALF_UP));
                output.setMouthIndex(output.getMouthIndex()==null?null:output.getMouthIndex().setScale(2, BigDecimal.ROUND_HALF_UP));
//            }
        }
        return ResponseResult.success(assessmentDepartReportOutputList);
    }

    public ResponseResult exportExcel(HttpServletRequest request,HttpServletResponse response){
        String title = "总表";
        String excelName = "总表";
        List<Object[]> dataList = Lists.newArrayList();
        Object[] objs ;
        PageData pageData=new PageData(request);
        List<AssessmentDepartReportOutput> assessmentDepartReportOutputList = assessmentDepartReportMapper.selectDepartReportByCondition(pageData);
        String[]rowsName = {"序列","单位名称","人均办件量","一窗受理率","网上申报率","事项联办率","资料精简率","中介服务效率","群众满意率","工作纪律","办理规范","创新工作","指标总和"};
        for(int i=0;i<assessmentDepartReportOutputList.size();i++){
            AssessmentDepartReportOutput assessmentDepartReportOutput = assessmentDepartReportOutputList.get(i);
            objs = new Object[rowsName.length];
            objs[0] = i;
            objs[1] = assessmentDepartReportOutput.getOrganizationName();
            objs[2] = assessmentDepartReportOutput.getDoThings();
            objs[3] = assessmentDepartReportOutput.getWindowAccept();
            objs[4] = assessmentDepartReportOutput.getOnlineReporting();
            objs[5] = assessmentDepartReportOutput.getMatterSeec();
            objs[6] = assessmentDepartReportOutput.getDataCompaction();
            objs[7] = assessmentDepartReportOutput.getIntermediaryService();
            objs[8] = assessmentDepartReportOutput.getPeopleSatisfaction();
            objs[9] = assessmentDepartReportOutput.getJobLogging();
            objs[10] = assessmentDepartReportOutput.getTransactNorm();
            objs[11] = assessmentDepartReportOutput.getInnovationWork();
            objs[12] = assessmentDepartReportOutput.getMouthIndex();

            dataList.add(objs);
        }
        ExportExcel exportExcel = new ExportExcel(title,rowsName,dataList,excelName);
        try{
            String excelpath =  exportExcel.export(response, request);
            return ResponseResult.success(excelpath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}


