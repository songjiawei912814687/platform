package com.message.mapper.jpa;

import com.message.core.base.BaseMapper;
import com.message.model.AcceptAddress;
import com.message.model.CommonQuestion;

public interface AcceptAddressRepository extends BaseMapper<AcceptAddress,Integer> {

    void deleteByQlInnerCode(String qlInnerCode);
}
