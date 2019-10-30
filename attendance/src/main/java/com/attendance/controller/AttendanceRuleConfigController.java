package com.attendance.controller;

import com.attendance.core.base.BaseController;
import com.attendance.core.base.BaseService;
import com.attendance.domian.output.AttendanceRuleConfigOutput;
import com.attendance.mapper.jpa.AttendanceRuleConfigRepository;
import com.attendance.mapper.jpa.AttendanceRuleNewRepository;
import com.attendance.mapper.mybatis.AttendanceRuleConfigMapper;
import com.attendance.model.AttendanceRuleConfig;
import com.attendance.model.AttendanceRuleNew;
import com.attendance.model.Organization;
import com.attendance.service.AttendanceRuleConfigService;
import com.common.model.PageData;
import com.common.response.ResponseResult;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * @author: young
 * @project_name: svn
 * @description:
 * @date: Created in 2019-03-13  09:59
 * @modified by:
 */
@RestController
@RequestMapping("attendanceRuleConfig")
public class AttendanceRuleConfigController extends BaseController<AttendanceRuleConfigOutput, AttendanceRuleConfig,Integer> {

    @Autowired
    private AttendanceRuleConfigService attendanceRuleConfigService;
    @Autowired
    private AttendanceRuleConfigRepository attendanceRuleConfigRepository;
    @Autowired
    private AttendanceRuleConfigMapper attendanceRuleConfigMapper;
    @Autowired
    private AttendanceRuleNewRepository attendanceRuleNewRepository;

    @Override
    public BaseService<AttendanceRuleConfigOutput, AttendanceRuleConfig, Integer> getService() {
        return attendanceRuleConfigService;
    }

    @Override
    @PostMapping(value = "form")
    @ApiOperation("新增/跟新考勤规则配置")
    public ResponseResult formPost(Integer id,@Validated @RequestBody @ApiParam(name="考勤规则配置相关信息",value="传入json格式",required=true) AttendanceRuleConfig attendanceRuleConfig) throws Exception {
        //如果考勤规则配置的名称为空
        if(attendanceRuleConfig.getName()==null||"".equals(attendanceRuleConfig.getName())){
            return ResponseResult.error(PARAM_EORRO);
        }
        if(id==null){
            //如果新增的时候名字已经存在就报错
            List<AttendanceRuleConfig> attendanceRuleConfigList =  attendanceRuleConfigRepository.findAllByNameAndAmputated(attendanceRuleConfig.getName(),0);
            if(attendanceRuleConfigList.size()>0){
                return ResponseResult.error("已有考勤配置名称请重新填写");
            }
        }else {
            //如果是编辑
            AttendanceRuleConfigOutput attendanceRuleConfigOutput = attendanceRuleConfigMapper.selectByPrimaryKey(id);
            if(attendanceRuleConfigOutput==null){
                return ResponseResult.error("未找到该记录");
            }
            if(attendanceRuleConfigOutput.getName().equals(attendanceRuleConfig.getName())){
                return super.formPost(id, attendanceRuleConfig);
            }else {
                //如果新增的时候名字已经存在就报错
                List<AttendanceRuleConfig> attendanceRuleConfigList =  attendanceRuleConfigRepository.findAllByNameAndAmputated(attendanceRuleConfig.getName(),0);
                if(attendanceRuleConfigList.size()>0){
                    return ResponseResult.error("已有考勤配置名称请重新填写");
                }
            }
        }

        return super.formPost(id, attendanceRuleConfig);
    }

    @Override
    @ApiOperation("删除考勤规则配置信息")
    @GetMapping(value = "logicDelete")
    public ResponseResult logicDelete(String idList) throws IllegalAccessException, IntrospectionException, InvocationTargetException, MethodArgumentNotValidException {
        List<AttendanceRuleNew> attendanceRuleNewList =
            attendanceRuleNewRepository.findAllByAttendanceRuleConfigIdAndAmputatedAndState(Integer.parseInt(idList),0,1);
        List<Organization> list=attendanceRuleConfigService.getByRuleId(Integer.parseInt(idList));
        if(list!=null&&list.size()>0){
          StringBuilder stringBuilder=new StringBuilder();
          int count=1;
          for(Organization organization:list){
              if(count>5){
                  break;
              }
              stringBuilder.append(organization.getName()+",");
              count++;
          }
          String name=stringBuilder.toString().substring(0,stringBuilder.toString().length()-1);
            return ResponseResult.error("此规则已被"+name+"等组织机构使用，不能删除");
        }
        for(AttendanceRuleNew attendanceRuleNew:attendanceRuleNewList){
            attendanceRuleNew.setAmputated(1);
            Integer id = attendanceRuleNewRepository.save(attendanceRuleNew).getId();
            if(id<=0){
                return ResponseResult.error("删除失败");
            }
        }
        return super.logicDelete(idList);
    }

    @Override
    @ApiOperation("根据id获取考勤规则配置信息")
    @GetMapping(value = "selectById")
    public ResponseResult selectById(Integer id) {
        return super.selectById(id);
    }

    @GetMapping(value = "findPageList")
    @ApiOperation("获取分页的考勤规则配置信息")
    public ResponseResult findPageList(HttpServletRequest request){
        PageData pageData = new PageData(request);
        return super.selectPageList(pageData);
    }

    @ApiOperation("获取所有不分页考勤规则配置信息")
    @GetMapping(value = "selectAll")
    public ResponseResult selectAll(HttpServletRequest request){
        return  super.selectAll(new PageData(request));
    }
}
