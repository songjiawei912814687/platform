package com.message.mapper.mybatis;

import com.message.core.base.MybatisBaseMapper;
import com.message.domain.output.SMSReceiveOutPut;
import com.message.model.SMSReceive;
import org.springframework.stereotype.Repository;

@Repository
public interface SMSReceiveMapper extends MybatisBaseMapper<SMSReceiveOutPut> {
    int deleteByPrimaryKey(Integer id);

    int insert(SMSReceive record);

    int insertSelective(SMSReceive record);

    SMSReceiveOutPut selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SMSReceive record);

    int updateByPrimaryKey(SMSReceive record);
}
