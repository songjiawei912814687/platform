package com.personnel.mapper.jpa;

import com.personnel.core.base.BaseMapper;
import com.personnel.model.StationEmployees;

import java.util.List;

public interface StationEmployeesRepository extends BaseMapper<StationEmployees, Integer> {
    List<StationEmployees> findAllByState(Integer State);
}
