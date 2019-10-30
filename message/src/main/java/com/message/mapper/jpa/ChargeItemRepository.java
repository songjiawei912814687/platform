package com.message.mapper.jpa;

import com.message.core.base.BaseMapper;
import com.message.model.ChargeItem;
import com.message.model.Materials;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ChargeItemRepository extends BaseMapper<ChargeItem,Integer> {

    @Transactional(rollbackFor = Exception.class)
    void deleteByQlInnerCode(String qlInnerCode);
}
