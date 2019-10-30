package com.assessment.controller;

import com.assessment.core.base.BaseController;
import com.assessment.core.base.BaseService;
import com.assessment.domian.output.AppraisalPlanOutput;
import com.assessment.model.AppraisalPlan;
import com.assessment.service.AppraisalEmployeePlanService;
import com.common.model.PageData;
import com.common.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/appraisalemployeeplan")
@Api(value = "员工年度考核controller",description = "员工年度考核操作",tags = {"员工年度考核操作接口"})
public class AppraisalEmployeePlanController extends BaseController<AppraisalPlanOutput, AppraisalPlan,Integer> {


    @Autowired
    private AppraisalEmployeePlanService appraisalEmployeePlanService;
    @Override
    public BaseService<AppraisalPlanOutput, AppraisalPlan, Integer> getService() {
        return appraisalEmployeePlanService;
    }


    @GetMapping(value = "findPageList")
    @ApiImplicitParams({@ApiImplicitParam(name="year",value="年",required=false,dataType="int", paramType = "query")})
    public ResponseResult findPageList(HttpServletRequest request){
        PageData pageData = new PageData(request);
        if(!pageData.containsKey("year")){
            ResponseResult.error(PARAM_EORRO);
        }else {
            if(pageData.get("year").equals("")||pageData.get("year")==null){
                ResponseResult.error(PARAM_EORRO);
            }
        }
        return ResponseResult.success(appraisalEmployeePlanService.getEmployeeRecord(pageData.get("year").toString()));
    }
}
