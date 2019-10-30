package com.knowledge.mapper.mybatis;

import com.knowledge.core.base.MybatisBaseMapper;
import com.knowledge.model.Answer;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerMapper extends MybatisBaseMapper<Answer>{
    int deleteByPrimaryKey(Integer id);

    int insert(Answer record);

    int insertSelective(Answer record);

    @Override
    Answer selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Answer record);

    int updateByPrimaryKey(Answer record);
}