package com.screenData.mapper.jpa;

import com.screenData.core.base.BaseMapper;
import com.screenData.model.Window;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WindowRepository extends BaseMapper<Window,Integer> {
    List<Window> findAllByOrganizationIdIn(List<Integer> list);
}
