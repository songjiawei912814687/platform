package com.personnel.controller;

import com.common.model.PageData;
import com.common.response.ResponseResult;
import com.personnel.core.base.BaseController;
import com.personnel.core.base.BaseService;
import com.personnel.domian.output.JobsOutput;
import com.personnel.model.Jobs;
import com.personnel.service.JobsService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;

@RestController
@RequestMapping(value = "/jobs")
@Api(value = "职务controller",description = "职务操作",tags = {"职务操作接口"})
public class JobsController extends BaseController<JobsOutput, Jobs,Integer> {


    @Autowired
    private JobsService jobsService;
    @Override
    public BaseService<JobsOutput,Jobs, Integer> getService() {
        return jobsService;
    }

    @Override
    @ApiOperation("新增或修改职务信息")
    @RequestMapping(value = "form", method = RequestMethod.POST)

    public ResponseResult formPost(Integer id, @Validated @RequestBody @ApiParam(name="职务信息",value="传入json格式",required=true) Jobs jobs) throws Exception {
        if(id==null){
            if(jobsService.getByName(jobs.getName()).size()>0){
                return ResponseResult.error("职务名称重复");
            }
        }else {
            if(!jobsService.isRepeat(id,jobs.getName())){
                if(jobsService.getByName(jobs.getName()).size()>0){
                    return ResponseResult.error("职务名称重复");
                }
            }
        }
     return super.formPost(id,jobs);
    }


    @Override
    @ApiOperation("删除职务信息")
    @GetMapping(value = "logicDelete")
    public ResponseResult logicDelete(String idList) throws IllegalAccessException, IntrospectionException, InvocationTargetException, MethodArgumentNotValidException {
        return super.logicDelete(idList);
    }

    @Override
    @ApiOperation("根据id获取单个职务")
    @GetMapping(value = "get")
    public ResponseResult get(Integer id) throws IllegalAccessException, IntrospectionException, InvocationTargetException {
        return super.get(id);
    }


    @GetMapping(value = "findPageList")
    @ApiOperation("获取分页的职务")
    @ApiImplicitParams({@ApiImplicitParam(name="name",value="职务名称",required=false,dataType="string", paramType = "query")})
    public ResponseResult findPageList(HttpServletRequest request){
        PageData pageData = new PageData(request);
        return super.selectPageList(pageData);
    }

    @ApiOperation("获取所有不分页职务信息")
    @GetMapping(value = "selectAll")
    public ResponseResult selectAll(HttpServletRequest request){
        return  super.selectAll(new PageData(request));
    }


    /**
     * 导出职务信息
     *
     * @param
     * @return
     */
    @RequestMapping(value = "export", method = RequestMethod.GET)
    public ResponseResult export(HttpServletResponse response, HttpServletRequest request) {
        try {
            String str = jobsService.export(response,request);
            return  ResponseResult.success(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseResult.error("获取报表失败");
    }

}
