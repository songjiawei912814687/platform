package com.knowledge.controller;

import com.common.model.PageData;
import com.common.response.ResponseResult;
import com.knowledge.core.base.BaseController;
import com.knowledge.core.base.BaseService;
import com.knowledge.model.Answer;
import com.knowledge.service.AnswerService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;

public class AnswerController extends BaseController<Answer,Answer,Integer> {
    @Autowired
    private AnswerService answerService;
    @Override
    public BaseService<Answer, Answer, Integer> getService() {
        return answerService;
    }


    @Override
    @ApiOperation("新增或修改咨询问题答案")
    @RequestMapping(value = "form", method = RequestMethod.POST)
    public ResponseResult formPost(Integer id, @Validated @RequestBody @ApiParam(name="指标信息",value="传入json格式",required=true) Answer answer) throws Exception {

        return  super.formPost(id,answer);
    }

    @ApiOperation("删除咨询问题答案")
    @GetMapping(value = "logicDelete")
    public ResponseResult logicDelete(String id) throws IllegalAccessException, IntrospectionException, InvocationTargetException, MethodArgumentNotValidException {
        return super.logicDelete(id);
    }

    @Override
    @GetMapping(value = "get")
    @ApiOperation(value="根据id获取单个咨询问题问题")
    public ResponseResult get(Integer id) {
        return super.selectById(id);
    }

    @ApiOperation("获取所有咨询问题答案")
    @GetMapping(value = "selectAll")
    public ResponseResult selectAll(HttpServletRequest request){
        return  super.selectAll(new PageData(request));
    }

    @GetMapping(value = "findPageList")
    @ApiImplicitParams({@ApiImplicitParam(name="questionId",value="问题Id",required=false,dataType="int", paramType = "query")})
    public ResponseResult selectPageList(HttpServletRequest request){
        PageData pageData = new PageData(request);
        return super.selectPageList(pageData);
    }
}
