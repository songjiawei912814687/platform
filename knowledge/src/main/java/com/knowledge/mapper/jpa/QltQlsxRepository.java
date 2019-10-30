package com.knowledge.mapper.jpa;

import com.knowledge.core.base.BaseMapper;
import com.knowledge.model.QltQlsx;
import org.springframework.data.jpa.repository.Query;

/**
 * @author: Young
 * @description:
 * @date: Created in 13:40 2018/11/13
 * @modified by:
 */
public interface QltQlsxRepository extends BaseMapper<QltQlsx,String> {
    @Query(value = "select * from QLT_QLSX b where b.QL_FULL_ID=?1", nativeQuery = true)
    QltQlsx getById(Integer id);

    @Query(value = "delete * from QLT_QLSX b where b.QL_FULL_ID=?1", nativeQuery = true)
    void deleteById(Integer id);

}
