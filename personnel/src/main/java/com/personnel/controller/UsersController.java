package com.personnel.controller;

import com.common.response.ResponseResult;
import com.personnel.core.base.BaseController;
import com.personnel.core.base.BaseService;
import com.personnel.domian.output.UsersOutput;
import com.personnel.model.Users;
import com.personnel.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;

//import org.springframework.security.access.prepost.PreAuthorize;


@RestController
@RequestMapping(value = "/users")
//@PreAuthorize("hasAuthority('YUUa')")
public class UsersController extends BaseController<UsersOutput, Users,Integer> {





    @Autowired
    private UserService userService;


    @Override
    public BaseService<UsersOutput,Users,Integer> getService() {
        return userService;
    }




    @GetMapping(value = "getByUserName")
    public Users getByUserName(String name,String uu,Integer i){
        if(!name.equals("")){
            return userService.getByUserName(name);
        }
        return null;
    }




    @RequestMapping(value="/login",method = RequestMethod.GET)
    public String login(){
        return "login";
    }


    @Override
    @GetMapping(value = "get")
//    @PreAuthorize("hasAuthority('YUUuuu')")
    public ResponseResult get(Integer id) throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        return super.get(id);
    }


    /**
     * @Description: 临时接口 （添加账号并给默认值）
     * @Param:
     * @return:
     * @Author: dingpc
     * @Date: 2019/2/19
     */
    @GetMapping(value = "addAccount")
    public ResponseResult addAccount() throws Exception{
        userService.addAccount();
        return ResponseResult.success();
    }

}
