package com.api.mapper.jpa;

import com.api.core.base.BaseMapper;
import com.api.model.Window;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WindowRepository extends BaseMapper<Window,Integer>{
    List<Window> findAllByOrganizationIdIn(List<Integer> list);
}
