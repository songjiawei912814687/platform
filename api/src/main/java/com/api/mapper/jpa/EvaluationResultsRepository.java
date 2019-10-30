package com.api.mapper.jpa;

import com.api.model.EvaluationResults;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EvaluationResultsRepository extends JpaRepository<EvaluationResults,Integer> {

    EvaluationResults findByResourceId(Integer id);
}
