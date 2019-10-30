package com.assessment.domian.input;

import com.assessment.model.PlanItemAddition;

import java.util.ArrayList;
import java.util.List;

public class PlanItemAdditionInput {
    List<PlanItemAddition> list = new ArrayList<>();

    public List<PlanItemAddition> getList() {
        return list;
    }

    public void setList(List<PlanItemAddition> list) {
        this.list = list;
    }
}
