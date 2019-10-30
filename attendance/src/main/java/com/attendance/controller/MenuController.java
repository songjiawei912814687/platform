package com.attendance.controller;

import com.attendance.core.base.BaseController;
import com.attendance.core.base.BaseService;
import com.attendance.domian.output.MenuOutput;
import com.attendance.model.Menu;
import com.attendance.service.MenuService;
import com.common.response.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "menu")
public class MenuController extends BaseController<MenuOutput, Menu,Integer> {
    @Autowired
    private MenuService menuService;
    @Override
    public BaseService<MenuOutput, Menu, Integer> getService() {
        return menuService;
    }

    @GetMapping(value = "getMenuInit")
    public ResponseResult getMenuInit(){
        return ResponseResult.success(menuService.getMenuInit());
    }

    /**
     * 申请权限接口
     *
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/getByUserTypeAndLevel")
    public ResponseResult getByUserType() throws Exception {
        var user = this.getService().getUsers();
        if(user.getUserType() != 0 || user.getAdministratorLevel() == 9){
            return ResponseResult.error("请登陆个人账号申请");
        }
        return ResponseResult.success();
    }

}
