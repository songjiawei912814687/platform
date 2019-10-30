package com.selfservice.controller;

import com.common.response.ResponseResult;
import com.selfservice.model.FirstForm;
import com.selfservice.model.FourthForm;
import com.selfservice.model.SecondForm;
import com.selfservice.model.ThirdForm;
import com.selfservice.service.FormApplicationService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;


@RestController
@Api(value = "自助填单controller",description = "自助填单操作",tags = {"自助填单接口"})
@RequestMapping("form_application")
public class FormApplicationController {

    @Autowired
    private FormApplicationService formApplicationService;

    /**自助填单新增**/
    @PostMapping(value = "add")
    public ResponseResult downLoadMessage(@RequestBody FirstForm object,HttpServletRequest request) {
        ResponseResult result = formApplicationService.printForm1(object, request);
        return result;
    }
    /**自助填单新增**/
    @PostMapping(value = "add2")
    public ResponseResult downLoadMessage2(@RequestBody SecondForm object2, HttpServletRequest request) {
        ResponseResult result = formApplicationService.printForm2(object2, request);
        return result;
    }
    /**自助填单新增**/
    @PostMapping(value = "add3")
    public ResponseResult downLoadMessage3(@RequestBody ThirdForm object3, HttpServletRequest request) {
        ResponseResult result = formApplicationService.printForm3(object3, request);
        return result;
    }

    /**自助填单新增**/
    @PostMapping(value = "add4")
    public ResponseResult downLoadMessage3(@RequestBody FourthForm object4, HttpServletRequest request) {
        ResponseResult result = formApplicationService.printForm4(object4, request);
        return result;
    }
}
