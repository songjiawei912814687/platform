package com.stamp.controller;

import com.common.model.PageData;
import com.common.response.ResponseResult;
import com.stamp.core.base.BaseController;
import com.stamp.core.base.BaseService;
import com.stamp.domain.output.ActionOutput;
import com.stamp.model.Action;
import com.stamp.service.ActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;

@RestController
@RequestMapping(value = "action")
public class ActionController extends BaseController<ActionOutput, Action, Integer> {

    @Autowired
    private ActionService actionService;

    @Override
    public BaseService<ActionOutput, Action, Integer> getService() {
        return actionService;
    }

    @Override
    @PostMapping(value = "form")
    public ResponseResult formPost(Integer id, @RequestBody Action action) throws Exception {
        return super.formPost(id, action);
    }

    @Override
    @GetMapping(value = "delete")
    public ResponseResult delete(String idList) throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        return super.delete(idList);
    }


    @GetMapping(value = "selectPageList")
    public ResponseResult selectPageList(HttpServletRequest request){
        return super.selectPageList(new PageData(request));
    }

    @Override
    @GetMapping(value = "get")
    public ResponseResult get(Integer id){
        return super.selectById(id);
    }



}
