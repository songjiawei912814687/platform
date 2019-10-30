package com.api.mapper.mybatis;

import com.api.model.SmallProjectResult;
import com.common.model.PageData;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SmallProjectResultMapper {
    int deleteByPrimaryKey(String id);

    int insert(SmallProjectResult record);

    int insertSelective(SmallProjectResult record);

    SmallProjectResult selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(SmallProjectResult record);

    int updateByPrimaryKey(SmallProjectResult record);

    List<SmallProjectResult> selectAll(PageData pageData);
}