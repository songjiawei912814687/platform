package com.api.controller;

import com.api.service.StaffManagementService;
import com.common.response.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "staffManagement")
public class StaffManagementController {
    @Autowired
    private StaffManagementService staffManagementService;

    @GetMapping(value = "getStaffManagement")
    public ResponseResult getStaffManagement(){
        return ResponseResult.success(staffManagementService.getByOrganization());
    }
}
