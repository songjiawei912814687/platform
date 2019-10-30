package com.selfservice.mapper.jpa;

import com.selfservice.core.base.BaseMapper;
import com.selfservice.model.Menu;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends BaseMapper<Menu, Integer> {

}
