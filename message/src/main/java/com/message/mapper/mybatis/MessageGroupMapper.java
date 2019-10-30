package com.message.mapper.mybatis;

import com.message.core.base.MybatisBaseMapper;
import com.message.domain.output.MessageGroupOutput;
import com.message.model.MessageGroup;
import com.message.model.MessageGroupEmployee;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageGroupMapper extends MybatisBaseMapper<MessageGroupOutput> {
    int deleteByPrimaryKey(Integer id);

    int insert(MessageGroup record);

    int insertSelective(MessageGroup record);

    @Override
    MessageGroupOutput selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MessageGroup record);

    int updateByPrimaryKey(MessageGroup record);

    MessageGroupOutput selectByName(String name);

    List<MessageGroupEmployee> selectByGroupId(Integer groupId);

}