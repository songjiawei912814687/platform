package com.crawler.mapper.mybatis;

import com.common.model.PageData;
import com.crawler.model.HchenStatistical;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HchenStatisticalMapper {
    int deleteByPrimaryKey(Long id);

    int insert(HchenStatistical record);

    int insertSelective(HchenStatistical record);

    HchenStatistical selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(HchenStatistical record);

    int updateByPrimaryKey(HchenStatistical record);


    List<HchenStatistical> selectCountListByReportDate(PageData pageData);
}
