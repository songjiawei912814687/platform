package com.feedback.mapper.mybatis;


import com.feedback.core.base.MybatisBaseMapper;
import com.feedback.domain.output.SmsTemplateOutput;
import com.feedback.model.SmsTemplate;
import org.springframework.stereotype.Repository;

@Repository
public interface SmsTemplateMapper extends MybatisBaseMapper<SmsTemplateOutput> {
    int deleteByPrimaryKey(Integer id);

    int insert(SmsTemplate record);

    int insertSelective(SmsTemplate record);

    @Override
    SmsTemplateOutput selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SmsTemplate record);

    int updateByPrimaryKeyWithBLOBs(SmsTemplate record);

    int updateByPrimaryKey(SmsTemplate record);

    SmsTemplateOutput selectByName(String name);

    SmsTemplateOutput selectByType(String type);
}