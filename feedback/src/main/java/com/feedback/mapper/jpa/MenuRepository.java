package com.feedback.mapper.jpa;

import com.feedback.core.base.BaseMapper;
import com.feedback.model.Menu;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends BaseMapper<Menu, Integer> {

}
