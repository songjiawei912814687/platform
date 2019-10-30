package com.knowledge.mapper.mybatis;

import com.common.model.PageData;
import com.knowledge.core.base.MybatisBaseMapper;
import com.knowledge.domain.output.QuestionOutput;
import com.knowledge.model.Question;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionMapper extends MybatisBaseMapper<QuestionOutput>{
    int deleteByPrimaryKey(Integer id);

    int insert(Question record);

    int insertSelective(Question record);

    @Override
    QuestionOutput selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Question record);

    int updateByPrimaryKey(Question record);

    List<QuestionOutput> findAllByAnswerState(PageData pageData);

    List<Integer> selectOrgByParentId(Integer organizationId);

    List<QuestionOutput> selectQustionOrganizations(PageData pageData);
}
