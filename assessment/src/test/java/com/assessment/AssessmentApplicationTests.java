package com.assessment;

import com.assessment.model.Users;
import com.assessment.service.AppraisalPlanService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AssessmentApplicationTests {
	@Autowired
	AppraisalPlanService appraisalPlanService;
	@Test
	public void contextLoads() {
	}

	@Test
	public void getBackInfo(){
		Users users = new Users();
		users.setUsername("admin");
		users.setId(4997);
		appraisalPlanService.setDepartmentPlanScore("2019-10-20",users,null);
	}
}
