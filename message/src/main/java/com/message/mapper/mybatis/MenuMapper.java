package com.message.mapper.mybatis;

import com.message.core.base.MybatisBaseMapper;
import com.message.domain.output.MenuOutput;
import com.message.model.Menu;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuMapper extends MybatisBaseMapper<MenuOutput> {
    int deleteByPrimaryKey(Long id);

    int insert(Menu record);

    int insertSelective(Menu record);

    Menu selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Menu record);

    int updateByPrimaryKey(Menu record);

    List<MenuOutput> findByRoles(List<Integer> ids);
}
