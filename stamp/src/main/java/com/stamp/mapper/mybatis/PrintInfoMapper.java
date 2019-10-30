package com.stamp.mapper.mybatis;

import com.common.model.PageData;
import com.stamp.core.base.MybatisBaseMapper;
import com.stamp.domain.output.PrintInfoOutput;
import com.stamp.model.PrintInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrintInfoMapper extends MybatisBaseMapper<PrintInfoOutput> {
    int deleteByPrimaryKey(Long id);

    int insert(PrintInfo record);

    int insertSelective(PrintInfo record);

    @Override
    PrintInfoOutput selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PrintInfo record);

    int updateByPrimaryKey(PrintInfo record);

    @Override
    List<PrintInfoOutput> selectAll(PageData t);
}