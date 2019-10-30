package com.knowledge.mapper.mybatis;

import com.knowledge.core.base.MybatisBaseMapper;
import com.knowledge.domain.output.MenuOutput;
import com.knowledge.model.Menu;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuMapper extends MybatisBaseMapper<MenuOutput> {
    int deleteByPrimaryKey(Long id);

    int insert(Menu record);

    int insertSelective(Menu record);

    MenuOutput selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Menu record);

    int updateByPrimaryKey(Menu record);

    List<MenuOutput> findByRoles(List<Integer> ids);
}
