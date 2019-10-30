package com.stamp.controller;


import com.common.model.PageData;
import com.common.response.ResponseResult;
import com.stamp.core.base.BaseController;
import com.stamp.core.base.BaseService;
import com.stamp.domain.output.EmployeesOutput;
import com.stamp.model.Employees;
import com.stamp.service.EmployeesService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping(value = "/employees")
@Api(value = "人员controller",description = "人员操作",tags = {"人员操作接口"})
public class EmployeesController extends BaseController<EmployeesOutput, Employees,Integer> {

    @Autowired
    private EmployeesService employeesService;

    @Override
    public BaseService<EmployeesOutput,Employees, Integer> getService() {
        return employeesService;
    }
    @Override
    @ApiOperation("新增或修改人员信息")
    @RequestMapping(value = "form", method = RequestMethod.POST)
    public ResponseResult formPost(Integer id, @Validated @RequestBody @ApiParam(name="人员信息",value="传入json格式",required=true) Employees employees) throws Exception {

        return super.formPost(id, employees);
    }

    @GetMapping(value = "findPageList")
    @ApiOperation("获取分页的员工列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="employeeNo",value="员工工号",required=false,dataType="string", paramType = "query"),
            @ApiImplicitParam(name="name",value="员工姓名",required=false,dataType="string", paramType = "query")})
    public ResponseResult selectPageList(HttpServletRequest request){
        PageData pageData = new PageData(request);
        pageData.put("amputated",0);
        return super.selectPageList(pageData);
    }

    @Override
    @GetMapping(value = "selectById")
    @ApiOperation(value="根据id获取单个窗口")
    public ResponseResult selectById(Integer id) {
        if( id==null ){
            ResponseResult.error(PARAM_EORRO);
        }
        return ResponseResult.success(employeesService.getByEmployeeId(id));
    }

    @GetMapping(value = "findList")
    @ApiOperation("获取分页的员工列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="employeeNo",value="员工工号",required=false,dataType="string", paramType = "query"),
            @ApiImplicitParam(name="name",value="员工姓名",required=false,dataType="string", paramType = "query")})
    public ResponseResult findList(HttpServletRequest request){
        PageData pageData = new PageData(request);
        var level = employeesService.getUsers().getAdministratorLevel();
        if(level!=9){
            if(getService().getUsers().getUserType()==0){
                pageData.put("employeeId",getService().getUsers().getEmployeeId());
            }else {
                pageData.put("organId",getService().getUsers().getOrganizationId());
            }
        }
        return super.selectPageList(pageData);
    }
}
