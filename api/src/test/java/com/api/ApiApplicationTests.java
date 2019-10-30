package com.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.api.model.StationPeople;
import com.api.service.EvaluationResultService;
import com.api.service.StationPeopleService;
import com.api.util.PostUtil;
import com.google.gson.JsonObject;
import org.apache.commons.lang.StringEscapeUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApiApplicationTests {
    @Autowired
    private StationPeopleService stationPeopleService;

    @Test
    public void contextLoads() {

    }

    @Test
    public void poTest() throws Exception {
        stationPeopleService.asynGroup();
    }
}