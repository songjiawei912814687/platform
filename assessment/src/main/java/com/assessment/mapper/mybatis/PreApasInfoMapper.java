package com.assessment.mapper.mybatis;

import com.assessment.domian.output.PreApasInfoOutput;
import com.assessment.model.PreApasInfo;
import com.common.model.PageData;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PreApasInfoMapper {
    int deleteByPrimaryKey(String id);

    int insert(PreApasInfo record);

    int insertSelective(PreApasInfo record);

    PreApasInfo selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(PreApasInfo record);

    int updateByPrimaryKey(PreApasInfo record);

    List<String> getUnionVal();

    List<PreApasInfoOutput> findByDate(PageData pageData);
}
