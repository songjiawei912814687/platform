package com.meeting.mapper.mybatis;

import com.meeting.core.base.MybatisBaseMapper;
import com.meeting.model.Attachment;
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

    List<Attachment> selectByPitId(Integer pitId);
}