package com.assessment.controller;

import com.assessment.core.base.BaseController;
import com.assessment.core.base.BaseService;
import com.assessment.core.util.AppConsts;
import com.assessment.domian.input.TemplateIndexRuleInput;
import com.assessment.domian.output.AppraisalOrganizationOutput;
import com.assessment.domian.output.AppraisalTemplateOutput;
import com.assessment.domian.output.TemplateSetting;
import com.assessment.model.AppraisalTemplate;
import com.assessment.model.Role;
import com.assessment.model.Users;
import com.assessment.service.AppraisalTemplateService;
import com.common.model.PageData;
import com.common.response.ResponseResult;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;


@RestController
@RequestMapping(value = "/appraisalTemplate")
@Api(value = "考核模板controller",description = "考核模板操作",tags = {"考核模板操作接口"})
public class AppraisalTemplateController extends BaseController<AppraisalTemplateOutput, AppraisalTemplate,Integer> {

    @Autowired
    private AppraisalTemplateService appraisalTemplateService;

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Autowired
    private HttpServletRequest request;

    @Override
    public BaseService<AppraisalTemplateOutput,AppraisalTemplate, Integer> getService() {
        return appraisalTemplateService;
    }

    @ApiOperation("新增或修改考核模板")
    @RequestMapping(value = "formPost", method = RequestMethod.POST)
    public ResponseResult formPost(Integer id, String objectIdList, @Validated @RequestBody @ApiParam(name="考核模板",value="传入json格式",required=true) AppraisalTemplate appraisalTemplate) throws Exception {
        //指标名称需要做唯一性验证
        boolean isRepeat = appraisalTemplateService.nameIsRepeat(id,appraisalTemplate);
        if(isRepeat){
            return ResponseResult.error("考核模板名称不能重复");
        }
        if (appraisalTemplate == null) {
            return ResponseResult.error(PARAM_EORRO);
        }
        int result = 0;
        if (id == null) {
            if(!appraisalTemplateService.isAbleToAdd(appraisalTemplate)){
                return ResponseResult.error("员工考核模板只能启用一条，请将启用中的员工考核模板停用继续添加");
            }
            result = appraisalTemplateService.add(appraisalTemplate,objectIdList);
        } else {
            result = appraisalTemplateService.update(id, appraisalTemplate,objectIdList);
        }

        if (result < 0) {
            return ResponseResult.error(SYS_EORRO);
        }
        return ResponseResult.success();
    }

    @Override
    @ApiOperation("删除考核模板")
    @GetMapping(value = "logicDelete")
    public ResponseResult logicDelete(String id) throws IllegalAccessException, IntrospectionException, InvocationTargetException, MethodArgumentNotValidException {
        return super.logicDelete(id);
    }


    @ApiOperation("获取所有考核模板")
    @GetMapping(value = "selectAll")
    public ResponseResult selectAll(HttpServletRequest request){
        return  super.selectAll(new PageData(request));
    }

