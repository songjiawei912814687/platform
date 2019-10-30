package com.attendance.mapper.mybatis;


import com.attendance.core.base.MybatisBaseMapper;
import com.attendance.domian.output.AttachmentOutput;
import com.attendance.model.Attachment;

public interface AttachmentMapper extends MybatisBaseMapper<AttachmentOutput> {
    int deleteByPrimaryKey(Integer id);

    int insert(Attachment record);

    int insertSelective(Attachment record);

    @Override
    AttachmentOutput selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Attachment record);

    int updateByPrimaryKey(Attachment record);
}
