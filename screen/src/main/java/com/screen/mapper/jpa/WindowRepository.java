package com.screen.mapper.jpa;

import com.screen.core.base.BaseMapper;
import com.screen.model.Window;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WindowRepository extends BaseMapper<Window,Integer> {
    List<Window> findAllByOrganizationIdIn(List<Integer> list);
}
