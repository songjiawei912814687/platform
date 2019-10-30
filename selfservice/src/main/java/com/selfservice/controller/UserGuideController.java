package com.selfservice.controller;

import com.common.model.PageData;
import com.common.response.ResponseResult;
import com.selfservice.domain.output.MaterialListOutput;
import com.selfservice.domain.output.MinimumParticleOutput;
import com.selfservice.domain.output.QltQlsxOutput;
import com.selfservice.service.OrganizationService;
import com.selfservice.service.UserGuideService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author: Young
 * @description: 用户指南
 * @date: Created in 18:51 2018/11/15
 * @modified by:
 */
@RestController
@Api(value = "用户指南controller",description = "用户指南操作",tags = {"用户指南接口"})
@RequestMapping("user_guide")
public class UserGuideController {

    @Autowired
    private UserGuideService userGuideService;

    /**进入热门事项首页**/
    @GetMapping(value = "findHotList")
    @ApiOperation("获取最小颗粒")
    public ResponseResult findHotPageList() {
        return userGuideService.findHotList();
    }

    @ApiOperation("部门列表")
    @GetMapping(value = "findOrganizationList")
    public ResponseResult getOrganizationList() {
        return userGuideService.getOrganizationList();
    }

    /**按主题**/
    @GetMapping(value = "findHangList")
    @ApiOperation("获取分页的按主题分页内容")
    public ResponseResult findHangPageList() {
        return userGuideService.findHangList();
    }

    /**首页根据事项类型名称收索**/
    @GetMapping(value = "findByHappenType")
    @ApiOperation("首页根据事项类型名称收索")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "事项类型名称",  dataType = "string", paramType = "query")
    })
    public ResponseResult findByHappenType(HttpServletRequest request) {
        return userGuideService.findByHappenTypeList(request);
    }


    /**点击热门事项或主题或部门加载事项列表**/
    @GetMapping(value = "findMiniList")
    @ApiOperation("获取事项列表内容")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "事项id", required = true, dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "事项type", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "name", value = "查找框事项名称", required = false, dataType = "string", paramType = "query")
    })
    public ResponseResult findMiniList(HttpServletRequest request){
        PageData pageData = new PageData(request);
        String id = pageData.getString("id");
        Integer type = Integer.valueOf(pageData.getString("type"));
        if(StringUtils.isBlank(id) ||type==null||"".equals(type)){
            return ResponseResult.success();
        }
        List<MinimumParticleOutput>qltQlsxOutputs =userGuideService.findMiniList(type,pageData);
        return ResponseResult.success(qltQlsxOutputs);
    }


    /**查看更多情形列表**/
    @GetMapping(value = "findMore")
    @ApiOperation("查看更多情形列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "事项id", required = true, dataType = "int", paramType = "query")
    })
    public ResponseResult findMore(Integer id){
        if(id==null||"".equals(id)){
            return ResponseResult.success();
        }
        List<MinimumParticleOutput>qltQlsxOutputs =userGuideService.findMore(id);
        return ResponseResult.success(qltQlsxOutputs);
    }


    /**打印资料接口**/
    @GetMapping(value = "getMaterialListById")
    @ApiOperation("打印资料接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "事项id", required = true, dataType = "int", paramType = "query")
    })
    public ResponseResult getMaterialListById(Integer id){
        if(id==null||"".equals(id)){
            return ResponseResult.success();
        }
        List<MaterialListOutput>qltQlsxOutputs =userGuideService.getMaterialListById(id);
        return ResponseResult.success(qltQlsxOutputs);
    }

    /**按主题点击进进入事项；列表**/
    @GetMapping(value = "findByHangYeType")
    public ResponseResult findByHangYeType(String qlFullId){
        return userGuideService.findShiXList(qlFullId);
    }


    /**点击某个热门事项进入事项列表**/
    @GetMapping(value = "findShiXList")
    public ResponseResult findShiXList(String qlFullId){
        return userGuideService.findShiXList(qlFullId);
    }

    /**资料下载**/
    @GetMapping(value = "download")
    public ResponseResult download(String qlFullId){
        return userGuideService.download(qlFullId);
    }

    /**办事指南**/
    @GetMapping(value = "getMessage")
    @ApiOperation(value="根据id获取单个最小颗粒的办事指南")
    public ResponseResult getMessage(Integer id) {
        if(id==null||"".equals(id)){
            return ResponseResult.success();
        }
        return userGuideService.getMessageDetail(id);
    }

    /**办事指南下载**/
    @GetMapping(value = "downLoadMessage")
    @ApiOperation(value="根据id获取单个最小颗粒的办事指南")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "事项id", required = true, dataType = "int", paramType = "query")
    })
    public ResponseResult downLoadMessage(HttpServletRequest request) {
        PageData pageData = new PageData(request);
        Integer id = Integer.valueOf(pageData.getString("id"));
        if(id==null||"".equals(id)){
            return ResponseResult.success();
        }
        return userGuideService.downLoadMessage(request,id);
    }
}
