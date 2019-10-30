package com.attendance.mapper.jpa;

import com.attendance.core.base.BaseMapper;
import com.attendance.model.Attachment;

import java.util.List;

/**
 * @author: Young
 * @description:
 * @date: Created in 16:10 2018/9/5
 * @modified by:
 */
public interface AttachmentRepository extends BaseMapper<Attachment,Integer> {

    /**
     * 根据资源类型查询对象集合
     * @param resourcesType
     * @return
     */
    List<Attachment> findAttachmentsByResourcesType(Integer resourcesType);

    /**
     * 根据原文件名查询出对象
     * @param sourceFileName
     * @return
     */
    List<Attachment> findAttachmentsBySourceFileName(String sourceFileName);
}
