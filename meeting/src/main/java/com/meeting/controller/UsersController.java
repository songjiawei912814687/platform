package com.meeting.controller;

import com.common.response.ResponseResult;
import com.meeting.core.base.BaseController;
import com.meeting.core.base.BaseService;
import com.meeting.domain.output.UsersOutput;
import com.meeting.model.Users;
import com.meeting.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;


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
    public Users getByUserName(String name, String uu, Integer i){
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

}
