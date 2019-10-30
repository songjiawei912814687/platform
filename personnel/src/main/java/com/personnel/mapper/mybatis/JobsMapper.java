package com.personnel.mapper.mybatis;

import com.personnel.core.base.MybatisBaseMapper;
import com.personnel.domian.output.JobsOutput;
import com.personnel.model.Jobs;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobsMapper extends MybatisBaseMapper<JobsOutput> {
    int deleteByPrimaryKey(Integer id);

    int insert(Jobs record);

    int insertSelective(Jobs record);

    @Override
    JobsOutput selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Jobs record);

    int updateByPrimaryKey(Jobs record);

    Jobs selectAll(Integer isDelete);

    /**
     * 根据职务名称查询
     * @param name 职务名称
     * @return
     */
    List<Jobs> selectByName(String name);


    Jobs selectByResponsibilities(String responsibilities);
}
