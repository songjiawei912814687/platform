package com.api.mapper.jpa;

import com.api.model.Counter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author: young
 * @project_name: svn
 * @description:
 * @date: Created in 2019-01-14  15:16
 * @modified by:
 */
public interface CounterRepository extends JpaRepository<Counter,Integer> {
    List<Counter> findAllByDeptCodeAndOnlineFlag(String deptCode,Integer onlineFlag);

    List<Counter> findAllByOnlineFlagAndDeptCodeInOrderByCode(Integer onlineFlag,List<String> deptCodeList);

}
