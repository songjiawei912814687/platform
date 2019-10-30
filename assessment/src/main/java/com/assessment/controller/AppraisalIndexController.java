package com.assessment.controller;

import com.assessment.core.base.BaseController;
import com.assessment.core.base.BaseService;
import com.assessment.core.util.AppConsts;
import com.assessment.domian.output.AppraisalIndexOutput;
import com.assessment.model.AppraisalIndex;
import com.assessment.service.AppraisalIndexService;
import com.common.model.PageData;
import com.common.response.ResponseResult;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;


@RestController
@RequestMapping(value = "/appraisalIndex")
@Api(value = "指标controller",description = "指标操作",tags = {"指标操作接口"})
public class AppraisalIndexController extends BaseController<AppraisalIndexOutput, AppraisalIndex,Integer> {

    @Autowired
    private AppraisalIndexService appraisalIndexService;

    @Override
    public BaseService<AppraisalIndexOutput,AppraisalIndex, Integer> getService() {
        return appraisalIndexService;
    }

    @Override
    @ApiOperation("新增或修改指标信息")
    @RequestMapping(value = "form", method = RequestMethod.POST)
    public ResponseResult formPost(Integer id, @Validated @RequestBody @ApiParam(name="指标信息",value="传入json格式",required=true) AppraisalIndex appraisalIndex) throws Exception {
        //指标名称需要做唯一性验证
        boolean isRepeat = appraisalIndexService.nameIsRepeat(id,appraisalIndex);
        if(isRepeat){
            return ResponseResult.error("指标名称不能重复");
        }
        return  super.formPost(id,appraisalIndex);
    }

    @ApiOperation("删除指标")
    @GetMapping(value = "logicDelete")
    public ResponseResult logicDelete(Integer id) throws IllegalAccessException, IntrospectionException, InvocationTargetException, MethodArgumentNotValidException {
        //人均办件量和事项联办率的指标分类不可删除
        if(id.equals(AppConsts.personAvg)){
            AppraisalIndex byId = appraisalIndexService.getById(id);
            return ResponseResult.error("删除失败，"+byId.getName()+"不可删除");
        }
        if(id.equals(AppConsts.jointRate)){
            AppraisalIndex byId = appraisalIndexService.getById(id);
            return ResponseResult.error("删除失败，"+byId.getName()+"不可删除");
        }
        return appraisalIndexService.logicDelete(id);
    }


    @ApiOperation("获取所有指标")
    @GetMapping(value = "selectAll")
    public ResponseResult selectAll(HttpServletRequest request){
        PageData pageData = new PageData(request);
        return  super.selectAll(pageData);
    }

    @GetMapping(value = "findPageList")
    @ApiOperation("获取分页的指标列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="type",value="指标类型",required=false,dataType="int", paramType = "query"),
            @ApiImplicitParam(name="name",value="指标名称",required=false,dataType="string", paramType = "query"),
            @ApiImplicitParam(name="objectType",value="对象类型",required=false,dataType="int", paramType = "query"),
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

    @ApiOperation("启用指标")
    @GetMapping(value = "start")
    public ResponseResult start(String idList){
        if (idList == null || idList.length() <= 0) {
            return ResponseResult.error(PARAM_EORRO);
        }
        if(idList.split(",").length>1){
            return ResponseResult.error("指标则能单个停用");
        }
        int result = 0;
        try {
            result = appraisalIndexService.updatestate(idList, AppConsts.start);
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
        if(idList.split(",").length>1){
            return ResponseResult.error("指标则能单个停用");
        }
        int result = 0;
        try {
            if(appraisalIndexService.isInUse(idList)){
                return ResponseResult.error("指标正在被模板使用中无法停用");
            }
            result = appraisalIndexService.updatestate(idList, AppConsts.stop);
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
    @ApiOperation("根据搜索条件导出指标")
    @RequestMapping(value = "appraisalIndexExport", method = RequestMethod.GET)
    public ResponseResult appraisalIndexExport(HttpServletResponse response, HttpServletRequest request) {
        try {
            String str = appraisalIndexService.appraisalIndexExport(response,request);
            return  ResponseResult.success(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
