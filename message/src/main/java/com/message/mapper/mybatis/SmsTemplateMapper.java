package com.message.mapper.mybatis;

import com.message.core.base.MybatisBaseMapper;
import com.message.domain.output.SmsTemplateOutput;
import com.message.model.SmsTemplate;
import org.springframework.stereotype.Repository;

@Repository
public interface SmsTemplateMapper extends MybatisBaseMapper<SmsTemplateOutput>{
    int deleteByPrimaryKey(Integer id);

    int insert(SmsTemplate record);

    int insertSelective(SmsTemplate record);

    @Override
    SmsTemplateOutput selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SmsTemplate record);

    int updateByPrimaryKeyWithBLOBs(SmsTemplate record);

    int updateByPrimaryKey(SmsTemplate record);

    SmsTemplateOutput selectByName(String  name);

    SmsTemplateOutput selectByType(String  type);
}