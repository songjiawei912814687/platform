package com.attendance.controller;

import com.attendance.core.base.BaseController;
import com.attendance.core.base.BaseService;
import com.attendance.domian.output.AttachmentConfigOutput;
import com.attendance.model.AttachmentConfig;
import com.attendance.service.AttachmentConfigService;
import com.common.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;


/**
 * @author: Young
 * @description:
 * @date: Created in 17:17 2018/9/5
 * @modified by:
 */
@RestController
@RequestMapping("attachmentConfig")
@Api(value = "附件配置controller", description = "附件配置操作", tags = {"附件配置操作接口"})
@Slf4j
public class AttachmentConfigController extends BaseController<AttachmentConfigOutput, AttachmentConfig,Integer> {

    @Autowired
    private AttachmentConfigService attachmentConfigService ;

    @Override
    public BaseService<AttachmentConfigOutput,AttachmentConfig, Integer> getService() {
        return attachmentConfigService;
    }


    /**
     * 上传接口
     *
     * @param
     * @return
     */
    @Override
    @PostMapping("/form")
    @ResponseBody
    public ResponseResult formPost(Integer id, @RequestBody AttachmentConfig attachmentConfig) throws Exception {
        return super.formPost(null,attachmentConfig);

    }

    @Override
    @GetMapping(value = "delete")
    @ApiOperation(value = "删除")
    public ResponseResult delete(String idList) throws IllegalAccessException, IntrospectionException, InvocationTargetException, MethodArgumentNotValidException {
        return super.delete(idList);
    }


    @GetMapping(value = "find_by_config_type")
    @ApiOperation("根据附件类型查询")
    public ResponseResult findByConfigType(Integer configType){
        if(configType == null){
            return ResponseResult.error(PARAM_EORRO);
        }
        return attachmentConfigService.findByConfigType(configType);
    }
}
