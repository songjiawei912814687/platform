package com.api.controller;

import com.api.model.SmallProjectTrans;
import com.api.service.SmallProjectTransService;
import com.common.response.ResponseResult;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.xml.rpc.ServiceException;
import java.io.IOException;
import java.util.List;

/**
 * @author: young
 * @project_name: svn
 * @description: 小额工程交易公告
 * @date: Created in 2018-12-24  11:00
 * @modified by:
 */
@RestController
@RequestMapping("smallProjectTrans")
public class SmallProjectTransController {

    @Autowired
    private SmallProjectTransService smallProjectTransService;

    @GetMapping("save")
    public ResponseResult save() throws IOException, ServiceException {
        Integer result = smallProjectTransService.getSmallProjectTrans();
        if(result<0){
            return ResponseResult.error("新增失败");
        }
        return ResponseResult.success();
    }

    @GetMapping("findPageList")
    public ResponseResult findPageList(HttpServletRequest request){
        List<SmallProjectTrans> smallProjectTransList =  smallProjectTransService.findPageList(request);
        if(smallProjectTransList == null){
            return null;
        }

        PageInfo pageInfo = new PageInfo(smallProjectTransList);
        return ResponseResult.success(pageInfo);
    }
}
