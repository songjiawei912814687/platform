package com.knowledge.mapper.mybatis;

import com.knowledge.core.base.MybatisBaseMapper;
import com.knowledge.model.Attachment;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttachmentMapper extends MybatisBaseMapper<Attachment>{
    int deleteByPrimaryKey(Integer id);

    int insert(Attachment record);

    int insertSelective(Attachment record);

    @Override
    Attachment selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Attachment record);

    int updateByPrimaryKey(Attachment record);

    List<Attachment> selectByApplyId(Integer applyId);

    int deleteByApplyId(Integer id);
}