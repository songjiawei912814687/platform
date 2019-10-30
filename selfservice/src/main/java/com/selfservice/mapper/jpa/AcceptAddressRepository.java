package com.selfservice.mapper.jpa;


import com.selfservice.core.base.BaseMapper;
import com.selfservice.model.AcceptAddress;

import java.util.List;

public interface AcceptAddressRepository extends BaseMapper<AcceptAddress,Integer> {

    void deleteByQlsxId(String ql_full_id);

    List<AcceptAddress> findByQlInnerCode(String innerCode);
}
