package com.personnel.controller;


import com.common.model.PageData;
import com.common.response.ResponseResult;
import com.github.pagehelper.PageInfo;
import com.personnel.core.base.BaseController;
import com.personnel.core.base.BaseService;
import com.personnel.domian.output.WindowOutput;
import com.personnel.model.Organization;
import com.personnel.model.Window;
import com.personnel.service.OrganizationService;
import com.personnel.service.WindowService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.beans.IntrospectionException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

@RestController
@RequestMapping("/window")
@Api(value="窗口controller", description = "窗口操作",tags={"窗口操作接口"})
public class WindowController extends BaseController<WindowOutput, Window,Integer> {

    @Autowired
    private WindowService windowService;
    @Autowired
    private OrganizationService organizationService;
    @Override
    public BaseService<WindowOutput,Window, Integer> getService() {
        return windowService;
    }

    @Override
    @RequestMapping(value = "form", method = RequestMethod.POST)
    @ApiOperation(value="新增或修改窗口信息",notes = "新增或修改窗口的方法")
    public ResponseResult formPost(Integer id, @Validated @RequestBody @ApiParam(name="窗口信息",value="传入json格式",required =true) Window window) throws Exception {
        if(!windowService.verificationOrg(window)){
            return ResponseResult.error("必须将窗口关联末级组织");
        }
        if(id==null){
            if(windowService.getByName(window.getName()).size()>0){
                return ResponseResult.error("窗口名称重复");
            }
        }else {
            Window window1=windowService.getById(id);
            if (window1==null){
                return ResponseResult.error("没有找到对应id的窗口");
            }else {
                if(!window1.getName().equals(window.getName())){
                    if(windowService.getByName(window.getName()).size()>0){
                        return ResponseResult.error("窗口名称重复");
                    }
                }
            }
        }
        ResponseResult result =  super.formPost(id,window);
        if(result.getCode() != 200){
            return  ResponseResult.error("保存窗口失败");
        }
        //保存到取号叫号中
        Integer success = windowService.addOrUpdateQueueWindow(window);
        if(success<0){
            return ResponseResult.error("新增或者跟新到排队叫号系统中操作失败");
        }
        return ResponseResult.success("新增成功");
    }

    @Override
    @GetMapping(value = "delete")
    @ApiOperation(value="删除窗口信息")
    public ResponseResult delete(String idList) throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        return super.delete(idList);
    }

    @Override
    @GetMapping(value = "get")
    @ApiOperation(value="根据id获取单个窗口")
    public ResponseResult get(Integer id) throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        return super.selectById(id);
    }

    @GetMapping(value = "findPageList")
    @ApiOperation("获取分页的窗口列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="windowNo",value="窗口号",required=false,dataType="string", paramType = "query"),
            @ApiImplicitParam(name="name",value="窗口名称",required=false,dataType="string", paramType = "query")})
    public ResponseResult selectPageList(HttpServletRequest request){
        return super.selectPageList(new PageData(request));
    }

    @GetMapping(value = "findPageListWithinAuthority")
    @ApiOperation("获取带数据权限的分页的窗口列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name="windowNo",value="窗口号",required=false,dataType="string", paramType = "query"),
            @ApiImplicitParam(name="name",value="窗口名称",required=false,dataType="string", paramType = "query")})
    public ResponseResult selectPageListWithinAuthority(HttpServletRequest request){
        return ResponseResult.success(new PageInfo<>(windowService.selectPageListWithinAuthority(new PageData(request))));
    }

    @ApiOperation("获取所有不分页窗口信息")
    @GetMapping(value = "selectAll")
    @ApiImplicitParams({
            @ApiImplicitParam(name="organizationId",value="组织机构id",required=false,dataType="string", paramType = "query")})
    public ResponseResult selectAll(HttpServletRequest request) throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        PageData pageData=new PageData(request);
        Organization organization=null;
        if(pageData.containsKey("organizationId")){
            int organizationId=Integer.parseInt(pageData.getMap().get("organizationId"));
            organization=organizationService.getById(organizationId);
            if(organization!=null){
                pageData.put("path",organization.getPath()+",");
            }
        }
        return  super.selectAll(pageData);
    }

    @PostMapping(value = "importWindow")
    public  ResponseResult importWindow(MultipartFile file) throws InvocationTargetException, IntrospectionException, IllegalAccessException, IOException {
        if(file.isEmpty()){
            return ResponseResult.error("导入文件为空，请重新导入");
        }
        return windowService.checkedFile(file);
    }
}
