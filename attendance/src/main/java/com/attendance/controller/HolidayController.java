package com.attendance.controller;

import com.attendance.core.base.BaseController;
import com.attendance.core.base.BaseService;
import com.attendance.domian.output.HolidayOutput;
import com.attendance.model.Holiday;
import com.attendance.service.HolidayService;
import com.common.model.PageData;
import com.common.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;

/**
 * @author: Young
 * @description:
 * @date: Created in 22:59 2018/9/11
 * @modified by:
 */
@RestController
@RequestMapping("/holiday")
@Api(value = "假日controller",description = "节假日操作",tags = {"节假日操作接口"})
public class HolidayController extends BaseController<HolidayOutput, Holiday,Integer> {

    @Autowired
    private HolidayService holidayService;



    @Override
    public BaseService<HolidayOutput, Holiday, Integer> getService() {
        return holidayService;
    }

    @Override
    @PostMapping(value = "form")
    @ApiOperation("新增或修改节假日信息")
    public ResponseResult formPost(Integer id,@Validated @RequestBody Holiday holiday) throws Exception {
        return super.formPost(id,holiday);
    }


    @Override
    @GetMapping(value = "logicDelete")
    @ApiOperation("删除节假日信息")
    public ResponseResult logicDelete(String idList) throws InvocationTargetException, IntrospectionException, MethodArgumentNotValidException, IllegalAccessException {
        return super.logicDelete(idList);
    }

    @Override
    @GetMapping(value = "selectById")
    public ResponseResult selectById(Integer id){
        return  super.selectById(id);
    }

    @GetMapping(value = "findPageList")
    @ApiOperation("获取分页的节假日列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="holidayName",value="节假日名称",required=false,dataType="string", paramType = "query")
            })
    public ResponseResult findPageList(HttpServletRequest request){
        PageData pageData = new PageData(request);
        return super.selectPageList(pageData);
    }

    @GetMapping(value = "findAll")
    @ApiOperation("获取所有的节假日列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="holidayName",value="节假日名称",required=false,dataType="string", paramType = "query")
    })
    public ResponseResult findAll(HttpServletRequest request){
        PageData pageData = new PageData(request);
        return super.selectAll(pageData);
    }


}
