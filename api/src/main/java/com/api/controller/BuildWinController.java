package com.api.controller;

import com.api.model.BuildWin;
import com.api.service.BuildWinService;
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
 * @description: 建设工程中标公告接口设计
 * @date: Created in 2018-12-24  10:42
 * @modified by:
 */
@RestController
@RequestMapping("buildWind")
public class BuildWinController {

    @Autowired
    private BuildWinService buildWinService;

    @GetMapping("save")
    public ResponseResult save() throws ServiceException, RemoteException {
        Integer result = buildWinService.getBuildWin();
        if(result<0){
            return ResponseResult.error("新增失败");
        }
        return ResponseResult.success("新增成功");
    }

    @GetMapping("findPageList")
    public ResponseResult findPageList(HttpServletRequest request){
        List<BuildWin> buildWinList = buildWinService.findPageList(request);
        if(buildWinList==null){
            return ResponseResult.success();
        }

        PageInfo pageInfo = new PageInfo(buildWinList);
        return ResponseResult.success(pageInfo);
    }

}
