package com.attendance.mapper.jpa;

import com.attendance.core.base.BaseMapper;
import com.attendance.model.Menu;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends BaseMapper<Menu, Integer> {

}
