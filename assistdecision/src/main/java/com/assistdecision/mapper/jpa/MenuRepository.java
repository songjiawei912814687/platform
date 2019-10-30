package com.assistdecision.mapper.jpa;


import com.assistdecision.core.base.BaseMapper;
import com.assistdecision.model.Menu;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends BaseMapper<Menu, Integer> {

}
