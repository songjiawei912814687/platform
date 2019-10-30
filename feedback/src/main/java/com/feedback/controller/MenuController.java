package com.feedback.controller;

import com.common.response.ResponseResult;
import com.feedback.core.base.BaseController;
import com.feedback.core.base.BaseService;
import com.feedback.domain.output.MenuOutput;
import com.feedback.model.Menu;
import com.feedback.service.MenuService;
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

}
