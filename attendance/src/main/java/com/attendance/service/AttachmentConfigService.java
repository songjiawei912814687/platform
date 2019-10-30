package com.attendance.service;

import com.attendance.core.base.BaseMapper;
import com.attendance.core.base.BaseService;
import com.attendance.core.base.MybatisBaseMapper;
import com.attendance.domian.output.AttachmentConfigOutput;
import com.attendance.mapper.jpa.AttachmentConfigRepository;
import com.attendance.model.AttachmentConfig;
import com.common.response.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author: Young
 * @description:
 * @date: Created in 17:07 2018/9/5
 * @modified by:
 */
@Service
public class AttachmentConfigService  extends BaseService<AttachmentConfigOutput, AttachmentConfig,Integer> {

    @Autowired
    private AttachmentConfigRepository repository;

    @Override
    public BaseMapper<AttachmentConfig, Integer> getMapper() {
        return repository;
    }

    @Override
    public MybatisBaseMapper<AttachmentConfigOutput> getMybatisBaseMapper() {
        return null;
    }


    public ResponseResult findByConfigType(Integer type){

        List<AttachmentConfig> attachmentConfigList = repository.findAllByConfigType(type);
        if (CollectionUtils.isEmpty(attachmentConfigList)) {
            return ResponseResult.error("未查出集合");
        }
        return ResponseResult.success(attachmentConfigList);
    }
}
