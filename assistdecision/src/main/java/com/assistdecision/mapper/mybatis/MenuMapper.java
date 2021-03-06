package com.assistdecision.mapper.mybatis;

import com.assistdecision.core.base.MybatisBaseMapper;
import com.assistdecision.domain.output.MenuOutput;
import com.assistdecision.model.Menu;
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
