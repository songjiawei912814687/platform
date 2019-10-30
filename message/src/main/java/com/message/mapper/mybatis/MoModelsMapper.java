package com.message.mapper.mybatis;

import com.message.model.MoModels;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface MoModelsMapper  {
    int deleteByPrimaryKey(Integer id);

    int insert(MoModels record);

    int insertSelective(MoModels record);

    MoModels selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MoModels record);

    int updateByPrimaryKey(MoModels record);

    List<MoModels> selectPageList();
}
