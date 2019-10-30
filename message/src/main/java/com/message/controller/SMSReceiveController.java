package com.message.controller;

import com.common.model.PageData;
import com.common.response.ResponseResult;
import com.message.core.base.BaseController;
import com.message.core.base.BaseService;
import com.message.domain.output.SMSReceiveOutPut;
import com.message.model.SMSReceive;
import com.message.service.SMSReceiveService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;

/**
 * @author: Young
 * @description:
 * @date: Created in 11:45 2018/10/25
 * @modified by:
 */
@RestController
@RequestMapping("sms_receive")
public class SMSReceiveController extends BaseController<SMSReceiveOutPut, SMSReceive,Integer> {

    @Autowired
    private SMSReceiveService smsReceiveService;

    @Override
    public BaseService<SMSReceiveOutPut, SMSReceive, Integer> getService() {
        return smsReceiveService;
    }

    @GetMapping(value = "findPageList")
    @ApiOperation("获取分页的收件箱内容")
    @ApiImplicitParams({
            @ApiImplicitParam(name="type",value="业务类型",required=false,dataType="int", paramType = "query"),
            @ApiImplicitParam(name="sendTelephone",value="回复号码",required=false,dataType="string", paramType = "query"),
            @ApiImplicitParam(name="startDate",value="接受开始时间",required=false,dataType="string", paramType = "query"),
            @ApiImplicitParam(name="endDate",value="接受结束时间",required=false,dataType="string", paramType = "query"),
            @ApiImplicitParam(name="content",value="回复内容",required=false,dataType="string", paramType = "query")
    })
    public ResponseResult findPageList(HttpServletRequest request) {
        PageData pageData = new PageData(request);
        return super.selectPageList(pageData);
    }

    @Override
    @GetMapping(value = "selectById")
    public ResponseResult selectById(Integer id){
        return  super.selectById(id);
    }


    @Override
    @GetMapping(value = "logicDelete")
    public ResponseResult logicDelete(String idList) throws InvocationTargetException, IntrospectionException, MethodArgumentNotValidException, IllegalAccessException {
        return super.logicDelete(idList);
    }

    @GetMapping(value = "/exportExcel")
    @ApiOperation("导出到excel功能")
    public Object exportExcel(HttpServletResponse response , HttpServletRequest request)  {
        try {
            String str = smsReceiveService.exportExcel(response, request);
            return ResponseResult.success(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseResult.error("导出失败");
    }
}
