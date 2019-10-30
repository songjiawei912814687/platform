package com.feedback.domain.output;


import com.common.Enum.CheckStatusEnum;
import com.common.Enum.IsWorkDayStatusEnum;
import com.feedback.model.Holiday;

public class HolidayOutput extends Holiday {

    private String statusName;

    private String isWorkDayName;


    public String getStatusName() {
        if(getStatus()!=null){
            switch (getStatus()){
                case 0:
                    statusName = CheckStatusEnum.UN_CHECK.getMsg();
                    break;
                case 1:
                    statusName = CheckStatusEnum.CHECK_FINISH.getMsg();
                    break;
                case 2:
                    statusName = CheckStatusEnum.CHECK_UN_PASS.getMsg();
                    break;
            }
        }
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getIsWorkDayName() {
        if(getIsWorkDay()!=null){
            switch (getIsWorkDay()){
                case 1:
                    isWorkDayName = IsWorkDayStatusEnum.NOT_WORKDAY.getMsg();
                    break;
                case 3:
                    isWorkDayName = IsWorkDayStatusEnum.IS_WORKDAY.getMsg();
                    break;
            }
        }
        return isWorkDayName;
    }

    public void setIsWorkDayName(String isWorkDayName) {
        this.isWorkDayName = isWorkDayName;
    }
}
