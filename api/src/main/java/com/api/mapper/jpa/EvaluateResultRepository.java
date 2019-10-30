package com.api.mapper.jpa;

import com.api.model.EvaluateResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author: young
 * @project_name: svn
 * @description:
 * @date: Created in 2019-01-11  20:04
 * @modified by:
 */
public interface EvaluateResultRepository extends JpaRepository<EvaluateResult,Integer> {

    /**
     * 根据下发状态去评价结果源数据中拉取评价结果源数据
     * @param state
     * @return
     */
    List<EvaluateResult> findAllByState(Integer state);
}
