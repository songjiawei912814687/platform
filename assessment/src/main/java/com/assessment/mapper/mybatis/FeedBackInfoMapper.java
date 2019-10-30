package com.assessment.mapper.mybatis;

import com.assessment.model.FeedBackInfo;
import com.common.model.PageData;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface FeedBackInfoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(FeedBackInfo record);

    int insertSelective(FeedBackInfo record);

    FeedBackInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(FeedBackInfo record);

    int updateByPrimaryKey(FeedBackInfo record);

    List<FeedBackInfo> findByDate(PageData pageData);
}
