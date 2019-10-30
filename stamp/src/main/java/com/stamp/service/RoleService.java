package com.stamp.service;

import com.common.model.PageData;
import com.github.pagehelper.PageHelper;
import com.stamp.core.base.BaseRepository;
import com.stamp.core.base.BaseService;
import com.stamp.core.base.MybatisBaseMapper;
import com.stamp.domain.output.RoleOutput;
import com.stamp.mapper.jpa.RoleRepository;
import com.stamp.mapper.mybatis.RoleMapper;
import com.stamp.model.Role;
import com.stamp.model.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.transaction.Transactional;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Service
public class RoleService extends BaseService<RoleOutput, Role,Integer> {
    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private RoleActionService roleActionService;

    @Autowired
    private RoleOrganService roleOrganService;

    @Override
    public BaseRepository<Role, Integer> getMapper() {
        return roleRepository;
    }

    @Override
    public MybatisBaseMapper<RoleOutput> getMybatisBaseMapper() {
        return roleMapper;
    }


    public List<Role> getByUserId(Integer userId, PageData pageData) {
        Integer pagesize = pageData.getRows();
        Integer page = pageData.getPageIndex();
        PageHelper.startPage(page, pagesize);
        if(getUsers().getAdministratorLevel()!=9){
            return roleMapper.findByUserId(getUsers().getId(),userId);
        }else{
            return roleMapper.findByUserId(null,userId);
        }
    }

    public List<Role> getByUserIdNotIn(Integer userId,PageData pageData) {
        Integer pagesize = pageData.getRows();
        Integer page = pageData.getPageIndex();
        PageHelper.startPage(page, pagesize);
        return roleMapper.findByUserIdNotIn(getUsers().getId(),userId);
    }

    @Async
    public void setDefaultRole(Integer userId) throws InvocationTargetException, IntrospectionException, MethodArgumentNotValidException, IllegalAccessException {
        var roles = roleRepository.findAllByDefaultPermissions(1);
        if(roles == null || roles.size() <= 0){
            return;
        }
        for(var r : roles){
            var userRole = new UserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(r.getId());
            userRoleService.add(userRole);
        }

    }

    @Override
    @Transactional
    public int deleteById(String idList) {
        String[] strs = idList.split(",");
        for(var str : strs){
            Integer id = Integer.parseInt(str);
            roleMapper.deleteByPrimaryKey(id);
            userRoleService.deleteByRoleId(id);
            roleActionService.deleteRoleActionAndRoleMenu(id);
            roleOrganService.deleteRoleOrgan(id);
        }
        return SUCCESS;
    }

}
