package com.screenData.controller;

import com.common.response.ResponseResult;
import com.common.utils.HttpRequestUtil;
import com.common.utils.Iat;
import com.common.utils.Valid;
import com.screenData.core.base.BaseController;
import com.screenData.core.base.BaseService;
import com.screenData.domain.output.ScreenConfigOutput;
import com.screenData.model.ScreenConfig;
import com.screenData.service.ScreenConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.beans.IntrospectionException;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

@RestController
@RequestMapping("screenConfig")
@Api(value = "大屏参数controller",description = "大屏参数操作",tags = {"大屏参数操作接口"})
public class ScreenConfigController extends BaseController<ScreenConfigOutput, ScreenConfig,Integer> {

    @Autowired
    private ScreenConfigService screenConfigService;

    @Override
    public BaseService<ScreenConfigOutput, ScreenConfig, Integer> getService() {
        return screenConfigService;
    }



    @ApiOperation("服务承诺")
    @GetMapping("/getPledgeData")
    public ResponseResult getPledgeData() {
        return screenConfigService.getPledgeData();
    }

    @ApiOperation("移动办事之城")
    @GetMapping("/getMobileService")
    public ResponseResult getMobileService() {
        return screenConfigService.getMobileService();
    }




    @Override
    @PostMapping(value = "form")
    public ResponseResult formPost(Integer id, @RequestBody ScreenConfig screenConfig) throws Exception {
        if (screenConfig == null) {
            return ResponseResult.error(PARAM_ERROR);
        }

        if (id == null) {
            screenConfig.setState(0);
            screenConfig.setHisChild(0);
            if(screenConfig.getParentId() <= 0){
                return super.formPost(id,screenConfig);
            }
            if (screenConfig.getParentId() <= 0) {
                return super.formPost(id, screenConfig);
            }
            var parentConfig = screenConfigService.getById(screenConfig.getParentId());
            if (parentConfig == null) {
                return ResponseResult.error(PARAM_ERROR);
            }
            parentConfig.setHisChild(1);
            screenConfigService.add(parentConfig);
            return super.formPost(id, screenConfig);
        } else {
            var originalConfig = screenConfigService.getById(id);
            if (originalConfig == null) {
                return ResponseResult.error(PARAM_ERROR);
            }
            if (originalConfig.getParentId().equals(screenConfig.getParentId())) {
                return super.formPost(id, screenConfig);
            }
            if (screenConfigService.getListByParentId(originalConfig.getParentId()).size() <= 0) {
                screenConfigService.updateParentConfig(originalConfig.getParentId(), 0);
                return super.formPost(id, screenConfig);
            }
            if (screenConfigService.getListByParentId(screenConfig.getParentId()).size() <= 0) {
                screenConfigService.updateParentConfig(screenConfig.getParentId(), 1);
                return super.formPost(id, screenConfig);
            }

        }
        return ResponseResult.success();
    }


    @GetMapping(value = "delete")
    public ResponseResult deleted(String idList) throws InvocationTargetException, IntrospectionException, MethodArgumentNotValidException, IllegalAccessException {
        if (idList == null || "".equals(idList)) {
            return ResponseResult.error(PARAM_ERROR);
        }
        var strs = idList.split(",");
        if (strs.length > 1) {
            return ResponseResult.error(PARAM_ERROR);
        }
        if (Valid.isNumeric(strs[0])) {
            var screenConfigs = screenConfigService.getListByParentId(Integer.parseInt(strs[0]));
            if (screenConfigs.size() > 0) {
                return ResponseResult.error("含有子集不能删除");
            }
            var screenConfigOutput = screenConfigService.getById(Integer.parseInt(strs[0]));
            screenConfigs = screenConfigService.getListByParentId(screenConfigOutput.getParentId());
            if(screenConfigs.size() <= 1){
                ScreenConfig screenConfig = new ScreenConfig();
                screenConfig.setId(screenConfigOutput.getParentId());
                screenConfig.setHisChild(0);
                screenConfigService.update(screenConfigOutput.getParentId(),screenConfig);
            }

        }
        return super.delete(idList);
    }

    @GetMapping(value = "getTree")
    public ResponseResult getTree(){
        var screenConfigs = screenConfigService.findAll();
        return ResponseResult.success(screenConfigService.toZtree(screenConfigs));
    }


    @GetMapping(value = "selectAllList")
    public ResponseResult selectAllList(HttpServletRequest request) {
        return ResponseResult.success(screenConfigService.findTree());
    }

    @Override
    @GetMapping(value = "get")
    public ResponseResult get(Integer id){
        return super.selectById(id);
    }


    @GetMapping(value = "getByKey")
    public ResponseResult getByKey(String key) {
        if (key == null || "".equals(key))
            return ResponseResult.error(PARAM_ERROR);
        return ResponseResult.success(screenConfigService.getByKey(key));
    }

    @GetMapping(value = "getListByKey")
    public ResponseResult getListByKey(String key) {
        if (key == null || "".equals(key))
            return ResponseResult.error(PARAM_ERROR);
        return ResponseResult.success(screenConfigService.getListByKey(key));
    }


    @PostMapping(value = "uploadWav")
    public ResponseResult upload(@RequestParam("file") MultipartFile file) throws Exception {
        String fileName = file.getOriginalFilename();
        //获取末尾的扩展名
        fileName += ".wav";
        int hasCode = fileName.hashCode();
        String hex = Integer.toString(hasCode);
        String filePath = "D:\\audio\\";
//        String filePath = "/Users/lee/lee/uploadFile";
        String returnPath = "/" + hex.charAt(0) + "/" + hex.charAt(1) + "/" + System.currentTimeMillis() + "_" + fileName;
        filePath = filePath + "/" + returnPath;

        File fileDir = new File(filePath);
        if (!fileDir.getParentFile().exists()) {
            fileDir.getParentFile().mkdirs();
        }
        InputStream inputStream = file.getInputStream();
        FileOutputStream fos = new FileOutputStream(fileDir);
        BufferedOutputStream bos = new BufferedOutputStream(fos, 1024);
        Integer length = 0;
        byte[] buffer = new byte[1024];
        while ((length = inputStream.read(buffer)) != -1) {
            fos.write(buffer, 0, length);
        }
        bos.flush();
        bos.close();
        inputStream.close();
        file.transferTo(fileDir);


        var str = Iat.aiuiWebApi(filePath);
        System.out.println(str);

        return ResponseResult.success(str);

    }


    @GetMapping(value = "getToken")
    public ResponseResult getToken() {
        var token = HttpRequestUtil.sendGet("http://10.32.250.86:1025/home/ssoTokenKey.action?&t=1540805706115", null);
        return ResponseResult.success(token);
    }





}
