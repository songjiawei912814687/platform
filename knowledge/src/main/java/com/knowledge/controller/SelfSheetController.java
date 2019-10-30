package com.knowledge.controller;

import com.common.model.PageData;
import com.common.response.ResponseResult;
import com.knowledge.core.base.BaseController;
import com.knowledge.core.base.BaseService;
import com.knowledge.domain.output.SelfSheetOutput;
import com.knowledge.model.SelfSheet;
import com.knowledge.service.SelfSheetService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;

/**
 * @author: young
 * @description:
 * @date: Created in 2019-08-21  15:17
 */
@RestController
@RequestMapping("selfSheet")
public class SelfSheetController extends BaseController<SelfSheetOutput, SelfSheet,Integer> {

    @Autowired
    private SelfSheetService selfSheetService;

    @Override
    public BaseService<SelfSheetOutput, SelfSheet, Integer> getService() {
        return selfSheetService;
    }

    @Override
    @ApiOperation("自助表单填写")
    @PostMapping(value = "form")
    public ResponseResult formPost(Integer id, @Validated @RequestBody @ApiParam(name="自助表单",value="传入json格式",required=true) SelfSheet selfSheet) throws Exception {
        return super.formPost(id,selfSheet);
    }

    @Override
    @GetMapping(value = "logicDelete")
    public ResponseResult logicDelete(String idList) throws InvocationTargetException, IntrospectionException, MethodArgumentNotValidException, IllegalAccessException {
        return super.logicDelete(idList);
    }


    @Override
    @GetMapping(value = "selectById")
    public ResponseResult selectById(Integer id){
        return super.selectById(id);
    }

    @GetMapping(value = "selectPage")
    @ApiImplicitParams({
            @ApiImplicitParam(name="name",value="表单名称",dataType="String", paramType = "query"),
            @ApiImplicitParam(name="type",value="表单类型",dataType="int", paramType = "query"),
    })
    public ResponseResult selectPage(HttpServletRequest request){
        PageData pageData = new PageData(request);
        return super.selectPageList(pageData);
    }
}


