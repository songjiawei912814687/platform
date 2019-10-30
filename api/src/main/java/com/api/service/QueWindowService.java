package com.api.service;

import com.api.config.RedisComponent;
import com.api.config.RedisQueueComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;

/**
 * @author: young
 * @project_name: svn
 * @description: 排队叫号系统的登陆，注销，暂停服务，取消暂停
 * @date: Created in 2018-12-13  14:25
 * @modified by:
 */
@Service
public class QueWindowService {

    private static final Logger log = LoggerFactory.getLogger(QueWindowService.class);

    @Autowired
    private RedisComponent redisComponent;
    @Autowired
    private RedisQueueComponent redisQueueComponent;


    public String login(Map<String,Object> map,Map<String,Object> mapEx) {
        //获取部门编号
        String deptNo = (String) mapEx.get("dept_code");
        if(deptNo==null||deptNo==""){
            return null;
        }
        //获取工号
        String empNo = (String) mapEx.get("code");
        if(empNo == null||empNo==""){
            return null;
        }
        //获取窗口号
        String windowNo = (String) map.get("code");
        if(windowNo==null||windowNo==""){
            return null;
        }

        String value = empNo+","+windowNo+",1;";
        //如果不存在将value设置进去
       if(redisComponent.hasKey(deptNo)==false){
           redisComponent.set(deptNo,value,Long.valueOf(60*60*8));
           return value;
       }else {
           //如果部门已经存在先获取已有的value
           String longValue =redisComponent.get(deptNo);

           //判断是否已经登陆
           if(longValue.contains(empNo)){
               redisComponent.set(deptNo,longValue,Long.valueOf(60*60*8));
               return longValue;
           }else {
               //在加上新增的value组成新的value
               value = longValue+value;
               redisComponent.set(deptNo,value,Long.valueOf(60*60*8));
               return value;
           }
       }
    }

    public String logOut(Map<String,Object> map,Map<String,Object> mapEx){
        //获取部门编号
        String deptNo = (String) map.get("dept_code");
        //获取工号
        String empNo = (String) mapEx.get("code");
        //获取窗口号
        String windowNo = (String) map.get("code");
        //

        String longValue = redisComponent.get(deptNo);
        if(longValue==null){
            return "";
        }
        if(longValue.contains(empNo+","+windowNo+",1;")){
            longValue = longValue.replace(empNo+","+windowNo+",1;","");
            redisComponent.set(deptNo,longValue,Long.valueOf(60*60*8));
        }
        if(longValue.contains(empNo+","+windowNo+",2;")){
            longValue = longValue.replace(empNo+","+windowNo+",2;","");
            redisComponent.set(deptNo,longValue,Long.valueOf(60*60*8));
        }
        return longValue;
    }

    public String pauseService(Map<String,Object> map){
        //获取部门编号
        String deptNo = (String) map.get("dept_code");
        //获取工号
        String empNo = (String) map.get("evaluator_emp_code");
        //获取窗口号
        String windowNo = (String) map.get("code");


        String longValue = redisComponent.get(deptNo);
        if(longValue==null){
            return "";
        }
        if(longValue.contains(empNo)){
            longValue = longValue.replace(empNo+","+windowNo+",1;",empNo+","+windowNo+",2;");
            redisComponent.set(deptNo,longValue,Long.valueOf(60*60*8));
        }
        return longValue;
    }

    public String cancelPause(Map<String,Object> map){
        //获取部门编号
        String deptNo = (String) map.get("dept_code");
        //获取工号
        String empNo = (String) map.get("evaluator_emp_code");
        //获取窗口号
        String windowNo = (String) map.get("code");

        String longValue =  redisComponent.get(deptNo);
        if(longValue==null){
            return "";
        }
        if(longValue.contains(empNo)){
            longValue = longValue.replace(empNo+","+windowNo+",2;",empNo+","+windowNo+",1;");
            redisComponent.set(deptNo,longValue,Long.valueOf(60*60*8));
        }
        return longValue;
    }
}
