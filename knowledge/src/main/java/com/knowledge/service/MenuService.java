package com.knowledge.service;

import com.knowledge.core.base.BaseMapper;
import com.knowledge.core.base.BaseService;
import com.knowledge.core.base.MybatisBaseMapper;
import com.knowledge.domain.output.MenuOutput;
import com.knowledge.mapper.jpa.MenuRepository;
import com.knowledge.mapper.mybatis.MenuMapper;
import com.knowledge.model.Menu;
import com.knowledge.model.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class MenuService extends BaseService<MenuOutput, Menu,Integer> {

    @Autowired
    private MenuRepository repository;

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public BaseMapper<Menu, Integer> getMapper() {
        return repository;
    }

    @Override
    public MybatisBaseMapper<MenuOutput> getMybatisBaseMapper() {
        return menuMapper;
    }


    public List<MenuOutput> getByIdIn(List<Integer> ids){
        return menuMapper.findByRoles(ids);
    }

    public List<MenuOutput> getMenuInit(){
        List<MenuOutput> menus = new ArrayList<>();
        if(getUsers().getAdministratorLevel() != 9){
            var ids = getUsers().getRoles().stream().map(UserRole::getRoleId).collect(toList());
            if(ids == null || ids.size() <= 0){
                return null;
            }else {
                menus = menuMapper.findByRoles(ids);
            }
        }else {
            menus = menuMapper.selectAll(null);
        }
        var sys = menus.stream().filter(MenuOutput -> MenuOutput.getParentId().equals(0)).sorted(Comparator.comparingInt(Menu::getParentId)).collect(toList());
        if(menus.size() <= 0){
            return null;
        }
        if(menus.size() <= 0){
            return null;
        }
        if(sys.size() > 1){
            return null;
        }
        return getMenuOutput(menus,sys.get(0).getId()).stream().sorted(Comparator.comparingInt(MenuOutput::getDisPlayOrderBy)).collect(toList());
    }

    public List<MenuOutput> getMenuOutput(List<MenuOutput> menus,Integer parentId) {
        List<MenuOutput> out = new ArrayList<>();
        List<MenuOutput> firstMenu = menus.stream().filter(Menu -> Menu.getParentId().equals(parentId)).sorted(Comparator.comparingInt(Menu::getDisPlayOrderBy)).collect(toList());
        out.addAll(firstMenu);
        for (MenuOutput menuOutput : out) {
            if (menuOutput.getHasChild() != null && menuOutput.getHasChild() == 1) {
                List<MenuOutput> list = menus.stream().filter(Menu -> Menu.getParentId() .equals(menuOutput.getId())).collect(toList());
                menuOutput.setChildren(list);
                getMenuOutput(menus,menuOutput.getId());
            } else {
                continue;
            }
        }
        return out;
    }


}
