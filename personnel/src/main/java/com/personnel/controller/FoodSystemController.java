package com.personnel.controller;

import com.common.response.ResponseResult;
import com.personnel.domian.input.FoodEmpIdInput;
import com.personnel.domian.input.FoodOrganiztionInput;
import com.personnel.domian.output.GardenOutput;
import com.personnel.domian.output.IdentityOutput;
import com.personnel.service.FoodSystemService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * @author: young
 * @project_name: svn
 * @description:
 * @date: Created in 2019-01-07  17:52
 * @modified by:
 */
@RestController
@RequestMapping("foodSystem")
public class FoodSystemController {

    @Autowired
    private FoodSystemService foodSystemService;

    /**获取token*/
    @GetMapping("getToken")
    public ResponseResult getToken(){

        String accessToken= foodSystemService.getToken();
        if(accessToken == null){
            return ResponseResult.error("没有获取到token");
        }
        return ResponseResult.success(accessToken);
    }

    /**获取身份参数**/
    @PostMapping("getAccClass")
    public ResponseResult getAccClass() throws IOException {
        IdentityOutput identityOutput = foodSystemService.getAccClass();
        if(identityOutput == null){
            return ResponseResult.error("没有获取身份参数");
        }
        return ResponseResult.success(identityOutput);
    }

    /**获取园区参数**/
    @PostMapping("getArea")
    public ResponseResult getArea() throws IOException {
        GardenOutput gardenOutput = foodSystemService.getArea();
        if(gardenOutput == null){
            return ResponseResult.error("没有获取到园区参数");
        }
        return ResponseResult.success(gardenOutput);
    }


    /**新增组织中间表**/
    @GetMapping("addFoodOrganization")
    public ResponseResult addFoodOrganization(){
        Integer id =foodSystemService.addFoodOrganization();
        if(id== 0){
            return ResponseResult.error("手动新增中间组织失败");
        }
        return ResponseResult.success("手动新增中间组织成功");
    }

    /**新增人员中间表**/
    @GetMapping("addAllFoodEmp")
    public ResponseResult addAllFoodEmp() throws IOException {
        Integer result =foodSystemService.addAllFoodEmp();
        if(result == 0){
            return ResponseResult.error("手动新增中间人员失败");
        }
        return ResponseResult.success("手动新增中间人员成功");
    }

    @GetMapping("delEmp")
    public ResponseResult delEmp(String sId){
        foodSystemService.delFoodEmp(sId);
        return ResponseResult.success();
    }

    /**获取卡户部门*/
    @PostMapping("getOrga")
    @ApiOperation("获取卡户部门信息")
    public ResponseResult getOrga(@RequestBody  FoodOrganiztionInput foodOrganiztionInput) throws IOException {
        return foodSystemService.getOrga(foodOrganiztionInput);
    }

    /**获取卡户信息*/
    @PostMapping("getEmpInfo")
    @ApiOperation("获取卡户信息")
    public ResponseResult getEmpInfo(@RequestBody FoodEmpIdInput foodEmpIdInput) throws IOException {
        return foodSystemService.getEmpInfo(foodEmpIdInput);
    }


}
