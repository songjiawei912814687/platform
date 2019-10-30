package com.meeting.controller;

import com.common.response.ResponseResult;
import com.meeting.core.base.BaseController;
import com.meeting.core.base.BaseService;
import com.meeting.domain.input.PermissionsInput;
import com.meeting.domain.output.RoomOrganOutput;
import com.meeting.model.RoomOrgan;
import com.meeting.service.RoomOrganService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("roleOrgan")
public class RoomOrganController extends BaseController<RoomOrganOutput, RoomOrgan,Integer> {

    @Autowired
    private RoomOrganService roomOrganService;

    @Override
    public BaseService<RoomOrganOutput, RoomOrgan, Integer> getService() {
        return roomOrganService;
    }

    @PostMapping(value = "savePermissions")
    public ResponseResult savePermissions(@RequestBody PermissionsInput permissionsInput){
        if(permissionsInput.getRoomId() == null){
            return ResponseResult.error(PARAM_EORRO);
        }
        var result = roomOrganService.savePermissions(permissionsInput.getRoomId(),permissionsInput);
        if(result >= 0){
            return ResponseResult.success();
        }
        return ResponseResult.error(SYS_EORRO);
    }


    @GetMapping(value = "getOrganByRoom")
    public ResponseResult getOrganByRole(Integer roomId){
        if(roomId == null){
            return ResponseResult.error(PARAM_EORRO);
        }
        var result = roomOrganService.getOrganByRoom(roomId);
        return ResponseResult.success(result);
    }
}
