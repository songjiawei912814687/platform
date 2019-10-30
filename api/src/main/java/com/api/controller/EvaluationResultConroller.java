package com.api.controller;

import com.api.service.EvaluationResultService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: young
 * @project_name: svn
 * @description:
 * @date: Created in 2019-01-22  16:00
 * @modified by:
 */
@RestController
@RequestMapping("evalResult")
public class EvaluationResultConroller {

    @Autowired
    private EvaluationResultService evaluationResultService;
    @GetMapping("addEvalResult")
    public void addEvalResult() throws ParseException, JsonProcessingException {
        Map<String,Object> map = new HashMap<>();
        map.put("id",1);
        map.put("employee_code","11379");
        map.put("counter_code","H4");
        map.put("evaluate_code","A");
        map.put("evaluate_value",2);
        map.put("appraiser_mobile",null);
        map.put("creation_time",new Date());
        map.put("dept_code","G0000056");
        map.put("queueing_list_id",17942);

        evaluationResultService.addEvaluationResult(map);
        return;
    }
}
