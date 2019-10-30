package com.attendance.service;

import com.attendance.core.base.BaseMapper;
import com.attendance.core.base.BaseService;
import com.attendance.core.base.MybatisBaseMapper;
import com.attendance.domian.output.AttachmentOutput;
import com.attendance.mapper.jpa.AttachmentRepository;
import com.attendance.model.Attachment;
import com.common.response.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author: Young
 * @description:
 * @date: Created in 16:11 2018/9/5
 * @modified by:
 */
@Service
public class AttachmentService extends BaseService<AttachmentOutput, Attachment,Integer> {

    @Autowired
    private AttachmentRepository attachmentRepository;

    @Override
    public BaseMapper<Attachment, Integer> getMapper() {
        return attachmentRepository;
    }

    @Override
    public MybatisBaseMapper<AttachmentOutput> getMybatisBaseMapper() {
        return null;
    }

    public ResponseResult findByResourcesType(Integer resourcesType){

        List<Attachment> attachmentList = attachmentRepository.findAttachmentsByResourcesType(resourcesType);
        if (CollectionUtils.isEmpty(attachmentList)) {
            return ResponseResult.error("集合为空");
        }
        return ResponseResult.success(attachmentList);
    }

    public ResponseResult findBySourceFileName(String sourceName){

        List<Attachment> attachmentList = attachmentRepository.findAttachmentsBySourceFileName(sourceName);
        if(attachmentList == null){
            return ResponseResult.error("查询的单个附件管理对象为空");
        }

        return ResponseResult.success(attachmentList);
    }
}
