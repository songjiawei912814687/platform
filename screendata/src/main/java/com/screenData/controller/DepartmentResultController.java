package com.screenData.controller;

import com.common.model.PageData;
import com.common.response.ResponseResult;
import com.screenData.service.DepartmentResultService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("departmentResult")
@Api(value = "部门考核结果controller",description = "部门考核结果操作",tags = {"部门考核结果操作接口"})
public class DepartmentResultController {

    @Autowired
    private DepartmentResultService departmentResultService;

    @GetMapping("/performanceIndex")
    @ApiOperation("效能指数")
    @ApiImplicitParams({@ApiImplicitParam(name="rows",value="行数",required=false,dataType="Integer", paramType = "query")})
    public ResponseResult performanceIndex(HttpServletRequest request) {
        PageData pageData = new PageData(request);
        return departmentResultService.performanceIndex(pageData);
    }
}
