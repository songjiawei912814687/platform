package com.assessment.mapper.mybatis;

import com.assessment.core.base.MybatisBaseMapper;
import com.assessment.domian.output.AppraisalIndexOutput;
import com.assessment.domian.output.TemplateSetting;
import com.assessment.model.AppraisalIndex;
import com.common.model.PageData;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface AppraisalIndexMapper extends MybatisBaseMapper<AppraisalIndexOutput> {
    int deleteByPrimaryKey(Long id);

    int insert(AppraisalIndex record);

    int insertSelective(AppraisalIndex record);

    AppraisalIndex selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AppraisalIndex record);

    int updateByPrimaryKey(AppraisalIndex record);

    List<AppraisalIndexOutput> findByNameNotId(AppraisalIndex appraisalIndex);

    List<TemplateSetting> selectIndexList(PageData  pageData);

    Integer sumdatumValue(ArrayList<Integer> idList);
}
