package com.assessment.controller;

import com.assessment.core.base.BaseController;
import com.assessment.core.base.BaseService;
import com.assessment.domian.output.AppraisalPlanOutput;
import com.assessment.mapper.mybatis.AppraisalPlanMapper;
import com.assessment.model.AppraisalPlan;
import com.assessment.service.AppraisalAwardReportService;
import com.assessment.service.AppraisalPlanService;
import com.common.model.PageData;
import com.common.response.ResponseResult;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.assessment.core.base.BaseController.PARAM_EORRO;


/**
 * @author: XiGuoQing
 * @description:
 * @date: Created in 下午 2:55 2018/10/10 0010
 * @modified by:
 */
@RestController
@RequestMapping(value = "/appraisalAwardReport")
@Api(value = "考核奖报表controller",description = "考核奖报表操作",tags = {"考核奖报表操作接口"})
public class AppraisalAwardReportController extends BaseController<AppraisalPlanOutput, AppraisalPlan,Integer> {

  @Autowired
  private AppraisalAwardReportService appraisalAwardReportService;
  @Autowired
  private AppraisalPlanService appraisalPlanService;
  @Autowired
  private HttpServletRequest request;
  @Override
  public BaseService<AppraisalPlanOutput, AppraisalPlan, Integer> getService() {
    return appraisalPlanService;
  }

  /**
   * 获取分页的员工考核奖列表
   */
  @GetMapping(value = "findAppraisalAward")
  @ApiOperation("获取分页的员工考核奖报表")
  @ApiImplicitParams({
      @ApiImplicitParam(name="organizationId",value="组织机构id",required=false,dataType="string", paramType = "query"),
      @ApiImplicitParam(name="year",value="年",required=false,dataType="int", paramType = "query"),
      @ApiImplicitParam(name="month",value="月",required=false,dataType="int", paramType = "query")})
  public ResponseResult findAppraisalAward(){
    PageData pageData = new PageData(request);
    if(pageData.getMap().get("year")==null
        ||pageData.getMap().get("year").equals("")
        ||pageData.getMap().get("month")==null
        ||pageData.getMap().get("month").equals("")){
      return ResponseResult.error("获取员工考核奖失败");
    }
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
    return ResponseResult.success(appraisalAwardReportService.getAppraisalAward(pageData));

  }


  /**
   * 导出考核奖报表信息
   *
   * @param
   * @return
   */
  @ApiOperation("导出员工考核奖报表")
  @RequestMapping(value = "appraisalAwardReportExport", method = RequestMethod.GET)
  @ApiImplicitParams({
      @ApiImplicitParam(name="organizationId",value="组织机构id",required=false,dataType="string", paramType = "query"),
      @ApiImplicitParam(name="year",value="年",required=false,dataType="int", paramType = "query"),
      @ApiImplicitParam(name="month",value="月",required=false,dataType="int", paramType = "query")})
  public ResponseResult export(HttpServletResponse response, HttpServletRequest request) {
    PageData pageData = new PageData(request);
    if(pageData.getMap().get("year")==null
        ||pageData.getMap().get("year").equals("")
        ||pageData.getMap().get("month")==null
        ||pageData.getMap().get("month").equals("")){
      return ResponseResult.error("导出员工考核奖报表失败");
    }




    try {
      String str = appraisalAwardReportService.appraisalAwardReportExport(response,request);
      return  ResponseResult.success(str);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }


}
