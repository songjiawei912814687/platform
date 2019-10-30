package com.api.controller;

import com.api.model.Lighthouse;
import com.api.service.LighthouseService;
import com.common.response.ResponseResult;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.xml.rpc.ServiceException;
import java.rmi.RemoteException;
import java.util.List;

/**
 * @author: young
 * @project_name: svn
 * @description: 曝光台
 * @date: Created in 2018-12-24  11:03
 * @modified by:
 */

@RestController
@RequestMapping("lighthouse")
public class LighthouseController {

    @Autowired
    private LighthouseService lighthouseService;

    @GetMapping("save")
    public ResponseResult save() throws ServiceException, RemoteException {
        Integer result = lighthouseService.getLightHouse();
        if(result<0){
            return ResponseResult.error("新增失败");
        }
        return ResponseResult.success();
    }


    @GetMapping("findPageList")
    public ResponseResult findPageList(HttpServletRequest request){
        List<Lighthouse> lighthouseList = lighthouseService.findPageList(request);
        if(lighthouseList == null){
            return ResponseResult.success();
        }
        PageInfo pageInfo = new PageInfo(lighthouseList);
        return ResponseResult.success(pageInfo);
    }
}
