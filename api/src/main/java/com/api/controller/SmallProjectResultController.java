package com.api.controller;

import com.api.model.SmallProjectResult;
import com.api.service.SmallProjectResultService;
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
 * @description: 获取小额工程成交结果公示接口设计
 * @date: Created in 2018-12-24  10:56
 * @modified by:
 */
@RestController
@RequestMapping("smallProjectResult")
public class SmallProjectResultController {

    @Autowired
    private SmallProjectResultService smallProjectResultService;

    @GetMapping("save")
    public ResponseResult save() throws ServiceException, RemoteException {
        Integer result = smallProjectResultService.getSmallProjectResult();
        if(result<0){
            return ResponseResult.error("新增失败");
        }
        return ResponseResult.success("新增成功");
    }

    @GetMapping("findPageList")
    public ResponseResult findPageList(HttpServletRequest request){
        List<SmallProjectResult> smallProjectResultList =  smallProjectResultService.findPageList(request);
        if(smallProjectResultList == null){
            return ResponseResult.success();
        }
        PageInfo pageInfo = new PageInfo(smallProjectResultList);
        return ResponseResult.success(pageInfo);
    }
}
