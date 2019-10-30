package com.message.controller;

import com.common.model.PageData;
import com.common.response.ResponseResult;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.message.core.base.BaseController;
import com.message.core.base.BaseService;
import com.message.domain.input.SMSSendInput;
import com.message.domain.output.SMSSendOutput;
import com.message.model.SMSSend;
import com.message.service.SMSSendService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;


@RestController
@RequestMapping(value = "/smsSend")
@Api(value = "发短信controller",description = "发短信操作",tags = {"发短信操作接口"})
public class SMSSendController extends BaseController<SMSSendOutput, SMSSend,Integer> {

    @Autowired
    private SMSSendService sMSSendService;


    @Override
    public BaseService<SMSSendOutput,SMSSend, Integer> getService() {
        return sMSSendService;
    }


    @ApiOperation("新增待发送短信")
    @RequestMapping(value = "form", method = RequestMethod.POST)
    public ResponseResult formPost(@Validated @RequestBody @ApiParam(name="指标信息",value="传入json格式",required=true) SMSSendInput smsSendInput) throws Exception {
        if ((smsSendInput.getSendList() == null
                || smsSendInput.getSendList().size() == 0)
                && (smsSendInput.getGroupIdList() == null
                || "".equals(smsSendInput.getGroupIdList()))) {
            return ResponseResult.error(PARAM_EORRO);
        }
        return sMSSendService.addMessages(smsSendInput);
    }

//    /**发送短信通知部门管理员*/
//    @GetMapping("sendSuggesstion")
//    public ResponseResult sendSuggesstion(){
//       return sMSSendService.sendSuggesstion();
//    }
    /**发送回馈短信*/
    @GetMapping(value = "sendFeedbackInfo")
    public ResponseResult sendFeedbackInfo(){
        return sMSSendService.sendFeedbackInfo();
    }

    @Override
    @GetMapping(value = "/logicDelete")
    @ApiOperation(value="删除未发送成功短信")
    public ResponseResult logicDelete(String idList) throws IllegalAccessException, IntrospectionException, InvocationTargetException, MethodArgumentNotValidException {

        if(idList==null||"".equals(idList)){
            return ResponseResult.error(PARAM_EORRO);
        }
        var strs = idList.split(",");
        for(var str:strs){
            var id=Integer.parseInt(str);
            SMSSend smsSend = sMSSendService.getById(id);
            if(smsSend.getState()==1){
                return ResponseResult.error("发送成功的短信不能删除");
            }
        }
        return super.logicDelete(idList);
    }


    @ApiOperation("重发短信")
    @RequestMapping(value = "reSendMessage", method = RequestMethod.GET)
    public ResponseResult reSendMessage(Integer messageId) throws Exception {
        if(messageId==null||"".equals(messageId)){
            return ResponseResult.error(PARAM_EORRO);
        }
        var result = sMSSendService.reSendMessage(messageId);
        if(result > 0){
            return ResponseResult.success();
        }else {
            return ResponseResult.error("短信已发送成功，不能重新发送");

        }
    }


    /**
     * 发送成功
     * @param request
     * @return
     */
    @GetMapping(value = "selectPage")
    @ApiOperation("获取分页的发件箱内容")
    @ApiImplicitParams({
            @ApiImplicitParam(name="type",value="业务类型",required=false,dataType="int", paramType = "query"),
            @ApiImplicitParam(name="sendTelephone",value="回复号码",required=false,dataType="string", paramType = "query"),
            @ApiImplicitParam(name="startDate",value="发送开始时间",required=false,dataType="string", paramType = "query"),
            @ApiImplicitParam(name="desc",value="回复号码",required=false,dataType="string", paramType = "query"),
            @ApiImplicitParam(name="endDate",value="发送结束时间",required=false,dataType="string", paramType = "query")

    })
    public ResponseResult selectPage(HttpServletRequest request){
        PageData pageData = new PageData(request);
        return super.selectPageList(pageData);
    }

    /**
     * 待发送和发送失败的
     * @param request
     * @return
     */
    @GetMapping(value = "selectNotPage")
    @ApiOperation("获取分页的待发送内容")
    @ApiImplicitParams({
            @ApiImplicitParam(name="type",value="业务类型",required=false,dataType="int", paramType = "query"),
            @ApiImplicitParam(name="sendTelephone",value="回复号码",required=false,dataType="string", paramType = "query"),
            @ApiImplicitParam(name="desc",value="回复号码",required=false,dataType="string", paramType = "query"),
            @ApiImplicitParam(name="startDate",value="发送开始时间",required=false,dataType="string", paramType = "query"),
            @ApiImplicitParam(name="endDate",value="发送结束时间",required=false,dataType="string", paramType = "query")
    })
    public ResponseResult selectNotPage(HttpServletRequest request){
        PageData pageData = new PageData(request);
        return ResponseResult.success(new PageInfo<>(sMSSendService.findNotPage(pageData)));
    }


    /**
     * 一建重发  会把所有待发送的在加入到发送的队列里
     * @return
     */
    @GetMapping(value = "reSendAll")
    public ResponseResult reSendAll(){
        return ResponseResult.success(sMSSendService.reSend());
    }
}
