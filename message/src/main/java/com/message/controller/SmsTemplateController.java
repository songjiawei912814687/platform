package com.message.controller;


import com.common.Enum.SmsTemplateEnum;
import com.common.model.PageData;
import com.common.response.ResponseResult;
import com.message.core.base.BaseController;
import com.message.core.base.BaseService;
import com.message.domain.output.SmsTemplateOutput;
import com.message.model.SmsTemplate;
import com.message.service.SmsTemplateService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;

@RestController
@RequestMapping(value = "/smstemplate")
@Api(value = "短信模板controller",description = "短信模板操作",tags = {"短信模板操作接口"})
public class SmsTemplateController extends BaseController<SmsTemplateOutput,SmsTemplate,Integer>{

    @Autowired
    private SmsTemplateService smsTemplateService;

    @Autowired
    private LoadBalancerClient loadBalancerClient;
    @Override
    public BaseService<SmsTemplateOutput, SmsTemplate, Integer> getService() {
        return smsTemplateService;
    }

    @Override
    @ApiOperation("新增或修改短信模板")
    @RequestMapping(value = "form", method = RequestMethod.POST)
    public ResponseResult formPost(Integer id,@RequestBody  SmsTemplate smsTemplate) throws Exception {
        if(id==null){
            if(smsTemplate.getIsReply()==null||smsTemplate.getIsReply().equals("")){
                return ResponseResult.error("是否回复不能为空");
            }else {
                if(smsTemplate.getIsReply()==1){
                    if(smsTemplate.getReplyLimit()==null||smsTemplate.getReplyLimit().equals("")){
                        return ResponseResult.error("是回复时必须要有回复期限");
                    }
                }
            }
            if(smsTemplateService.getByName(smsTemplate.getName())!=null){
                return ResponseResult.error("名称重复，请重新填写名称");
            }
            smsTemplate.setState(0);
            smsTemplate.setDisplayOrder(0);
        }else {
            SmsTemplate smsTemplate1=smsTemplateService.getById(id);
            if(smsTemplate1.getState()==1){
                return ResponseResult.error("启用状态的模板不能编辑");
            }
            smsTemplate.setState(smsTemplate1.getState());
            if(!smsTemplate.getName().equals(smsTemplate1.getName())){
                if(smsTemplateService.getByName(smsTemplate.getName())!=null){
                    return ResponseResult.error("名称重复，请重新填写名称");
                }
            }
        }
        return super.formPost(id,smsTemplate);
    }


    @Override
    @ApiOperation("删除短信模板信息")
    @GetMapping(value = "logicDelete")
    public ResponseResult logicDelete(String idList) throws IllegalAccessException, IntrospectionException, InvocationTargetException, MethodArgumentNotValidException {
        if(idList==null||"".equals(idList)){
            return ResponseResult.error(PARAM_EORRO);
        }
        SmsTemplate smsTemplate=smsTemplateService.getById(Integer.parseInt(idList));
        if(smsTemplate.getState()==1){
            return ResponseResult.error("启用状态的模板不能删除");
        }
        return super.logicDelete(idList);
    }

    @Override
    @ApiOperation("根据id获取单个短信模板")
    @GetMapping(value = "selectById")
    public ResponseResult selectById(Integer id){
        return super.selectById(id);
    }


    @GetMapping(value = "findPageList")
    @ApiOperation("获取分页的短信模板")
    public ResponseResult findPageList(HttpServletRequest request){
        PageData pageData = new PageData(request);
        return super.selectPageList(pageData);
    }


    @ApiOperation("启用短信模板")
    @GetMapping(value = "start")
    public ResponseResult start(String idList){
        if (idList == null || idList.length() <= 0) {
            return ResponseResult.error(PARAM_EORRO);
        }
        int result = 0;
        try {
            result = smsTemplateService.updatestate(idList,1);
        } catch (Exception e) {
            return ResponseResult.error(SYS_EORRO);
        }
        if(result==0){
            return ResponseResult.error("此类型下已有启用的模板");
        }
        if (result < 0) {
            return ResponseResult.error(SYS_EORRO);
        }
        return ResponseResult.success();
    }

    @ApiOperation("停用短信模板")
    @GetMapping(value = "stop")
    public ResponseResult stop(String idList){
        if (idList == null || idList.length() <= 0) {
            return ResponseResult.error(PARAM_EORRO);
        }
        int result = 0;
        try {
            result = smsTemplateService.updatestate(idList, 0);
        } catch (Exception e) {
            return ResponseResult.error(SYS_EORRO);
        }
        if (result < 0) {
            return ResponseResult.error(SYS_EORRO);
        }
        return ResponseResult.success();
    }

    @GetMapping(value = "findByType")
    @ApiOperation("根据类型获取短信内容")
    public ResponseResult findByType(HttpServletRequest request){
        PageData pageData = new PageData(request);
        if(pageData.containsKey("type")){
            if(pageData.getMap().get("type").equals("")||pageData.getMap().get("type")==null){
                return ResponseResult.error(PARAM_EORRO);
            }
        }else {
            return ResponseResult.error(PARAM_EORRO);
        }
        SmsTemplateOutput smsTemplateOutput=smsTemplateService.getByType(pageData.getMap().get("type"));
        if(smsTemplateOutput==null){
            return ResponseResult.error("此类型下没有启用的模板");
        }

        return ResponseResult.success(smsTemplateOutput.getDescription());
    }

    @GetMapping(value = "findConfig")
    public ResponseResult findConfig(HttpServletRequest request){
        var ruleMap = SmsTemplateEnum.getBymodel("template");
        return ResponseResult.success(ruleMap);
    }

    @GetMapping(value = "findConfigByType")
    @ApiImplicitParams({@ApiImplicitParam(name="type",value="业务类型",required=true,dataType="string", paramType = "query")})
    public ResponseResult findConfigByType(HttpServletRequest request){
        PageData pageData = new PageData(request);
        if(pageData.containsKey("type")){
            if(pageData.getMap().get("type").equals("")||pageData.getMap().get("type")==null){
                return ResponseResult.error(PARAM_EORRO);
            }
        }else {
            return ResponseResult.error(PARAM_EORRO);
        }
        var ruleMap = SmsTemplateEnum.getBymodel(pageData.getMap().get("type"));
        return ResponseResult.success(ruleMap);
    }

}
