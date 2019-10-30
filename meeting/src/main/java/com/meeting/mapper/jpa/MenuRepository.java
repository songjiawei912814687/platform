package com.meeting.mapper.jpa;

import com.meeting.core.base.BaseMapper;
import com.meeting.model.Menu;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuRepository extends BaseMapper<Menu, Integer> {

}
