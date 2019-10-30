package com.assessment.service;

import com.assessment.core.base.BaseMapper;
import com.assessment.core.base.BaseService;
import com.assessment.core.base.MybatisBaseMapper;
import com.assessment.domian.output.AppraisalAwardReportOutput;
import com.assessment.domian.output.AppraisalPlanOutput;
import com.assessment.mapper.jpa.AppraisalPlanRepository;
import com.assessment.mapper.mybatis.AppraisalPlanMapper;
import com.assessment.model.AppraisalPlan;
import com.common.model.PageData;
import com.common.request.GetConfig;
import com.common.utils.ExportExcel;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
   * 考核奖报表业务层逻辑
   */
  @Service
  public class AppraisalAwardReportService extends BaseService<AppraisalPlanOutput, AppraisalPlan,Integer> {

    //日志打印属性
    private final Logger log = LoggerFactory.getLogger(AppraisalAwardReportService.class);

    @Autowired
    private AppraisalPlanService appraisalPlanService;

    @Autowired
    private AppraisalPlanMapper appraisalPlanMapper;
    @Autowired
    private AppraisalPlanRepository appraisalPlanRepository;
    @Autowired
    private LoadBalancerClient loadBalancerClient;
    @Autowired
    private HttpServletRequest request;

   @Override
   public BaseMapper<AppraisalPlan, Integer> getMapper() {
    return appraisalPlanRepository;
   }

   @Override
   public MybatisBaseMapper<AppraisalPlanOutput> getMybatisBaseMapper() {
     return appraisalPlanMapper;
   }

    public List<AppraisalAwardReportOutput> getAppraisalAward(PageData pageData)  {
//      pageData.put("userId",getUsers().getId().toString());
//      if(getUsers().getAdministratorLevel()!=9){
//        if(getUsers().getUserType()==0){
//          pageData.put("employeeId",getUsers().getEmployeeId().toString());
//        }else {
//          pageData.put("orgId",getUsers().getOrganizationId().toString());
//        }
//      }
      List<AppraisalPlanOutput> list = appraisalPlanMapper.selectEmployees(pageData);
      List<AppraisalAwardReportOutput> awardReportOutputList = Lists.newArrayList();
      try{
        pageData = new PageData(request);
        pageData.put("key","asi");
        var map = GetConfig.getListByKey(loadBalancerClient,pageData);
        for (AppraisalPlanOutput appraisalPlanOutput : list) {
          AppraisalAwardReportOutput appraisalAwardReportOutput = new AppraisalAwardReportOutput();
          appraisalAwardReportOutput.setOrganizationName(appraisalPlanOutput.getOrganizationName());
          appraisalAwardReportOutput.setEmployeeName(appraisalPlanOutput.getEmployeeName());
//          appraisalAwardReportOutput.setWindowNoOrOffice(appraisalPlanOutput.getWindowOrOfficeName());
          appraisalAwardReportOutput.setBankCardNumber(appraisalPlanOutput.getBankCardNumber());
          appraisalAwardReportOutput.setDatumValue(appraisalPlanOutput.getDatumValue());
          appraisalAwardReportOutput.setFinalScore(appraisalPlanOutput.getFinalScore());
          appraisalAwardReportOutput.setAppraisalAward(Integer.valueOf(map.get("appraisalDefaultAward")));

          BigDecimal b1 = appraisalPlanOutput.getFinalScore();
          BigDecimal b2 = appraisalPlanOutput.getDatumValue();
          BigDecimal b3;
          BigDecimal b;
          if(b1 != null && !b1.equals("") && b2 != null && !b2.equals("")){
            b3 = b1.subtract(b2);
            b = b3.multiply(new BigDecimal(Integer.valueOf(map.get("scoreDefaultAward"))));
            appraisalAwardReportOutput.setAppraisalAward(Integer.valueOf(map.get("appraisalDefaultAward")) + b.intValue());
            if(appraisalAwardReportOutput.getAppraisalAward()<0){
              appraisalAwardReportOutput.setAppraisalAward(0);
            }
          }

          //是服务明星，默认值90，如果不是服务明星，值为0
          if (appraisalPlanOutput.getIsStar() != null) {
            if (appraisalPlanOutput.getIsStarName().equals("否") || appraisalPlanOutput.getIsStarName() ==""){
              appraisalAwardReportOutput.setIsStar(0);
              appraisalAwardReportOutput.setTotal(appraisalAwardReportOutput.getAppraisalAward());
            }else {
              appraisalAwardReportOutput.setIsStar(Integer.valueOf(map.get("isStarDefaultAward")));
              appraisalAwardReportOutput.setTotal(appraisalAwardReportOutput.getAppraisalAward() + appraisalAwardReportOutput.getIsStar());
            }
          }
          Integer i = appraisalAwardReportOutput.getAppraisalAward();
          if (i != null) {
            if (i > 300) {
              appraisalAwardReportOutput.setDescription("+" + b1.subtract(b2) + "分");
            } else if (i < 300) {
              appraisalAwardReportOutput.setDescription(b1.subtract(b2) + "分");
            } else {
              appraisalAwardReportOutput.setDescription("");
            }
          }
          awardReportOutputList.add(appraisalAwardReportOutput);
        }
      }catch(Exception e){
        e.printStackTrace();
      }
      return awardReportOutputList;
    }


    /**
     *
     * @param response
     * @param request
     * @return
     */
    public String appraisalAwardReportExport(HttpServletResponse response, HttpServletRequest request) throws Exception {
      String title = "员工考核奖报表";
      String excelName = "员工考核奖报表";
      String[] rowsName = new String[]{"序号","单位","窗口/后台","姓名","市名卡银行卡卡号","考核奖","服务明星","合计","备注"};
      List<Object[]> dataList = new ArrayList<Object[]>();
      Object[] objects = null;
      PageData pageData=new PageData(request);

      if(pageData.getMap().get("organizationId")!=null&&!pageData.getMap().get("organizationId").equals("")){
        String  s=appraisalPlanService.getPathById(Integer.parseInt(pageData.getMap().get("organizationId")));
        pageData.put("path",s+",");
      }
      pageData.put("userId",appraisalPlanService.getUsers().getId().toString());
      if(appraisalPlanService.getUsers().getAdministratorLevel()!=9){
        if(appraisalPlanService.getUsers().getUserType()==0){
          pageData.put("employeeId",appraisalPlanService.getUsers().getEmployeeId().toString());
        }else {
          pageData.put("orgId",appraisalPlanService.getUsers().getOrganizationId().toString());
        }
      }

      List<AppraisalAwardReportOutput> list=getAppraisalAward(pageData);
      if(list.size()>0){
        int i=1;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        for(AppraisalAwardReportOutput appraisalAwardReportOutput:list){
          objects = new Object[rowsName.length];
          objects[0]=i;
          objects[1]=appraisalAwardReportOutput.getOrganizationName();
          objects[2]=appraisalAwardReportOutput.getWindowNoOrOffice();
          objects[3]=appraisalAwardReportOutput.getEmployeeName();
          objects[4]=appraisalAwardReportOutput.getBankCardNumber();
          objects[5]=appraisalAwardReportOutput.getAppraisalAward();
          objects[6]=appraisalAwardReportOutput.getIsStar();
          objects[7]=appraisalAwardReportOutput.getTotal();
          objects[8]=appraisalAwardReportOutput.getDescription();
          dataList.add(objects);
          i++;
        }
      }
      ExportExcel excel = new ExportExcel(title, rowsName, dataList, excelName);
      return excel.export(response,request);
    }


    public String getPathById(int id) {
      return appraisalPlanMapper.selectPathById(id);
    }
}
