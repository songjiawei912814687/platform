package com.stamp.controller;

import com.common.response.ResponseResult;
import com.stamp.core.base.BaseController;
import com.stamp.core.base.BaseService;
import com.stamp.domain.output.UsersOutput;
import com.stamp.model.Users;
import com.stamp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: young
 * @project_name: platform
 * @description:
 * @date: Created in 2019-04-12  21:42
 * @modified by:
 */
@RestController
public class TestController extends BaseController<UsersOutput,Users,Integer> {

    @Autowired
    private UserService userService;

    @Override
    public BaseService<UsersOutput, Users, Integer> getService() {
        return userService;
    }

    @GetMapping("getUserName")
    public ResponseResult getUserName(String username){
       Users users =  userService.getByUserName(username);
       return ResponseResult.success(users);
    }


}
