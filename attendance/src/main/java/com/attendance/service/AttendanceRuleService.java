package com.attendance.service;

import com.attendance.core.base.BaseMapper;
import com.attendance.core.base.BaseService;
import com.attendance.core.base.MybatisBaseMapper;
import com.attendance.domian.output.AttendanceRuleOutput;
import com.attendance.mapper.jpa.AttendanceRuleRepository;
import com.attendance.mapper.mybatis.AttendanceRuleMapper;
import com.attendance.model.AttendanceRule;
import com.common.model.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.transaction.Transactional;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Service
public class AttendanceRuleService extends BaseService<AttendanceRuleOutput, AttendanceRule,Integer> {

    @Autowired
    private AttendanceRuleRepository repository;

    @Autowired
    private AttendanceRuleMapper attendanceRuleMapper;
    @Override
    public BaseMapper<AttendanceRule, Integer> getMapper() {
        return repository;
    }

    @Override
    public MybatisBaseMapper<AttendanceRuleOutput> getMybatisBaseMapper() {
        return attendanceRuleMapper;
    }

    public List<AttendanceRule> getByName(String name){
        return repository.findByName(name);
    }

    public boolean isRepeat(Integer id,String name){
        AttendanceRule attendanceRule=repository.getById(id);
        if(attendanceRule.getName().equals(name)){
            return true;
        }
        return false;
    }

    @Transactional
    public int updatestate(String idList,Integer state) throws IllegalAccessException, IntrospectionException, InvocationTargetException,MethodArgumentNotValidException,MethodArgumentNotValidException {
        var strs = idList.split(",");
        for(var str : strs){
            var id = Integer.parseInt(str);
           AttendanceRule t =  getMapper().getById(id);
            if(t==null){
                continue;
            }
            setProperty(t, "state", state);
            var result = this.update(id,t);
        }
        return SUCCESS;
    }
}
