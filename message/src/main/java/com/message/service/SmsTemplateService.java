package com.message.service;

import com.message.core.base.BaseMapper;
import com.message.core.base.BaseService;
import com.message.core.base.MybatisBaseMapper;
import com.message.domain.output.SmsTemplateOutput;
import com.message.mapper.jpa.SmsTemplateRepository;
import com.message.mapper.mybatis.SmsTemplateMapper;
import com.message.model.SmsTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.transaction.Transactional;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;

@Service
public class SmsTemplateService  extends BaseService<SmsTemplateOutput,SmsTemplate,Integer>{
    @Autowired
    private SmsTemplateRepository repository;

    @Autowired
    private SmsTemplateMapper smsTemplateMapper;
    @Override
    public BaseMapper<SmsTemplate, Integer> getMapper() {
        return repository;
    }

    @Override
    public MybatisBaseMapper<SmsTemplateOutput> getMybatisBaseMapper() {
        return smsTemplateMapper;
    }

    public int updatestate(String idList,Integer state) throws IllegalAccessException, IntrospectionException, InvocationTargetException,MethodArgumentNotValidException {
        var id = Integer.parseInt(idList);
        SmsTemplate t =  getMapper().getById(id);
        if(t==null){
            return -1;
        }
       if(state==1){
            if(smsTemplateMapper.selectByType(t.getType())!=null){
                if(!smsTemplateMapper.selectByType(t.getType()).getId().equals(t.getId())){
                    return 0;
                }
            }
       }
        setProperty(t, "state", state);
        var result = this.update(id,t);
        return result;
    }

    public SmsTemplateOutput getByName(String name){
        return smsTemplateMapper.selectByName(name);
    }

    public SmsTemplateOutput getByType(String type){
        return smsTemplateMapper.selectByType(type);
    }
}
