package com.attendance.controller;

import com.attendance.core.base.BaseController;
import com.attendance.core.base.BaseService;
import com.attendance.domian.output.AttendanceRuleNewOutput;
import com.attendance.mapper.mybatis.AttendanceRuleNewMapper;
import com.attendance.model.AttendanceRuleNew;
import com.attendance.service.AttendanceRuleNewService;
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
@RequestMapping(value = "/attendanceRuleNew")
@Api(value = "考勤规则controller",description = "新考勤规则操作",tags = {"新考勤规则操作接口"})
public class AttendanceRuleNewController extends BaseController<AttendanceRuleNewOutput, AttendanceRuleNew,Integer> {
    @Autowired
    private AttendanceRuleNewService attendanceRuleNewService;
    @Autowired
    private AttendanceRuleNewMapper attendanceRuleNewMapper;
    @Override
    public BaseService<AttendanceRuleNewOutput,AttendanceRuleNew, Integer> getService() {
        return attendanceRuleNewService;
    }


    @Override
    @ApiOperation("新增或修改考勤规则信息")
    @RequestMapping(value = "form", method = RequestMethod.POST)
    public ResponseResult formPost(Integer id, @RequestBody @ApiParam(name="考勤规则信息",value="传入json格式",required=true) AttendanceRuleNew attendanceRuleNew) throws Exception {
        if(attendanceRuleNew.getName()==null||"".equals(attendanceRuleNew.getName())
                ||attendanceRuleNew.getValue()==null||attendanceRuleNew.getValue().equals("")
                ||attendanceRuleNew.getState()==null||attendanceRuleNew.getState().equals("")){
            return ResponseResult.error(PARAM_EORRO);
        }
        //判断当前考勤配置类型是否重复
        if(id == null){
            if(attendanceRuleNewService.typeIsExist(attendanceRuleNew)){
                return ResponseResult.error("当前考勤规则配置下已经存在当前类型的考勤规则");
            }
        }else {
            AttendanceRuleNewOutput attendanceRuleNewOutput = attendanceRuleNewService.selectById(id);
            if(attendanceRuleNewOutput==null){
                return ResponseResult.error("没有查到考勤规则");
            }
            if(!attendanceRuleNewOutput.getName().equals(attendanceRuleNew.getName())){
                if(attendanceRuleNewService.typeIsExist(attendanceRuleNew)){
                    return ResponseResult.error("当前考勤规则下已存在规则值");
                }
            }
        }
        return super.formPost(id,attendanceRuleNew);
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
        int result = attendanceRuleNewService.updatestate(idList,1);
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
        int result = attendanceRuleNewService.updatestate(idList,3);
        if (result < 0) {
            return ResponseResult.error(SYS_EORRO);
        }

        return ResponseResult.success();
    }

    //获取所有启用的考勤规则
    @GetMapping(value = "selectInUseByuser")
    public ResponseResult selectInUse(Integer employeeId){
        return ResponseResult.success(attendanceRuleNewService.selectInUseByuser(employeeId));
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
    @ApiImplicitParams({@ApiImplicitParam(name="name",value="考勤规则名称",dataType="string", paramType = "query")})
    public ResponseResult findPageList(HttpServletRequest request){
        PageData pageData = new PageData(request);
        pageData.put("amputated",0);
        return super.selectPageList(pageData);
    }
}
