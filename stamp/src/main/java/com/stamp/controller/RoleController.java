package com.stamp.controller;

import com.common.model.PageData;
import com.common.response.ResponseResult;
import com.github.pagehelper.PageInfo;
import com.stamp.core.base.BaseController;
import com.stamp.core.base.BaseService;
import com.stamp.domain.output.RoleOutput;
import com.stamp.model.Role;
import com.stamp.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;

@RequestMapping(value = "role")
@RestController
public class RoleController extends BaseController<RoleOutput, Role,Integer> {

    @Autowired
    private RoleService roleService;

    @Override
    public BaseService<RoleOutput, Role, Integer> getService() {
        return roleService;
    }

    @Override
    @PostMapping(value = "form")
    public ResponseResult formPost(Integer id, @RequestBody Role role) throws Exception {
        return super.formPost(id,role);
    }




    @Override
    @GetMapping(value = "delete")
    public ResponseResult delete(String idList) throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        return super.delete(idList);
    }


    @GetMapping(value = "selectPageList")
    public ResponseResult selectPageList(HttpServletRequest request){
        PageData pageData = new PageData(request);
        pageData.put("createdUserId",roleService.getUsers().getId());
        return super.selectPageList(pageData);
    }

    @GetMapping(value = "selectAll")
    public ResponseResult selectAll(HttpServletRequest request){
        PageData pageData = new PageData(request);
        return super.selectAll(pageData);
    }


    @GetMapping(value = "getByUserId")
    public ResponseResult getByUserId(Integer userId,HttpServletRequest request){
        if(userId == null){
            return ResponseResult.error(PARAM_EORRO);
        }
        return ResponseResult.success(new PageInfo<>(roleService.getByUserId(userId,new PageData(request))));
    }

    @GetMapping(value = "getByUserIdNotIn")
    public ResponseResult getByUserIdNotIn(Integer userId,HttpServletRequest request){
        if(userId == null){
            return ResponseResult.error(PARAM_EORRO);
        }
        return ResponseResult.success(new PageInfo<>(roleService.getByUserIdNotIn(userId,new PageData(request))));
    }

    @Override
    @GetMapping(value = "selectById")
    public ResponseResult selectById(Integer id){
        return super.selectById(id);
    }



}
