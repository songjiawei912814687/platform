package com.stamp.controller;

import com.common.response.ResponseResult;
import com.common.utils.Valid;
import com.stamp.core.base.BaseController;
import com.stamp.core.base.BaseService;
import com.stamp.domain.output.MenuOutput;
import com.stamp.model.Menu;
import com.stamp.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;

@RestController
@RequestMapping(value = "menu")
public class MenuController extends BaseController<MenuOutput, Menu,Integer> {
    @Autowired
    private MenuService menuService;

    @Override
    public BaseService<MenuOutput, Menu, Integer> getService() {
        return menuService;
    }

    @Override
    @PostMapping(value = "form")
    public ResponseResult formPost(Integer id, @RequestBody Menu menu) throws Exception {
        if(menu == null){
            return ResponseResult.error(PARAM_EORRO);
        }
        if(menu.getDisPlayOrderBy() == null){
            menu.setDisPlayOrderBy(0);
        }
        if(id == null){
            if(menu.getParentId() <= 0){
                menu.setHasChild(0);
                menu.setIsShow(0);
                return super.formPost(id,menu);
            }

            var parentMenu = menuService.getById(menu.getParentId());
            if(parentMenu == null){
                return ResponseResult.error(PARAM_EORRO);
            }

            parentMenu.setHasChild(1);
            super.formPost(parentMenu.getId(),parentMenu);
            menu.setHasChild(0);
            menu.setIsShow(0);
            return super.formPost(id,menu);
        }else {
            var originalMenu = menuService.getById(id);
            if(originalMenu == null){
                return ResponseResult.error(PARAM_EORRO);
            }
            var originalMenuParentId = originalMenu.getParentId();
            if(originalMenuParentId.equals(menu.getParentId())){
                return super.formPost(id,menu);
            }
            Menu originalParentMenu = null;
            if(originalMenuParentId != 0){
                originalParentMenu = menuService.getById(originalMenu.getParentId());

            }
            Menu parentMenu = null;

            if(menu.getParentId() != 0){
                parentMenu = menuService.getById(menu.getParentId());

            }

           if(originalParentMenu != null){
               if(menuService.getByParentId(originalParentMenu.getParentId()).size() <= 0){
                   originalParentMenu.setHasChild(0);
                   super.formPost(originalParentMenu.getId(),originalParentMenu);
               }
           }
            if(parentMenu != null){
                parentMenu.setHasChild(1);
                super.formPost(parentMenu.getId(),parentMenu);
            }

            return super.formPost(id,menu);
        }
    }

    @Override
    @GetMapping(value = "get")
    public ResponseResult get(Integer id){
        return super.selectById(id);
    }


    @GetMapping(value = "getMenuInit")
    public ResponseResult getMenuInit(){
        return ResponseResult.success(menuService.getMenuInit());
    }

    @GetMapping(value = "getAll")
    public ResponseResult getAll(){
        return ResponseResult.success(menuService.getAllList());
    }

    @GetMapping(value = "getTree")
    public ResponseResult getTree(){
        var menus = menuService.findAll();
        return ResponseResult.success(menuService.toZtree(menus));
    }

    @GetMapping(value = "delete")
    public ResponseResult deleted(String idList) throws InvocationTargetException, IntrospectionException, MethodArgumentNotValidException, IllegalAccessException {
        if(idList == null || "".equals(idList)){
            return ResponseResult.error(PARAM_EORRO);
        }
        var strs = idList.split(",");
        if(strs.length > 1){
            return ResponseResult.error(PARAM_EORRO);
        }
        if(Valid.isNumeric(strs[0])){
            var menus = menuService.getByParentId(Integer.parseInt(strs[0]));
            if(menus.size()>0){
                return ResponseResult.error("含有子集不能删除");
            }
            var menuOutput = menuService.getById(Integer.parseInt(strs[0]));
            menus = menuService.getByParentId(menuOutput.getParentId());
            if(menus.size() <= 1){
                Menu menu = new Menu();
                menu.setId(menuOutput.getParentId());
                menu.setHasChild(0);
                menuService.update(menuOutput.getParentId(),menu);
            }

        }

        return super.delete(idList);

    }


}
