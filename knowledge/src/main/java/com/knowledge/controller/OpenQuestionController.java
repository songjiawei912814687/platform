package com.knowledge.controller;

import com.common.model.PageData;
import com.common.response.ResponseResult;
import com.knowledge.core.base.BaseController;
import com.knowledge.core.base.BaseService;
import com.knowledge.domain.output.QuestionOutput;
import com.knowledge.model.Question;
import com.knowledge.service.QuestionService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("openquestion")
public class OpenQuestionController extends BaseController<QuestionOutput,Question,Integer> {
    @Autowired
    private QuestionService questionService;

    @Override
    public BaseService<QuestionOutput, Question, Integer> getService() {
        return questionService;
    }

    @GetMapping(value = "findPageList")
    @ApiImplicitParams({@ApiImplicitParam(name="title",value="问题标题",required=false,dataType="String", paramType = "query")})
    public ResponseResult selectPageList(HttpServletRequest request){
        PageData pageData = new PageData(request);
        pageData.put("answerState","1");
        return questionService.selectByAnswerState(pageData);
    }

    @Override
    @GetMapping(value = "get")
    @ApiOperation(value="根据id获取单个咨询问题")
    public ResponseResult get(Integer id) {
        return super.selectById(id);
    }

    @GetMapping(value = "getQustionOrganizations")
    public ResponseResult getQustionOrganizations(){
        PageData pageData = new PageData();
        pageData.put("answerState","1");
        return questionService.selectQustionOrganizations(pageData);
    }


}

