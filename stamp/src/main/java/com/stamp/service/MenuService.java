package com.stamp.service;


import com.stamp.core.base.BaseRepository;
import com.stamp.core.base.BaseService;
import com.stamp.core.base.MybatisBaseMapper;
import com.stamp.domain.output.MenuOutput;
import com.stamp.domain.output.MenuTree;
import com.stamp.mapper.jpa.MenuRepository;
import com.stamp.mapper.mybatis.MenuMapper;
import com.stamp.model.Menu;
import com.stamp.model.UserRole;
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
    public BaseRepository<Menu, Integer> getMapper() {
        return repository;
    }

    @Override
    public MybatisBaseMapper<MenuOutput> getMybatisBaseMapper() {
        return menuMapper;
    }


    public List<MenuOutput> getAllList(){
        var menus = menuMapper.findAll();
        return getMenuOutput(menus,0);
    }

    public List<MenuOutput> findAll(){
        return menuMapper.findAll();
    }

    public List<MenuOutput> getMenuOutput(List<MenuOutput> menus,Integer parentId) {
        List<MenuOutput> out = new ArrayList<>();
        List<MenuOutput> firstMenu = menus.stream().filter(Menu -> Menu.getParentId().equals(parentId)).sorted(Comparator.comparingInt(Menu::getDisPlayOrderBy)).collect(toList());
        out.addAll(firstMenu);
        for (MenuOutput menuOutput : out) {
            if (menuOutput.getHasChild() != null && menuOutput.getHasChild() == 1) {
                List<MenuOutput> list = menus.stream().filter(Menu -> Menu.getParentId() .equals(menuOutput.getId())).sorted(Comparator.comparingInt(Menu::getDisPlayOrderBy)).collect(toList());
                menuOutput.setChildren(list);
                getMenuOutput(menus,menuOutput.getId());
            } else {
                continue;
            }
        }
        return out;
    }
    public List<MenuTree> toZtree(List<MenuOutput> all) {
        List<MenuTree> parentArray = new ArrayList<>();
        for (var o:all) {
            if(o.getParentId()==0){
                parentArray.add(new MenuTree(o));
            }
        }
        parentArray = parentArray.stream().sorted(Comparator.comparingInt(MenuTree::getDisplayOrder)).collect(toList());
        return getTree(all,parentArray);
    }
    private List<MenuTree> getTree(List<MenuOutput> list, List<MenuTree> parents) {
        for (MenuTree parentOrg:parents) {
            List<MenuTree> childs = new ArrayList<>();
            for (MenuOutput o:list) {
                if(parentOrg.getKey().equals(o.getParentId())){
                    childs.add(new MenuTree(o));
                }
            }
            childs = childs.stream().sorted(Comparator.comparingInt(MenuTree::getDisplayOrder)).collect(toList());
            parentOrg.setChildren(childs.size()==0?null:childs);
            if(childs.size()>0){
                getTree(list,childs)  ;
            }
        }
        return parents;
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
        if(menus == null || menus.size() <= 0){
            return null;
        }
        if(sys.size() > 1){
            return null;
        }
        return getMenuOutput(menus,sys.get(0).getId()).stream().sorted(Comparator.comparingInt(MenuOutput::getDisPlayOrderBy)).collect(toList());
    }


    public List<MenuOutput> getByParentId(Integer parentId){
        return menuMapper.findByParentId(parentId);
    }

    public List<MenuOutput> getByIdIn(List<Integer> ids){
        return menuMapper.findByRoles(ids);
    }

    public List<MenuOutput> selectAllToW(){
        return menuMapper.selectAllToW();
    }
}
