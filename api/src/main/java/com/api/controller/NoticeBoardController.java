package com.api.controller;

import com.api.model.NoticeBoard;
import com.api.service.NoticeBoardService;
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
 * @description: 公示栏
 * @date: Created in 2018-12-24  11:06
 * @modified by:
 */
@RestController
@RequestMapping("noticeBoard")
public class NoticeBoardController {

    @Autowired
    private NoticeBoardService noticeBoardService;

    @GetMapping("save")
    public ResponseResult save() throws ServiceException, RemoteException {

        Integer result = noticeBoardService.getNoticeBoard();
        if(result<0){
            return ResponseResult.error("新增失败");
        }
        return ResponseResult.success();
    }

    @GetMapping("findPageList")
    public ResponseResult findPageList(HttpServletRequest request){
        List<NoticeBoard> noticeBoardList = noticeBoardService.findPageList(request);
        if(noticeBoardList == null){
            return ResponseResult.success();
        }
        PageInfo pageInfo = new PageInfo(noticeBoardList);
        return ResponseResult.success(pageInfo);
    }
}
