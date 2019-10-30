package com.attendance.controller;

import com.attendance.core.base.BaseController;
import com.attendance.core.base.BaseService;
import com.attendance.domian.output.AttendanceRuleOutput;
import com.attendance.mapper.mybatis.AttendanceRuleMapper;
import com.attendance.model.AttendanceRule;
import com.attendance.service.AttendanceRuleService;
import com.common.model.PageData;
import com.common.response.ResponseResult;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;

@RestController
@RequestMapping(value = "/attendanceRule")
@Api(value = "考勤规则controller",description = "考勤规则操作",tags = {"考勤规则操作接口"})
public class AttendanceRuleController extends BaseController<AttendanceRuleOutput, AttendanceRule,Integer> {
    @Autowired
    private AttendanceRuleService attendanceRuleService;
    @Autowired
    private AttendanceRuleMapper attendanceRuleMapper;
    @Override
    public BaseService<AttendanceRuleOutput,AttendanceRule, Integer> getService() {
        return attendanceRuleService;
    }

    @Override
    @ApiOperation("新增或修改考勤规则信息")
    @RequestMapping(value = "form", method = RequestMethod.POST)
    public ResponseResult formPost(Integer id, @RequestBody @ApiParam(name="考勤规则信息",value="传入json格式",required=true) AttendanceRule attendanceRule) throws Exception {
        if(attendanceRule.getName()==null||"".equals(attendanceRule.getName())
                ||attendanceRule.getValue()==null||attendanceRule.getValue().equals("")
                ||attendanceRule.getState()==null||attendanceRule.getState().equals("")){
            return ResponseResult.error(PARAM_EORRO);
        }
        if(id==null){
            if(attendanceRuleService.getByName(attendanceRule.getName()).size()>0){
                return ResponseResult.error("名称重复");
            }
        }else {
            if(!attendanceRuleService.isRepeat(id,attendanceRule.getName())){
                if(attendanceRuleService.getByName(attendanceRule.getName()).size()>0){
                    return ResponseResult.error("名称重复");
                }
            }
        }
        return super.formPost(id,attendanceRule);
    }

    @Override
    @ApiOperation("删除考勤规则信息")
    @GetMapping(value = "logicDelete")
    public ResponseResult logicDelete(String idList) throws IllegalAccessException, IntrospectionException, InvocationTargetException, MethodArgumentNotValidException {
        return super.logicDelete(idList);
    }

    @ApiOperation("启用考勤规则信息")
    @GetMapping(value = "start")
    public ResponseResult start(String idList) throws IllegalAccessException, IntrospectionException, InvocationTargetException, MethodArgumentNotValidException {
        if (idList == null || idList.length() <= 0) {
            return ResponseResult.error(PARAM_EORRO);
        }
        int result = attendanceRuleService.updatestate(idList,1);
        if (result < 0) {
            return ResponseResult.error(SYS_EORRO);
        }


        return ResponseResult.success();
    }

    @ApiOperation("停用考勤规则信息")
    @GetMapping(value = "stop")
    public ResponseResult stop(String idList) throws IllegalAccessException, IntrospectionException, InvocationTargetException, MethodArgumentNotValidException {
        if (idList == null || idList.length() <= 0) {
            return ResponseResult.error(PARAM_EORRO);
        }
        int result = attendanceRuleService.updatestate(idList,3);
        if (result < 0) {
            return ResponseResult.error(SYS_EORRO);
        }

        return ResponseResult.success();
    }

    //获取所有启用的考勤规则
    @GetMapping(value = "select_in_use")
    public ResponseResult selectInUse(){
        return ResponseResult.success(attendanceRuleMapper.selectInUse());
    }

    @Override
    @ApiOperation("根据id获取考勤规则")
    @GetMapping(value = "selectById")
    public ResponseResult selectById(Integer id) {
        return super.selectById(id);
    }

    @Override
    @ApiOperation("根据id获取考勤规则")
    @GetMapping(value = "get")
    public ResponseResult get(Integer id) {
        return super.selectById(id);
    }

    @GetMapping(value = "findPageList")
    @ApiOperation("获取分页的考勤规则")
    @ApiImplicitParams({@ApiImplicitParam(name="name",value="考勤规则名称",required=false,dataType="string", paramType = "query")})
    public ResponseResult findPageList(HttpServletRequest request){
        PageData pageData = new PageData(request);
        pageData.put("amputated",0);
        return super.selectPageList(pageData);
    }
}
