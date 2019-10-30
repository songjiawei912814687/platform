package com.assessment.controller;

import com.assessment.core.base.BaseController;
import com.assessment.core.base.BaseService;
import com.assessment.domian.output.AppraisalPlanOutput;
import com.assessment.model.AppraisalPlan;
import com.assessment.service.AppraisalPlanService;
import com.common.model.PageData;
import com.common.response.ResponseResult;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;



@RestController
@RequestMapping(value = "/appraisalplan")
@Api(value = "考核计划controller",description = "考核计划操作",tags = {"考核计划接口"})
public class AppraisalPlanController extends BaseController<AppraisalPlanOutput, AppraisalPlan,Integer> {
    @Autowired
    private AppraisalPlanService appraisalPlanService;
    @Override
    public BaseService<AppraisalPlanOutput, AppraisalPlan, Integer> getService() {
        return appraisalPlanService;
    }


    @Override
    @ApiOperation("新增或修改考核计划")
    @RequestMapping(value = "form", method = RequestMethod.POST)
    public ResponseResult formPost(Integer id, @Validated @RequestBody AppraisalPlan appraisalPlan) throws Exception {
        if(id==null||id.equals("")||appraisalPlan.getFinalScore()==null||appraisalPlan.getFinalScore().equals("")){
            return ResponseResult.error(PARAM_EORRO);
        }
        AppraisalPlan appraisalPlan1=getService().getById(id);
        appraisalPlan1.setFinalScore(appraisalPlan.getFinalScore());
        return  super.formPost(id,appraisalPlan1);
    }

    @Override
    @GetMapping(value = "get")
    @ApiOperation(value="根据id获取单个计划")
    public ResponseResult get(Integer id) {
        return super.selectById(id);
    }


