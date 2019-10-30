package com.feedback.controller;

import com.common.response.ResponseResult;
import com.feedback.core.base.BaseController;
import com.feedback.core.base.BaseService;
import com.feedback.domain.output.FeedbackInfoOutput;
import com.feedback.model.FeedbackInfo;
import com.feedback.service.FeedbackInfoService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;

/**
 * @author: Young
 * @description: 反馈信息
 * @date: Created in 15:42 2018/11/5
 * @modified by:
 */
@RestController
@RequestMapping("feedback_info")
public class FeedbackInfoController extends BaseController<FeedbackInfoOutput, FeedbackInfo,Integer> {

    @Autowired
    private FeedbackInfoService feedbackInfoService;

    @Override
    public BaseService<FeedbackInfoOutput, FeedbackInfo, Integer> getService() {
        return feedbackInfoService;
    }
    /**1.导入反馈信息**/
    @PostMapping(value = "import")
    public ResponseResult importFeedbackInfo(MultipartFile file) throws Exception{
       if(file.isEmpty()){
           return ResponseResult.error("导入文件为空，请重新导入");
       }
       return feedbackInfoService.checkedFile(file);
    }

    /**跟新状态*/
    @PostMapping(value = "update_feedback")
    public ResponseResult updateFeedback(Integer id, Integer statis,Integer complete) throws IntrospectionException, MethodArgumentNotValidException, IllegalAccessException, InvocationTargetException {
        if(statis==null||complete==null){
            return ResponseResult.error("请选择满意率和实现率");
        }
        int result = feedbackInfoService.updateFeedBack(id, statis,complete);
        if(result<0){
            return ResponseResult.error("跟新状态失败");
        }
        return ResponseResult.success("跟新状态成功");
    }



    /**处理回馈信息**/
    @GetMapping(value = "find_feedbackInfo_in_twoDays")
    public ResponseResult findFeedbackInfoInTwoDays(Integer id) throws IntrospectionException, MethodArgumentNotValidException, IllegalAccessException, InvocationTargetException, ParseException {
         feedbackInfoService.findFeedbackInfoInTwoHours(id);
         return ResponseResult.success();
    }


    /**2.查询单条反馈信息**/
    @Override
    @GetMapping(value = "selectById")
    public ResponseResult selectById(Integer id){
        return  super.selectById(id);
    }

    /**3.条件查询反馈信息，姓名：手工入库，支持模糊查询，受理部门。手工入库，支持模糊查询**/
    @GetMapping(value = "findPageList")
    @ApiOperation("获取分页的反馈信息列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="organizationId",value="组织机构id",required=false,dataType="string", paramType = "query"),
            @ApiImplicitParam(name="personName",value="群众姓名",required=false,dataType="string", paramType = "query"),
            @ApiImplicitParam(name="employeesNo",value="员工工号",required=false,dataType="string", paramType = "query"),
            @ApiImplicitParam(name="phone",value="手机号码",required=false,dataType="string", paramType = "query"),
            @ApiImplicitParam(name="satisfaction",value="满意率",required=false,dataType="int", paramType = "query"),
            @ApiImplicitParam(name="complete",value="实现率",required=false,dataType="int", paramType = "query"),
            @ApiImplicitParam(name="appraiseContent",value="回复内容",required=false,dataType="string", paramType = "query"),
            @ApiImplicitParam(name="startTime",value="开始时间",required=false,dataType="string", paramType = "query"),
            @ApiImplicitParam(name="endTime",value="结束时间",required=false,dataType="string", paramType = "query")
    })
    public ResponseResult findPageList(HttpServletRequest request){
        return feedbackInfoService.findPageList(request);
    }

    /**4.导出到excel**/
    @GetMapping(value = "export")
    public ResponseResult export(HttpServletResponse response, HttpServletRequest request) {
        try {
            String str = feedbackInfoService.export(response,request);
            return  ResponseResult.success(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

