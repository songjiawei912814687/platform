package com.api.domain.output;

import com.api.model.EvaluationResults;

public class EvaluationResultsOutput extends EvaluationResults {
    private Integer satisfaction;

    private String name;

    private Integer evaId;

    public Integer getSatisfaction() {
        return satisfaction;
    }

    public void setSatisfaction(Integer satisfaction) {
        this.satisfaction = satisfaction;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getEvaId() {
        return evaId;
    }

    public void setEvaId(Integer evaId) {
        this.evaId = evaId;
    }
}
