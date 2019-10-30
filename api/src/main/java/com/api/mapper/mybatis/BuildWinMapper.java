package com.api.mapper.mybatis;

import com.api.model.BuildWin;
import com.common.model.PageData;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BuildWinMapper {
    int deleteByPrimaryKey(String id);

    int insert(BuildWin record);

    int insertSelective(BuildWin record);

    BuildWin selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(BuildWin record);

    int updateByPrimaryKey(BuildWin record);

    List<BuildWin> selectAll(PageData pageData);
}