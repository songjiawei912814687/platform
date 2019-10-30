package com.api.mapper.mybatis;

import com.api.model.SmallProjectTrans;
import com.common.model.PageData;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SmallProjectTransMapper {
    int deleteByPrimaryKey(String id);

    int insert(SmallProjectTrans record);

    int insertSelective(SmallProjectTrans record);

    SmallProjectTrans selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(SmallProjectTrans record);

    int updateByPrimaryKey(SmallProjectTrans record);

    List<SmallProjectTrans> selectAll(PageData pageData);
}