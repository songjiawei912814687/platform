package com.stamp.service;


import com.stamp.core.base.BaseRepository;
import com.stamp.core.base.BaseService;
import com.stamp.core.base.MybatisBaseMapper;
import com.stamp.domain.input.PermissionsInput;
import com.stamp.domain.output.ActionOutput;
import com.stamp.domain.output.MenuOutput;
import com.stamp.domain.output.RoleActionOutput;
import com.stamp.mapper.jpa.RoleActionRepository;
import com.stamp.mapper.jpa.RoleMenuRepository;
import com.stamp.mapper.mybatis.RoleActionMapper;
import com.stamp.model.RoleAction;
import com.stamp.model.RoleMenu;
import com.stamp.model.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static java.util.stream.Collectors.toList;

@Service
public class RoleActionService extends BaseService<RoleActionOutput, RoleAction, Integer> {


    @Autowired
    private RoleActionMapper roleActionMapper;

    @Autowired
    private RoleActionRepository roleActionRepository;

    @Autowired
    private RoleMenuRepository roleMenuRepository;

    @Autowired
    private MenuService menuService;

    @Autowired
    private ActionService actionService;


    @Override
    public BaseRepository<RoleAction, Integer> getMapper() {
        return roleActionRepository;
    }

    @Override
    public MybatisBaseMapper<RoleActionOutput> getMybatisBaseMapper() {
        return roleActionMapper;
    }

    public List<MenuOutput> getPermissions(Integer roleId) {
        var ids = getUsers().getRoles().stream().map(UserRole::getRoleId).collect(toList());
        List<MenuOutput> logingMenu = null;
        if(getUsers().getAdministratorLevel()!=9){
            if(ids != null && ids.size() > 0){
                logingMenu = roleActionMapper.findMenuByRoleIdIn(ids);
            }

        }else {
            logingMenu = menuService.selectAllToW();
        }
        if (logingMenu.size() <= 0) {
            return null;
        }
        List<MenuOutput> currentMenu = roleActionMapper.findMenuByRoleId(roleId);


        for (var menu : logingMenu) {
            for (var subMenu : currentMenu) {
                if (menu.getId().equals(subMenu.getId())) {
                    menu.setCheckState(1);
                }
            }
        }

        List<ActionOutput> logingAction = null;
        if(getUsers().getAdministratorLevel()!=9){
            if(ids != null && ids.size() > 0){
                logingAction = actionService.getByIdIn(ids);

            }
        }else {
            logingAction = actionService.selectAllTow();
        }
        if (logingAction.size() <= 0) {
            return null;
        }
        var currentAction = actionService.findByRoleId(roleId);;


        for (var action : logingAction) {
            for (var subAction : currentAction) {
                if (action.getId().equals(subAction.getId())) {
                    action.setCheckState(1);
                }
            }
            logingMenu = new CopyOnWriteArrayList(logingMenu);
            for (var menu : logingMenu) {
                if (menu.getType() == 0 && menu.getId().equals(action.getMenuId())) {
                    menu.setHasChild(1);
                    var menuOutput = new MenuOutput();
                    menuOutput.setCheckState(action.getCheckState());
                    menuOutput.setType(1);
                    menuOutput.setParentId(action.getMenuId());
                    menuOutput.setName(action.getName());
                    menuOutput.setId(action.getId());
                    menuOutput.setCode(action.getCode());
                    menuOutput.setHasChild(0);
                    menuOutput.setDisPlayOrderBy(0);
                    logingMenu.add(menuOutput);
                }
            }
        }
        var out = menuService.getMenuOutput(logingMenu,0);

        return out;
    }

    @Transactional
    public int savePermissions(Integer roleId, PermissionsInput permissionsInput){
        roleActionRepository.deleteAllByRoleId(roleId);
        roleMenuRepository.deleteAllByRoleId(roleId);
        if(permissionsInput.getRoleMenuIntputs() == null){
            return SUCCESS;
        }
        var menus = permissionsInput.getRoleMenuIntputs();
        if(menus.size() > 0){
            var roleActions = new ArrayList<RoleAction>();
            var roleMenus = new ArrayList<RoleMenu>();


            var actionPermissions = menus.stream().filter(RoleMenuInput -> RoleMenuInput.getType() == 1).collect(toList());
            var menuPermissions = menus.stream().filter(RoleMenuInput -> RoleMenuInput.getType() == 0).collect(toList());
            for(var menuOutput : menuPermissions){
                roleMenus.add(new RoleMenu(roleId,menuOutput.getId()));
            }
            roleMenuRepository.saveAll(roleMenus);
            for(var menuOutput : actionPermissions){
                roleActions.add(new RoleAction(roleId,menuOutput.getId(),menuOutput.getParentId()));
            }
            roleActionRepository.saveAll(roleActions);
        }
        return SUCCESS;
    }


    @Async
    public void deleteRoleActionAndRoleMenu(Integer roleId){
        roleActionRepository.deleteAllByRoleId(roleId);
        roleMenuRepository.deleteAllByRoleId(roleId);
    }

//    public List<MenuOutput> getMenuOutput(List<MenuOutput> out,List<MenuOutput> list){
//        list = new CopyOnWriteArrayList<>(list);
//        for(var menuOutput : list){
//            if(menuOutput.getChildren() != null && menuOutput.getChildren().size() != 0){
//                getMenuOutput(out,menuOutput.getChildren());
//            }
//            list.remove(menuOutput.getChildren());
//            out.add(menuOutput);
//        }
//        return out;
//    }

}
