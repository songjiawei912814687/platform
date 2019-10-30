package com.knowledge.mapper.jpa;

import com.knowledge.core.base.BaseMapper;
import com.knowledge.model.Action;
import com.knowledge.model.Question;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends BaseMapper<Question,Integer>{

    List<Question> findAllByTitleAndAmputated(String title,Integer amputated);
}
