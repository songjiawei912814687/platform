package com.api.mapper.mybatis;

import com.api.model.BuildCall;
import com.common.model.PageData;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BuildCallMapper {
    int deleteByPrimaryKey(String id);

    int insert(BuildCall record);

    int insertSelective(BuildCall record);

    BuildCall selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(BuildCall record);

    int updateByPrimaryKey(BuildCall record);

    List<BuildCall> selectAll(PageData pageData);
}