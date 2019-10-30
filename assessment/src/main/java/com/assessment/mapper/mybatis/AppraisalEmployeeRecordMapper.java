package com.assessment.mapper.mybatis;

import com.assessment.core.base.MybatisBaseMapper;
import com.assessment.domian.output.AppraisalEmployeeRecordOutput;
import com.assessment.model.AppraisalEmployeeRecord;
import com.common.model.PageData;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AppraisalEmployeeRecordMapper extends MybatisBaseMapper<AppraisalEmployeeRecordOutput> {
    int deleteByPrimaryKey(Integer id);

    int insert(AppraisalEmployeeRecord record);

    int insertSelective(AppraisalEmployeeRecord record);

    @Override
    AppraisalEmployeeRecordOutput selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AppraisalEmployeeRecord record);

    int updateByPrimaryKey(AppraisalEmployeeRecord record);

    List<AppraisalEmployeeRecordOutput> selectByPlan(PageData pageData);

    List<AppraisalEmployeeRecordOutput> selectByDate(PageData pageData);

    List<AppraisalEmployeeRecordOutput> getByState(int state);
}
