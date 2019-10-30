package com.stamp.service;

import com.common.model.PageData;
import com.stamp.core.base.BaseRepository;
import com.stamp.core.base.BaseService;
import com.stamp.core.base.MybatisBaseMapper;
import com.stamp.domain.input.PermissionsInput;
import com.stamp.domain.output.OrganPermissterOutput;
import com.stamp.domain.output.RoleOrganOutput;
import com.stamp.mapper.jpa.RoleOrganRepository;
import com.stamp.mapper.mybatis.OrganizationMapper;
import com.stamp.mapper.mybatis.RoleOrganMapper;
import com.stamp.model.Organization;
import com.stamp.model.RoleOrgan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class RoleOrganService extends BaseService<RoleOrganOutput, RoleOrgan,Integer> {

    @Autowired
    private RoleOrganRepository roleOrganRepository;

    @Autowired
    private RoleOrganMapper roleOrganMapper;

    @Autowired
    private OrganizationMapper organizationMapper;


    @Override
    public BaseRepository<RoleOrgan, Integer> getMapper() {
        return roleOrganRepository;
    }

    @Override
    public MybatisBaseMapper getMybatisBaseMapper() {
        return roleOrganMapper;
    }

    public List<OrganPermissterOutput> getOrganByRole(Integer roleId) {
        List<OrganPermissterOutput> currentOrganList = null;
        if(getUsers().getAdministratorLevel() != 9){
            PageData pageData = new PageData();
            pageData.put("userId",getUsers().getId());
            if(getUsers().getUserType()==1){//部门账号为部门本身和部门账号权限内的组织
                Organization organizationOutput = organizationMapper.selectOrNoByOrId(getUsers().getOrganizationId());
                pageData.put("path",organizationOutput.getPath());
                pageData.put("linkedId",organizationOutput.getId());
            }
            currentOrganList = roleOrganMapper.findOrganByuserId(pageData);
        }else {
            currentOrganList = roleOrganMapper.findAllOrgan();
        }
        if(currentOrganList == null || currentOrganList.size() <= 0 ){
            return null;
        }
        var roleOrgans = roleOrganRepository.findAllByRoleId(roleId);
        if(roleOrgans != null && roleOrgans.size() > 0){
            for(var ro : roleOrgans){
                for(var co : currentOrganList){
                    if(co.getId().equals(ro.getOrganId())){
                        co.setCheckState(1);
                        continue;
                    }
                }
            }
        }

        var out = getOrganOutput(currentOrganList,0);

        return out;



    }

    private List<OrganPermissterOutput> getOrganOutput(List<OrganPermissterOutput> list,Integer parentId){
        List<OrganPermissterOutput> out = new ArrayList<>();
        var firstList = list.stream().filter(OrganPermissterOutput -> OrganPermissterOutput.getParentId().equals(parentId)).collect(toList());
        if(firstList == null || firstList.size() <= 0){
            return null;
        }
        out.addAll(firstList);
        for(var organ : out){
            var l = getOrganOutput(list,organ.getId());
            if(l == null){
                continue;
            }
            organ.setChildren(l);
        }

        return out;
    }

    @Transactional
    public int savePermissions(Integer roleId, PermissionsInput permissionsInput) {
        roleOrganRepository.deleteAllByRoleId(roleId);
        if(permissionsInput == null || permissionsInput.getRoleMenuIntputs() == null || permissionsInput.getRoleMenuIntputs().size() <= 0){
            return SUCCESS;
        }
        List<RoleOrgan> list = new ArrayList<>();
        for(var o : permissionsInput.getRoleMenuIntputs()){
            RoleOrgan roleOrgan = new RoleOrgan();
            roleOrgan.setOrganId(o.getId());
            roleOrgan.setRoleId(roleId);
            list.add(roleOrgan);
        }
        var result = roleOrganRepository.saveAll(list);
        if(result == null || result.size() <= 0){
            return ERROR;
        }else{
            return SUCCESS;
        }
    }

    @Async
    public void deleteRoleOrgan(Integer roleId){
        roleOrganRepository.deleteAllByRoleId(roleId);
    }
}
