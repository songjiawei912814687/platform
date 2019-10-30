package com.api.domain.output;

import com.api.model.AppointmentNumber;

/**
 * @author: young
 * @project_name: svn
 * @description:
 * @date: Created in 2019-01-06  22:13
 * @modified by:
 */
public class AppointmentNumberOutput extends AppointmentNumber {

    /**是否已经取消预约名称*/
    private String appointmentName;

    public String getAppointmentName() {
        if(getAmputated()!=null){
            if(getAmputated()==0){
                appointmentName = "预约成功";
            }else if(getAmputated()==1){
                appointmentName = "已取消";
            }
        }

        return appointmentName;
    }

    public void setAppointmentName(String appointmentName) {
        this.appointmentName = appointmentName;
    }
}
