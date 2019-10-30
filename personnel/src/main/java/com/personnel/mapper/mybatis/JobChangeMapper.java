package com.personnel.mapper.mybatis;

import com.common.model.PageData;
import com.personnel.core.base.MybatisBaseMapper;
import com.personnel.domian.output.JobChangeOutput;
import com.personnel.model.JobChange;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobChangeMapper extends MybatisBaseMapper<JobChangeOutput> {
    int deleteByPrimaryKey(Integer id);

    int insert(JobChange record);

    int insertSelective(JobChange record);

    @Override
    JobChangeOutput selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(JobChange record);

    int updateByPrimaryKey(JobChange record);

    List<JobChangeOutput>  selectByEmployeeId(Integer employeeId);

}
