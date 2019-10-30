package com.attendance.mapper.mybatis;

import com.attendance.model.Verification;
import com.common.model.PageData;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Verification record);

    int insertSelective(Verification record);

    Verification selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Verification record);

    int updateByPrimaryKey(Verification record);


    Integer deleteByDate(PageData pageData);
}