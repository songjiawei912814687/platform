package com.stamp.controller;

import com.common.response.ResponseResult;
import com.common.utils.Valid;
import com.stamp.core.base.BaseController;
import com.stamp.core.base.BaseService;
import com.stamp.domain.input.PassWordInput;
import com.stamp.domain.input.UserRoleInput;
import com.stamp.domain.output.UserRoleOutput;
import com.stamp.model.UserRole;
import com.stamp.model.Users;
import com.stamp.service.UserRoleService;
import com.stamp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "userRole")
public class UserRoleController extends BaseController<UserRoleOutput, UserRole,Integer> {

    @Autowired
    private UserRoleService userRoleService;


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Override
    public BaseService<UserRoleOutput, UserRole, Integer> getService() {
        return userRoleService;
    }


    @PostMapping(value = "saveByUserId")
    public ResponseResult saveByUserId(@RequestBody UserRoleInput userRoleInput) throws Exception {
        if(userRoleInput.getUserId() == null){
            return ResponseResult.error(PARAM_EORRO);
        }
        if(userRoleInput.getRoleIdList().length() <= 0){
            return ResponseResult.success();
        }
        var strs = userRoleInput.getRoleIdList().split(",");
        for(var str : strs){
            var roleId = 0;
            if(Valid.isNumeric(str)){
                roleId = Integer.parseInt(str);
            }
            if(roleId <= 0){
                continue;
            }
            var userRole = new UserRole(roleId,userRoleInput.getUserId());
            super.formPost(null,userRole);
        }
        return ResponseResult.success();
    }


    @PostMapping(value = "saveByRoleId")
    public ResponseResult saveByRoleId(@RequestBody UserRoleInput userRoleInput) throws Exception {
        if(userRoleInput.getRoleId() == null){
            return ResponseResult.error(PARAM_EORRO);
        }
        if(userRoleInput.getUserIdList().length() <= 0){
            return ResponseResult.success();
        }
        var strs = userRoleInput.getUserIdList().split(",");
        for(var str : strs){
            var userId = 0;
            if(Valid.isNumeric(str)){
                userId = Integer.parseInt(str);
            }
            if(userId <= 0){
                continue;
            }
            var userRole = new UserRole(userRoleInput.getRoleId(),userId);
            super.formPost(null,userRole);
        }
        return ResponseResult.success();
    }



    @GetMapping(value = "deleteByUserId")
    public ResponseResult deleteByUserId(Integer userId,String roleIdList){
        if(userId == null){
            return ResponseResult.error(PARAM_EORRO);
        }
        if(roleIdList == null || roleIdList.length()<=0){
            return ResponseResult.success();
        }
        var strs = roleIdList.split(",");
        for(var str : strs){
            if(Valid.isNumeric(str)){
                userRoleService.deleteByUserIdANdRoleId(userId,Integer.parseInt(str));
            }
        }
        return ResponseResult.success();
    }

    @GetMapping(value = "deleteByRoleId")
    public ResponseResult deleteByRoleId(Integer roleId,String userIdList){
        if(roleId == null){
            return ResponseResult.error(PARAM_EORRO);
        }
        if(userIdList == null || userIdList.length()<=0){
            return ResponseResult.success();
        }
        var strs = userIdList.split(",");
        for(var str : strs){
            if(Valid.isNumeric(str)){
                userRoleService.deleteByUserIdANdRoleId(Integer.parseInt(str),roleId);
            }
        }
        return ResponseResult.success();
    }

    @PostMapping(value = "updatePassWord")
    public ResponseResult updatePassWord(@RequestBody PassWordInput input) throws Exception {
        Users users=userService.getUsers();

        var pa = passwordEncoder.encode(input.getOldPassword());
        if(!passwordEncoder.matches(input.getOldPassword(),users.getPassword())){
            return ResponseResult.error("原密码错误");
        }
        if(input.getNewPassword1().equals(input.getOldPassword())){
            return ResponseResult.error("新密码不能与旧密码一致");
        }
        users.setPassword(passwordEncoder.encode(input.getNewPassword1()).toString());
        if(userService.updatePassword(users)){
            return ResponseResult.success();
        }
        return ResponseResult.error("修改密码失败，请稍后重试");
    }



}
