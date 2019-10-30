package com.personnel.mapper.jpa;

import com.personnel.model.QueueEmployees;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author: young
 * @project_name: svn
 * @description:
 * @date: Created in 2018-12-05  10:22
 * @modified by:
 */
public interface QueueEmployeesRepository extends JpaRepository<QueueEmployees,Integer> {

    List<QueueEmployees> findAllByQueueState(Integer queueState);

    QueueEmployees findByCode(String code);
}
