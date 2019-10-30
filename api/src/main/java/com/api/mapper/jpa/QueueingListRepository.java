package com.api.mapper.jpa;

import com.api.model.QueueingList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author: young
 * @project_name: svn
 * @description:
 * @date: Created in 2019-01-11  16:08
 * @modified by:
 */
public interface QueueingListRepository extends JpaRepository<QueueingList,Integer> {

    /***
     * 根据同步状态查找取号记录
     * @param state
     * @return
     */
    List<QueueingList> findAllByState(Integer state);
}
