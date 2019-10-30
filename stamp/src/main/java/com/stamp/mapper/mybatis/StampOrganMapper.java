package com.stamp.mapper.mybatis;

import com.stamp.core.base.MybatisBaseMapper;
import com.stamp.domain.output.StampOrganOutput;
import com.stamp.model.StampOrgan;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StampOrganMapper extends MybatisBaseMapper<StampOrganOutput> {
    int deleteByPrimaryKey(Integer id);

    int insert(StampOrgan record);

    int insertSelective(StampOrgan record);

    @Override
    StampOrganOutput selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(StampOrgan record);

    int updateByPrimaryKey(StampOrgan record);

    String selectMaxNo();


    List<StampOrgan> selectAllOrg();

    List<StampOrganOutput> selectByParentId(Integer organizationId);
}
