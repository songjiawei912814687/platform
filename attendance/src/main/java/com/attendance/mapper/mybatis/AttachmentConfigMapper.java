package com.attendance.mapper.mybatis;

import com.attendance.core.base.MybatisBaseMapper;
import com.attendance.domian.output.AttachmentConfigOutput;
import com.attendance.model.AttachmentConfig;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachmentConfigMapper extends MybatisBaseMapper<AttachmentConfigOutput> {
    int deleteByPrimaryKey(Integer id);

    int insert(AttachmentConfig record);

    int insertSelective(AttachmentConfig record);

    @Override
    AttachmentConfigOutput selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(AttachmentConfig record);

    int updateByPrimaryKey(AttachmentConfig record);
}
