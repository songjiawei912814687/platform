package com.message.mapper.jpa;

import com.message.core.base.BaseMapper;
import com.message.model.SmsTemplate;
import org.springframework.stereotype.Repository;

@Repository
public interface SmsTemplateRepository extends BaseMapper<SmsTemplate,Integer>{
}
