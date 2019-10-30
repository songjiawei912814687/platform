package com.assessment.service;


import com.assessment.core.base.BaseMapper;
import com.assessment.core.base.BaseService;
import com.assessment.core.base.MybatisBaseMapper;
import com.assessment.mapper.jpa.AttachmentRepository;
import com.assessment.mapper.mybatis.AttachmentMapper;
import com.assessment.model.Attachment;
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


    public boolean deleteByRecordId(Integer id){
        if(attachmentMapper.deleteByRecordId(id)<0){
            return false;
        }
        return true;
    }
}
