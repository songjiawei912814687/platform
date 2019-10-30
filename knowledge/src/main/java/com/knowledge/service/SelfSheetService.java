package com.knowledge.service;

import com.knowledge.core.base.BaseMapper;
import com.knowledge.core.base.BaseService;
import com.knowledge.core.base.MybatisBaseMapper;
import com.knowledge.domain.output.SelfSheetOutput;
import com.knowledge.mapper.jpa.SelfSheetRepository;
import com.knowledge.mapper.mybatis.SelfSheetMapper;
import com.knowledge.model.SelfSheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: young
 * @description:
 * @date: Created in 2019-08-21  15:15
 */
@Service
public class SelfSheetService extends BaseService<SelfSheetOutput, SelfSheet,Integer> {

    @Autowired
    private SelfSheetMapper selfSheetMapper;
    @Autowired
    private SelfSheetRepository selfSheetRepository;

    @Override
    public BaseMapper<SelfSheet, Integer> getMapper() {
        return selfSheetRepository;
    }

    @Override
    public MybatisBaseMapper<SelfSheetOutput> getMybatisBaseMapper() {
        return selfSheetMapper;
    }
}
