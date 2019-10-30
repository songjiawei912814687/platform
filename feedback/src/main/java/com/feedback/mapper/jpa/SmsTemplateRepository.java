package com.feedback.mapper.jpa;


import com.feedback.core.base.BaseMapper;
import com.feedback.model.SmsTemplate;
import org.springframework.stereotype.Repository;

@Repository
public interface SmsTemplateRepository extends BaseMapper<SmsTemplate,Integer> {
}
