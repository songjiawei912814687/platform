package com.api.mapper.mybatis;

import com.api.model.Config;
import org.apache.ibatis.annotations.Param;

import java.net.InetAddress;
import java.util.List;

public interface ConfigMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Config record);

    int insertSelective(Config record);

    Config selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Config record);

    int updateByPrimaryKey(Config record);

    List<Config> selectByIdAndState(@Param("id") Integer id,@Param("state") Integer state);
}