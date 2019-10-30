package com.message.mapper.jpa;

import com.message.model.QltQlsx;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author: Young
 * @description:
 * @date: Created in 21:10 2018/10/23
 * @modified by:
 */
@Repository
public interface QltQlsxRepository extends JpaRepository<QltQlsx,String> {

    @Modifying
    @Query(value = "delete from QltQlsx q where q.QL_Full_ID = ?1")
    void deleteByQL_Full_ID(String qlId);
}
