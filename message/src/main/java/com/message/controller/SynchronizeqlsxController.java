package com.message.controller;


import com.common.response.ResponseResult;
import com.message.service.QlsxService;
import com.message.service.SynchronizeqlsxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.sql.SQLException;

/**
 * @author: Young
 * @description:
 * @date: Created in 18:34 2018/10/23
 * @modified by:
 */
@RestController
@RequestMapping("jobs")
public class SynchronizeqlsxController  {

    @Autowired
    private SynchronizeqlsxService synchronizeqlsxService;

    @Autowired
    private QlsxService qlsxService;

    @GetMapping("/synchronize")
    public ResponseResult SynchronizeBjsb()throws Exception{
       return synchronizeqlsxService.synchronizeDate();
    }

    @GetMapping("/sysQlsxSource")
    public ResponseResult sysQlsxSource()throws Exception{
        return ResponseResult.success("跟新完成"+qlsxService.sysQlsxSource()+"条数据") ;
    }
}
