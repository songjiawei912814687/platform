package com.assessment.mapper.mybatis;



import com.assessment.domian.output.HchenStatisticalOutput;
import com.assessment.model.HchenStatistical;
import com.common.model.PageData;
import org.apache.ibatis.annotations.Param;
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


    List<HchenStatisticalOutput> selectCountListByReportDate(@Param("year") Integer year,@Param("month") Integer moth);

    //根据鸿程网站组织名称查询出一条鸿程数据
    HchenStatisticalOutput selectByName(String hcName);
}
