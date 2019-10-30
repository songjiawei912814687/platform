package com.personnel.mapper.jpa;

import com.personnel.model.QueueWindow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author: young
 * @project_name: svn
 * @description:
 * @date: Created in 2018-12-05  10:22
 * @modified by:
 */
public interface QueueWindowRepository extends JpaRepository<QueueWindow,Integer> {

    /**根据下发的状态来取出所有的未下发的排队叫号系统中的数据*/
    List<QueueWindow> findAllByState(Integer state);
}
