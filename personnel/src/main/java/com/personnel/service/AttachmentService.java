package com.personnel.service;
;
import com.personnel.core.base.BaseMapper;
import com.personnel.core.base.BaseService;
import com.personnel.core.base.MybatisBaseMapper;
import com.personnel.mapper.jpa.AttachmentRepository;
import com.personnel.mapper.mybatis.AttachmentMapper;
import com.personnel.model.Attachment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AttachmentService extends BaseService<Attachment, Attachment,Integer> {
    @Autowired
    private AttachmentRepository repository;

    @Autowired
    private AttachmentMapper attachmentMapper;

    @Override
    public BaseMapper<Attachment, Integer> getMapper() {
        return repository;
    }

    @Override
    public MybatisBaseMapper<Attachment> getMybatisBaseMapper() {
        return attachmentMapper;
    }


}
