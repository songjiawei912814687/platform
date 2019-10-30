package com.meeting.service;

import com.common.model.PageData;
import com.meeting.core.base.BaseMapper;
import com.meeting.core.base.BaseService;
import com.meeting.core.base.MybatisBaseMapper;
import com.meeting.domain.input.PermissionsInput;
import com.meeting.domain.output.OrganPermissterOutput;
import com.meeting.domain.output.RoomOrganOutput;
import com.meeting.mapper.jpa.RoomOrganRepository;
import com.meeting.mapper.mybatis.OrganizationMapper;
import com.meeting.mapper.mybatis.RoomOrganMapper;
import com.meeting.model.Organization;
import com.meeting.model.RoomOrgan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class RoomOrganService extends BaseService<RoomOrganOutput, RoomOrgan,Integer> {

    @Autowired
    private RoomOrganRepository roomOrganRepository;

    @Autowired
    private RoomOrganMapper roomOrganMapper;

    @Autowired
    private OrganizationMapper organizationMapper;


    @Override
    public BaseMapper getMapper() {
        return roomOrganRepository;
    }

    @Override
    public MybatisBaseMapper getMybatisBaseMapper() {
        return roomOrganMapper;
    }

    public List<OrganPermissterOutput> getOrganByRoom(Integer roomId) {
        List<OrganPermissterOutput> currentOrganList = null;
        if(getUsers().getAdministratorLevel() != 9){
            PageData pageData = new PageData();
            pageData.put("userId",getUsers().getId());
            if(getUsers().getUserType()==1){//部门账号为部门本身和部门账号权限内的组织
                Organization organizationOutput = organizationMapper.selectOrNoByOrId(getUsers().getOrganizationId());
                pageData.put("path",organizationOutput.getPath());
                pageData.put("linkedId",organizationOutput.getId());
            }
            currentOrganList = roomOrganMapper.findOrganByuserId(pageData);
        }else {
            currentOrganList = roomOrganMapper.findAllOrgan();
        }
        if(currentOrganList == null || currentOrganList.size() <= 0 ){
            return null;
        }
        currentOrganList.sort((x, y) -> Double.compare(x.getDisplayOrder(), y.getDisplayOrder()));
        var roomOrgans = roomOrganRepository.findAllByRoomId(roomId);
        if(roomOrgans != null && roomOrgans.size() > 0){
            for(var ro : roomOrgans){
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
    public int savePermissions(Integer roomId, PermissionsInput permissionsInput) {
        roomOrganRepository.deleteAllByRoomId(roomId);
        if(permissionsInput == null || permissionsInput.getRoleMenuIntputs() == null || permissionsInput.getRoleMenuIntputs().size() <= 0){
            return SUCCESS;
        }
        List<RoomOrgan> list = new ArrayList<>();
        for(var o : permissionsInput.getRoleMenuIntputs()){
            RoomOrgan roleOrgan = new RoomOrgan();
            roleOrgan.setOrganId(o.getId());
            roleOrgan.setRoomId(roomId);
            list.add(roleOrgan);
        }
        var result = roomOrganRepository.saveAll(list);
        if(result == null || result.size() <= 0){
            return ERROR;
        }else{
            return SUCCESS;
        }
    }
}
