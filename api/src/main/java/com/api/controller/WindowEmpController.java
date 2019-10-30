package com.api.controller;

import com.api.service.WindowEmployeesService;
import com.common.response.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;

/**
 * @author: young
 * @project_name: svn
 * @description:
 * @date: Created in 2019-01-29  15:00
 * @modified by:
 */
@RestController
@RequestMapping("windowEmp")
public class WindowEmpController {

    @Autowired
    private WindowEmployeesService windowEmployeesService;

    @GetMapping("addWindowEmployees")
    public ResponseResult addWindowEmployees() throws IntrospectionException, MethodArgumentNotValidException, IllegalAccessException, InvocationTargetException {
        windowEmployeesService.addWindowEmployees();
        return ResponseResult.success();
    }

    @GetMapping("updateWindowEmployee")
    public ResponseResult updateWindowEmployee() throws IntrospectionException, MethodArgumentNotValidException, IllegalAccessException, InvocationTargetException {
        windowEmployeesService.updateWindowEmployee();
        return ResponseResult.success();
    }
}