    @ApiOperation("手动修改部门考核计划得分")
    @RequestMapping(value = "changeFinalScore", method = RequestMethod.POST)
    public ResponseResult changeFinalScore(Integer id, Double finalScore) throws Exception {
        if(id==null||id.equals("")||finalScore==null||finalScore.equals("")){
            return ResponseResult.error(PARAM_EORRO);
        }
        return appraisalPlanService.updateFinalScoreById(id,finalScore);
    }

//员工考核考核计划列表
    @GetMapping(value = "findEmployeesPlan")
    @ApiOperation("获取考核计划列表")
    @ApiImplicitParams({@ApiImplicitParam(name="organizationId",value="组织机构id",required=false,dataType="string", paramType = "query"),
            @ApiImplicitParam(name="year",value="年",required=false,dataType="int", paramType = "query"),
            @ApiImplicitParam(name="month",value="月",required=false,dataType="int", paramType = "query"),
            @ApiImplicitParam(name="name",value="员工姓名",required=false,dataType="string", paramType = "query")})
    public ResponseResult findEmployeesPlan(HttpServletRequest request){
        PageData pageData = new PageData(request);
        if(pageData.getMap().get("year")==null
                ||pageData.getMap().get("year").equals("")
                ||pageData.getMap().get("month")==null
                ||pageData.getMap().get("month").equals("")){
            return ResponseResult.error("获取员工考核计划列表失败");
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
        return ResponseResult.success(new PageInfo<>(appraisalPlanService.getEmployeesPlan(pageData)));
    }



    //部门考核考核计划列表
    @GetMapping(value = "findDepartmentPlan")
    @ApiOperation("获取部门考核计划列表")
    @ApiImplicitParams({@ApiImplicitParam(name="organizationId",value="组织机构id",required=false,dataType="string", paramType = "query"),
            @ApiImplicitParam(name="year",value="年",required=false,dataType="int", paramType = "query"),
            @ApiImplicitParam(name="month",value="月",required=false,dataType="int", paramType = "query")})
    public ResponseResult findDepartmentPlan(HttpServletRequest request){
        PageData pageData = new PageData(request);
        Integer organizationId = (pageData.get("organizationId")==null||"".equals(pageData.get("organizationId")))?null:Integer.valueOf(pageData.get("organizationId").toString());
        Integer year = (pageData.get("year")==null||"".equals(pageData.get("year")))?null:Integer.valueOf(pageData.get("year").toString());
        Integer month = (pageData.get("month")==null||"".equals(pageData.get("month")))?null:Integer.valueOf(pageData.get("month").toString());
        if(year==null||(year>9999||year%1!=0)||(month!=null&&(month<1||month>12||month%1!=0))){
            return ResponseResult.error(PARAM_EORRO);
        }
        pageData.put("year",year);
        pageData.put("month",month);
        pageData.put("userId",appraisalPlanService.getUsers().getId().toString());
        if(appraisalPlanService.getUsers().getAdministratorLevel()!=9){
            if(appraisalPlanService.getUsers().getUserType()==0){
                pageData.put("employeeId",appraisalPlanService.getUsers().getEmployeeId().toString());
            }else {
                pageData.put("orgId",appraisalPlanService.getUsers().getOrganizationId().toString());
            }
        }
        if(organizationId!=null&&!"".equals(organizationId)){
            pageData.put("path",","+organizationId+",");
        }
        return ResponseResult.success(new PageInfo<>(appraisalPlanService.findDepartmentPlan(pageData)));
    }

    @GetMapping(value = "findDepartmentPlanWithNoPage")
    public ResponseResult findDepartmentPlanWithNoPage(HttpServletRequest request){
        PageData pageData = new PageData(request);
        Integer organizationId = (pageData.get("organizationId")==null||"".equals(pageData.get("organizationId")))?null:Integer.valueOf(pageData.get("organizationId").toString());
        Integer year = (pageData.get("year")==null||"".equals(pageData.get("year")))?null:Integer.valueOf(pageData.get("year").toString());
        Integer month = (pageData.get("month")==null||"".equals(pageData.get("month")))?null:Integer.valueOf(pageData.get("month").toString());
        if(year==null||(year>9999||year%1!=0)||(month!=null&&(month<1||month>12||month%1!=0))){
            return ResponseResult.error(PARAM_EORRO);
        }
        pageData.put("year",year);
        pageData.put("month",month);
        pageData.put("userId",appraisalPlanService.getUsers().getId().toString());
        if(appraisalPlanService.getUsers().getAdministratorLevel()!=9){
            if(appraisalPlanService.getUsers().getUserType()==0){
                pageData.put("employeeId",appraisalPlanService.getUsers().getEmployeeId().toString());
            }else {
                pageData.put("orgId",appraisalPlanService.getUsers().getOrganizationId().toString());
            }
        }
        if(organizationId!=null&&!"".equals(organizationId)){
            pageData.put("path",","+organizationId+",");
        }
        return ResponseResult.success(appraisalPlanService.findDepartmentPlanWithNoPage(pageData));
    }

    @ApiOperation("设置为服务明星")
    @GetMapping(value = "start")
    public ResponseResult start(String idList){
        if (idList == null || idList.length() <= 0) {
            return ResponseResult.error(PARAM_EORRO);
        }
        int result = 0;
        try {
            result = appraisalPlanService.updatestate(idList, 1);
        } catch (Exception e) {
            return ResponseResult.error(SYS_EORRO);
        }
        if (result < 0) {
            return ResponseResult.error(SYS_EORRO);
        }
        return ResponseResult.success();
    }
    @ApiOperation("取消服务明星")
    @GetMapping(value = "stop")
    public ResponseResult stop(String idList) {
        if (idList == null || idList.length() <= 0) {
            return ResponseResult.error(PARAM_EORRO);
        }
        int result = 0;
        try {
            result = appraisalPlanService.updatestate(idList, 0);
        } catch (Exception e) {
            return ResponseResult.error(SYS_EORRO);
        }
        if (result < 0) {
            return ResponseResult.error(SYS_EORRO);
        }
        return ResponseResult.success();
    }

}
