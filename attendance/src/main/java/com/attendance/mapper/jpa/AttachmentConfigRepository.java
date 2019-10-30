package com.attendance.mapper.jpa;


import com.attendance.core.base.BaseMapper;
import com.attendance.model.AttachmentConfig;

import java.util.List;

/**
 * @author: Young
 * @description:
 * @date: Created in 17:08 2018/9/5
 * @modified by:
 */
public interface AttachmentConfigRepository extends BaseMapper<AttachmentConfig,Integer> {

    /**
     * 根据附件类型查询
     * @param
     * @return
     */
    List<AttachmentConfig> findAllByConfigType(Integer configType);
}