    @GetMapping(value = "findPageList")
    @ApiOperation("获取分页的考核模板列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="name",value="模板名称",required=false,dataType="string", paramType = "query"),
            @ApiImplicitParam(name="type",value="模板类型",required=false,dataType="int", paramType = "query"),
            @ApiImplicitParam(name="objectType",value="对象类型",required=false,dataType="int", paramType = "query")})
    public ResponseResult selectPageList(HttpServletRequest request){
        PageData pageData = new PageData(request);
        return super.selectPageList(pageData);
    }

    @Override
    @GetMapping(value = "get")
    @ApiOperation(value="根据id获取单个考核模板")
    public ResponseResult get(Integer id) {
        if( id==null ){
            ResponseResult.error(PARAM_EORRO);
        }
        return ResponseResult.success(appraisalTemplateService.selectById(id));
    }

    @ApiOperation("启用考核模板")
    @ApiImplicitParams({
            @ApiImplicitParam(name="templateId",value="模板Id",required=false,dataType="int", paramType = "query")})
    @GetMapping(value = "start")
    public ResponseResult start(Integer templateId) throws  Exception{
        if (templateId == null) {
            return ResponseResult.error(PARAM_EORRO);
        }
        String result = appraisalTemplateService.updatestate(templateId, AppConsts.start);
        if (result .equals("-1")) {
            return ResponseResult.error(SYS_EORRO);
        }else if(result.equals("0")){
            return ResponseResult.success();
        }else {
            return ResponseResult.error(result);
        }

    }

    @ApiOperation("停用考核模板")
    @ApiImplicitParams({
            @ApiImplicitParam(name="templateId",value="模板Id",required=false,dataType="int", paramType = "query")})
    @GetMapping(value = "stop")
    public ResponseResult stop(Integer templateId){
        if (templateId == null) {
            return ResponseResult.error(PARAM_EORRO);
        }
        String result = "";
        try {
            result = appraisalTemplateService.updatestate(templateId, AppConsts.stop);
        } catch (Exception e) {
            return ResponseResult.error(SYS_EORRO);
        }
        if (result.equals("-1")) {
            return ResponseResult.error(SYS_EORRO);
        }
        return ResponseResult.success();
    }

    @ApiOperation("获得考核组织")
    @ApiImplicitParams({
            @ApiImplicitParam(name="templateId",value="模板Id",required=false,dataType="int", paramType = "query")})
    @GetMapping(value = "/getAppraisalOrganizationNotInUse")
    public ResponseResult getAppraisalOrganization(Integer templateId,Integer type) throws Exception {

        PageData pageData = new PageData();
        pageData.put("templateId",templateId);
        pageData.put("state",AppConsts.start);
        pageData.put("type",type);
        List<AppraisalOrganizationOutput> list = appraisalTemplateService.getAppraisalOrganization(pageData);
        return ResponseResult.success(list);
    }

    @ApiOperation("获得角色")
    @GetMapping(value = "/getRolesNotInUse")
    public ResponseResult getAllRole(Integer templateId ) throws Exception {
        PageData pageData = new PageData();
        pageData.put("templateId",templateId);
        pageData.put("state",AppConsts.start);
        List<Role> list = appraisalTemplateService.getRolesNotInUse(pageData);
        return ResponseResult.success(list);
    }

    @ApiOperation("获取考核模板指标规则设置信息")
    @GetMapping(value = "/getIndexRuleInfoByEmplatetId")
    @ApiImplicitParams({
            @ApiImplicitParam(name="templateId",value="模板id",required=false,dataType="int", paramType = "query")})
    public ResponseResult getIndexRuleInfoByEmplatetId(Integer templateId) throws Exception {
        List<TemplateSetting> list = null;
        if(templateId!=null){
            list = appraisalTemplateService.getAllIndexRule(templateId);
        }else{
            return  ResponseResult.error("未传入考核模板Id");
        }
        return  ResponseResult.success(list);
    }

    @ApiOperation("保存考核模板指标规则设置信息")
    @PostMapping(value = "/savaIndexRuleInfoByEmplatetId")
    @ApiImplicitParams({
            @ApiImplicitParam(name="templateId",value="模板id",required=false,dataType="int", paramType = "query")})
    public ResponseResult savaIndexRuleInfoByEmplatetId(Integer templateId, @RequestBody @ApiParam(name="模板指标规则",value="传入json格式",required=true) TemplateIndexRuleInput templateIndexRuleInput) throws Exception {
        if(templateId == null){
            return ResponseResult.error(PARAM_EORRO);
        }
        int result = appraisalTemplateService.savaIndexRuleInfoByEmplatetId(templateId,templateIndexRuleInput);
        if(result >= 0){
            return ResponseResult.success();
        }
        return ResponseResult.error(SYS_EORRO);
    }

    @ApiOperation("生成考核计划")
    @GetMapping(value = "/createPlanAndPlanDetail")
    @ApiImplicitParams({
            @ApiImplicitParam(name="templatetId",value="模板id",required=false,dataType="int", paramType = "query")})
    public ResponseResult createPlanAndPlanDetail(Integer templatetId, Integer year,Integer month) throws Exception {
        if(templatetId==null){
            return ResponseResult.error("未选择模板");
        }else{
            //判断需要生成的是年度考核计划还是月度考核计划，若是年度考核计划则只要填写年份否则年月都要填写
            AppraisalTemplate byId = appraisalTemplateService.getById(templatetId);
            if(byId.getType().equals(AppConsts.Monthly_Assessment)){
                if((year==null||(year!=null&&(year>9999||year%1!=0)))||month==null||(month!=null&&(month<1||month>12||month%1!=0))){
                    return ResponseResult.error(PARAM_EORRO);
                }
            }else{
                if(month!=null||"".equals(month)){
                    return ResponseResult.error("当前选中的为年度考核模板，不需要填写月份");
                }else{
                    if((year==null||(year!=null&&(year>9999||year%1!=0)))){
                        return ResponseResult.error(PARAM_EORRO);
                    }
                }
            }
        }
        Users users = appraisalTemplateService.getUsers();
        appraisalTemplateService.createPlanAndPlanDetail(templatetId,year,month,users);
        return ResponseResult.success();
    }

    @ApiOperation("生成当月的所有的月度考核模板的")
    @GetMapping(value = "/timingCreatePlanAndPlanDetail")
    @ApiImplicitParams({
            @ApiImplicitParam(name="year",value="年份",required=false,dataType="int", paramType = "query"),
            @ApiImplicitParam(name="month",value="月份",required=false,dataType="int", paramType = "query")})
    public void timingCreatePlanAndPlanDetail(int year,int month){
        appraisalTemplateService.timingCreatePlanAndPlanDetail(null,year,month);
    }

}
