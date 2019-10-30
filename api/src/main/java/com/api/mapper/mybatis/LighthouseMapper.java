package com.api.mapper.mybatis;

import com.api.model.Lighthouse;
import com.common.model.PageData;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LighthouseMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Lighthouse record);

    int insertSelective(Lighthouse record);

    Lighthouse selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Lighthouse record);

    int updateByPrimaryKey(Lighthouse record);

    List<Lighthouse> selectAll(PageData pageData);
}