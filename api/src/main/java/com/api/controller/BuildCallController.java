package com.api.controller;

import com.api.model.BuildCall;
import com.api.service.BuildCallService;
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
 * @description: 建筑工程招标
 * @date: Created in 2018-12-24  10:39
 * @modified by:
 */
@RestController
@RequestMapping("/buildCall")
public class BuildCallController {

    @Autowired
    private BuildCallService buildCallService;

    @GetMapping("save")
    public ResponseResult save() throws ServiceException, RemoteException {
        Integer result = buildCallService.getBuildCall();
        if(result<0){
            return ResponseResult.error("新增建筑工程中标失败");
        }
        return ResponseResult.success("新增成功");
    }

    @GetMapping("findPageList")
    public ResponseResult findPageList(HttpServletRequest request){
        List<BuildCall> buildCallList = buildCallService.findPageList(request);
        if(buildCallList==null||buildCallList.size() == 0){
            return ResponseResult.success();
        }
        PageInfo pageInfo = new PageInfo(buildCallList);

        return ResponseResult.success(pageInfo);
    }
}
