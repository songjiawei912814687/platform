package com.knowledge.service;

import com.knowledge.core.base.BaseMapper;
import com.knowledge.core.base.BaseService;
import com.knowledge.core.base.MybatisBaseMapper;
import com.knowledge.mapper.jpa.AttachmentRepository;
import com.knowledge.mapper.mybatis.AttachmentMapper;
import com.knowledge.model.Attachment;
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

    public boolean deleteByApplyId(Integer id){
        if(attachmentMapper.deleteByApplyId(id)<0){
            return false;
        }
        return true;
    }
}
