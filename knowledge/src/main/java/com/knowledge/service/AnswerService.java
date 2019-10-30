package com.knowledge.service;

import com.knowledge.core.base.BaseMapper;
import com.knowledge.core.base.BaseService;
import com.knowledge.core.base.MybatisBaseMapper;
import com.knowledge.mapper.jpa.AnswerRepository;
import com.knowledge.mapper.mybatis.AnswerMapper;
import com.knowledge.model.Answer;
import org.springframework.beans.factory.annotation.Autowired;

public class AnswerService extends BaseService<Answer,Answer,Integer>{

    @Autowired
    private AnswerRepository repository;

    @Autowired
    private AnswerMapper answerMapper;

    @Override
    public BaseMapper<Answer, Integer> getMapper() {
        return repository;
    }

    @Override
    public MybatisBaseMapper<Answer> getMybatisBaseMapper() {
        return answerMapper;
    }
}
