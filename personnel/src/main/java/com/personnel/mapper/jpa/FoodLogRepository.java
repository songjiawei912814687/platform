package com.personnel.mapper.jpa;


import com.personnel.model.FoodLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author: young
 * @project_name: svn
 * @description:
 * @date: Created in 2019-01-25  15:00
 * @modified by:
 */
public interface FoodLogRepository extends JpaRepository<FoodLog,Integer> {

    List<FoodLog> findAllByResourceId(Integer resourceId);
}
