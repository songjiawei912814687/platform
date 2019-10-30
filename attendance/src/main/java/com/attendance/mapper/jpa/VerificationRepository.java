package com.attendance.mapper.jpa;

import com.attendance.model.Verification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author: young
 * @project_name: assist-decision
 * @description:
 * @date: Created in 2019-03-29  13:40
 * @modified by:
 */
public interface VerificationRepository extends JpaRepository<Verification,Integer> {


    void deleteAllByVerificationTimeOne(Date verificationTimeOne);
    List<Verification> getByLeaveApplicationId(Integer leaveApplicationId);
}
