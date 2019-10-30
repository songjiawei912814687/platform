package com.attendance.service;

import com.attendance.core.base.BaseMapper;
import com.attendance.core.base.BaseService;
import com.attendance.core.base.MybatisBaseMapper;
import com.attendance.domian.output.AttendanceRuleNewOutput;
import com.attendance.mapper.jpa.AttendanceRuleNewRepository;
import com.attendance.mapper.jpa.EmployeesRepository;
import com.attendance.mapper.jpa.UserRepository;
import com.attendance.mapper.mybatis.AttendanceRuleNewMapper;
import com.attendance.model.AttendanceRule;
import com.attendance.model.AttendanceRuleNew;
import com.attendance.model.Users;
import com.common.model.PageData;
import com.common.response.ResponseResult;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.transaction.Transactional;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;

/**
 * @author: young
 * @project_name: assist-decision
 * @description:
 * @date: Created in 2019-03-13  11:32
 * @modified by:
 */
@Service
public class AttendanceRuleNewService extends BaseService<AttendanceRuleNewOutput,AttendanceRuleNew,Integer> {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AttendanceRuleNewRepository attendanceRuleNewRepository;
    @Autowired
    private AttendanceRuleNewMapper attendanceRuleNewMapper;

    @Override
    public BaseMapper<AttendanceRuleNew, Integer> getMapper() {
        return attendanceRuleNewRepository;
    }

    @Override
    public MybatisBaseMapper<AttendanceRuleNewOutput> getMybatisBaseMapper() {
        return attendanceRuleNewMapper;
    }

    @Transactional
    public int updatestate(String idList,Integer state) throws IllegalAccessException, IntrospectionException, InvocationTargetException, MethodArgumentNotValidException,MethodArgumentNotValidException {
        var strs = idList.split(",");
        for(var str : strs){
            var id = Integer.parseInt(str);
            AttendanceRuleNew t =  getMapper().getById(id);
            if(t==null){
                continue;
            }
            setProperty(t, "state", state);
            var result = this.update(id,t);
        }
        return SUCCESS;
    }

    public boolean typeIsExist(AttendanceRuleNew attendanceRuleNew) {
        PageData pageData = new PageData();
        pageData.put("attendanceRuleConfigId",attendanceRuleNew.getAttendanceRuleConfigId());
        pageData.put("type",attendanceRuleNew.getType());
        List<AttendanceRuleNewOutput> list = attendanceRuleNewMapper.findByTypeAndAttendanceRuleConfigId(pageData);
        if(list!=null&&list.size()>0){
            return true;
        }
        return false;
    }

    public List<AttendanceRuleNewOutput> selectInUseByuser(Integer employeeId) {
        PageData pageData = new PageData();
        if(employeeId==null||"".equals(employeeId)){
            pageData.put("userName",getUsers().getUsername());
        }else{
            Users users = userRepository.findByEmployeeId(employeeId);
            pageData.put("userName",users.getUsername());
        }
        List<AttendanceRuleNewOutput> list = attendanceRuleNewMapper.selectInUseByuser(pageData);
        return list;
    }
}
