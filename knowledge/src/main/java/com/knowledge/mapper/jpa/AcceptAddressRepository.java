package com.knowledge.mapper.jpa;


import com.knowledge.core.base.BaseMapper;
import com.knowledge.model.AcceptAddress;

public interface AcceptAddressRepository extends BaseMapper<AcceptAddress,Integer> {

    void deleteByQlsxId(String ql_full_id);
}
