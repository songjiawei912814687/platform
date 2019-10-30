package com.assessment.controller;

import com.assessment.core.base.BaseController;
import com.assessment.core.base.BaseService;
import com.assessment.core.util.AppConsts;
import com.assessment.domian.output.AppraisalIndexOutput;
import com.assessment.domian.output.AppraisalRuleOutput;
import com.assessment.model.AppraisalIndex;
import com.assessment.model.AppraisalRule;
import com.assessment.service.AppraisalIndexService;
import com.assessment.service.AppraisalRuleService;
import com.common.model.PageData;
import com.common.response.ResponseResult;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;


@RestController
@RequestMapping(value = "/appraisalRule")
@Api(value = "指标规则controller",description = "指标规则操作",tags = {"指标规则操作接口"})
public class AppraisalRuleController extends BaseController<AppraisalRuleOutput, AppraisalRule,Integer> {

    @Autowired
    private AppraisalRuleService appraisalRuleService;

    @Override
    public BaseService<AppraisalRuleOutput,AppraisalRule, Integer> getService() {
        return appraisalRuleService;
    }

    @Override
    @ApiOperation("新增或修改考核规则")
    @RequestMapping(value = "form", method = RequestMethod.POST)
    public ResponseResult formPost(Integer id, @Validated @RequestBody @ApiParam(name="指标规则",value="传入json格式",required=true) AppraisalRule appraisalRule) throws Exception {
        //指标名称需要做唯一性验证
        boolean isRepeat = appraisalRuleService.nameIsRepeat(id,appraisalRule);
        if(isRepeat){
            return ResponseResult.error("指标规则名称不能重复");
        }
        return  super.formPost(id,appraisalRule);
    }

    @ApiOperation("删除考核规则")
    @GetMapping(value = "logicDelete")
    public ResponseResult logicDelete(Integer id) throws IllegalAccessException, IntrospectionException, InvocationTargetException, MethodArgumentNotValidException {
        return appraisalRuleService.logicDelete(id);
    }


    @ApiOperation("获取所有考核规则")
    @GetMapping(value = "selectAll")
    public ResponseResult selectAll(HttpServletRequest request){
        PageData pageData = new PageData(request);
        return  super.selectAll(pageData);
    }

    @GetMapping(value = "findPageList")
    @ApiOperation("获取分页的指标列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="appraisalId",value="指标",required=false,dataType="int", paramType = "query"),
            @ApiImplicitParam(name="name",value="考核规则名称",required=false,dataType="string", paramType = "query"),
            @ApiImplicitParam(name="description",value="计分标准项",required=false,dataType="string", paramType = "query"),
            @ApiImplicitParam(name="scoreType",value="打分规则",required=false,dataType="int", paramType = "query"),
            @ApiImplicitParam(name="state",value="状态",required=false,dataType="int", paramType = "query")})
    public ResponseResult selectPageList(HttpServletRequest request){
        PageData pageData = new PageData(request);
        return super.selectPageList(pageData);
    }

    @Override
    @GetMapping(value = "get")
    @ApiOperation(value="根据id获取单个指标")
    public ResponseResult get(Integer id) {
        return super.selectById(id);
    }

    @GetMapping(value = "getRuleByIndexId")
    @ApiOperation(value="根据指标Id获取考核规则")
    public ResponseResult getRuleByIndexId(Integer indexId) {
        if(indexId==null){
            return  ResponseResult.error(PARAM_EORRO);
        }
        return appraisalRuleService.getRuleByIndexId(indexId);
    }

    @ApiOperation("启用指标")
    @GetMapping(value = "start")
    public ResponseResult start(String idList){
        if (idList == null || idList.length() <= 0) {
            return ResponseResult.error(PARAM_EORRO);
        }
        int result = 0;
        try {
            result = appraisalRuleService.updatestate(idList, AppConsts.start);
        } catch (Exception e) {
            return ResponseResult.error(SYS_EORRO);
        }
        if (result < 0) {
            return ResponseResult.error(SYS_EORRO);
        }
        return ResponseResult.success();
    }

    @ApiOperation("停用指标")
    @GetMapping(value = "stop")
    public ResponseResult stop(String idList){
        if (idList == null || idList.length() <= 0) {
            return ResponseResult.error(PARAM_EORRO);
        }
        int result = 0;
        try {
            result = appraisalRuleService.updatestate(idList, AppConsts.stop);
        } catch (Exception e) {
            return ResponseResult.error(SYS_EORRO);
        }
        if (result < 0) {
            return ResponseResult.error(SYS_EORRO);
        }
        return ResponseResult.success();
    }


    /**
     *
     * @param
     * @return
     */
    @ApiOperation("根据搜索条件导出指标规则")
    @RequestMapping(value = "appraisalRuleExport", method = RequestMethod.GET)
    public ResponseResult appraisalRuleExport(HttpServletResponse response, HttpServletRequest request) {
        try {
            String str = appraisalRuleService.appraisalRuleExport(response,request);
            return  ResponseResult.success(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
