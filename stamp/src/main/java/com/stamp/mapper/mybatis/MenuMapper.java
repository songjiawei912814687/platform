package com.stamp.mapper.mybatis;

import com.stamp.core.base.MybatisBaseMapper;
import com.stamp.domain.output.MenuOutput;
import com.stamp.model.Menu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MenuMapper extends MybatisBaseMapper<MenuOutput> {
    int deleteByPrimaryKey(Long id);

    int insert(Menu record);

    int insertSelective(Menu record);

    Menu selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Menu record);

    int updateByPrimaryKey(Menu record);


    List<MenuOutput> findAll();

    List<MenuOutput> findByRoles(List<Integer> ids);

    List<MenuOutput> findByParentId(@Param(value = "parentId") Integer parentId);

    List<MenuOutput> selectAllToW();
}