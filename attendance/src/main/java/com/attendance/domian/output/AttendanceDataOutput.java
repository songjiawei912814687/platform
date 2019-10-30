package com.attendance.domian.output;


import com.attendance.model.AttendanceData;
import lombok.Data;

@Data
public class AttendanceDataOutput extends AttendanceData {

    private String organizationName;

    private String employeeName;

    private String employeeNo;

    private String jobsName;

    private Integer count;

}
