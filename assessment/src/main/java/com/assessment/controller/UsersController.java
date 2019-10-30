package com.assessment.controller;

import com.assessment.core.base.BaseController;
import com.assessment.core.base.BaseService;
import com.assessment.domian.output.UsersOutput;
import com.assessment.model.Users;
import com.assessment.service.UserService;
import com.common.response.ResponseResult;
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
public class UsersController extends BaseController<UsersOutput, Users, Integer> {

    @Autowired
    private UserService userService;

    @Override
    public BaseService<UsersOutput, Users, Integer> getService() {
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
     * 申请权限接口
     *
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/getByUserTypeAndLevel")
    public ResponseResult getByUserType() throws Exception {
        if(this.getService().getUsers().getUserType()!=0 || this.getService().getUsers().getAdministratorLevel() == 9){
            return ResponseResult.error("不是人员账号，不能发起申请");
        }
        return ResponseResult.success();
    }

}
