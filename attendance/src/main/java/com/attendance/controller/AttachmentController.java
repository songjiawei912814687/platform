package com.attendance.controller;

import com.attendance.core.base.BaseController;
import com.attendance.core.base.BaseService;
import com.attendance.domian.output.AttachmentOutput;
import com.attendance.model.Attachment;
import com.attendance.service.AttachmentService;
import com.common.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;

/**
 * @author: Young
 * @description:
 * @date: Created in 16:13 2018/9/5
 * @modified by:
 */
@RestController
@RequestMapping("/attachment")
@Api(value = "附件管理controller", description = "附件管理操作", tags = {"附件管理操作接口"})
public class AttachmentController extends BaseController<AttachmentOutput, Attachment,Integer> {

    @Autowired
    private AttachmentService attachmentService;

    @Override
    public BaseService getService() {
        return attachmentService;
    }


    @Override
    @PostMapping(value = "form")
    @ApiOperation("新增/跟新附件管理，当传入id已跟新的值为跟新")
    public ResponseResult formPost(Integer id, @RequestBody @ApiParam(name = "附件管理", value = "传入json格式", required = true) Attachment attachment) throws Exception {
        if(attachment==null){
            return ResponseResult.error(PARAM_EORRO);
        }
       return  super.formPost(id,attachment);
    }

    @Override
    @GetMapping(value = "delete")
    @ApiOperation(value = "删除")
    public ResponseResult delete(String idList) throws IllegalAccessException, IntrospectionException, InvocationTargetException, MethodArgumentNotValidException {
        return super.delete(idList);
    }


    @GetMapping(value = "find_By_resources_type")
    @ApiOperation("根据资源类型查询")
    public ResponseResult findByResourcesType(Integer resourcesType){
        if(resourcesType==null){
            return ResponseResult.error(PARAM_EORRO);
        }

        return attachmentService.findByResourcesType(resourcesType);
    }

    @GetMapping(value = "find_source_file_name")
    @ApiOperation("根据原文件查询")
    public ResponseResult findBySourceFileName(String sourceFileName){
        if(sourceFileName==null){
            return ResponseResult.error(PARAM_EORRO);
        }
        return attachmentService.findBySourceFileName(sourceFileName);
    }
}
