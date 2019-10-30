package com.personnel.mapper.jpa;

import com.personnel.model.QueueLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author: young
 * @project_name: platfrom
 * @description: 取号叫号下发日志
 * @date: Created in 2019-02-13  09:47
 * @modified by:
 */
public interface QueueLogRepository extends JpaRepository<QueueLog,Integer> {

    List<QueueLog> findAllByResourceId(Integer resourceId);
}
