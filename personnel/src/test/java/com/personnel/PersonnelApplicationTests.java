package com.personnel;

import com.personnel.model.Employees;
import com.personnel.model.StationEmployees;
import com.personnel.service.EmployeesService;
import com.personnel.service.StationEmployeesService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@EnableAutoConfiguration
@EnableJpaRepositories({"com.personnel.mapper.jpa","com.personnel.core.base"})
@EntityScan("com.personnel.**")
@SpringBootTest
public class PersonnelApplicationTests {
    @Autowired
    EmployeesService employeesService;

    @Autowired
    StationEmployeesService stationEmployeesService;

    @Test
    public void contextLoads() {
    }



    @Test
    public void stationEmpTest(){
        stationEmployeesService.saveAcc();
    }
}
