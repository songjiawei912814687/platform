package com.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class HolidayUtils {
        private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        /**
         * 获取计划激活日期
         * @param today opening date
         * @param  holidayList
         * @param  overTimeList
         * @param num num个工作日后
         * @return
         * @throws ParseException
         */
        public static Date getScheduleActiveDate(Date today, List<String> holidayList, List<String> overTimeList, int num) throws ParseException {
            Date tomorrow = null;
            Date yesderday = null;
            if(num>=0){
                int delay = 1;
                while(delay <= num){
                    tomorrow = getTomorrow(today);
                    //当前日期+1,判断是否是非工作日,
                    int holiday = isHoliday(sdf.format(tomorrow), holidayList, overTimeList);
                    if(holiday==1){
                        delay++;
                        today = tomorrow;
                    }else if(holiday==0){
                        today = tomorrow;
                    }else{
                        if (isWeekend(sdf.format(tomorrow))){
                            today = tomorrow;
                        }else{
                            delay++;
                            today = tomorrow;
                        }
                    }
                }
                return today;
            }else{
                int delay = 1;
                num = -num;
                while(delay <= num){
                    yesderday = getYesterday(today);
                    //当前日期+1,判断是否是非工作日,
                    int holiday = isHoliday(sdf.format(yesderday), holidayList, overTimeList);
                    if(holiday==1){
                        delay++;
                        today = yesderday;
                    }else if(holiday==0){
                        today = yesderday;
                    }else{
                        if (isWeekend(sdf.format(yesderday))){
                            today = yesderday;
                        }else{
                            delay++;
                            today = yesderday;
                        }
                    }
                }
                return today;
            }
        }


    /**
     * 获取工作日
     * @param today opening date
     * @param  holidayList
     * @param  overTimeList
     * @param num num个工作日后
     * @return
     * @throws ParseException
     */
    public static List<String> getWorkDays(Date today, List<String> holidayList, List<String> overTimeList, int num) throws ParseException {
        Date tomorrow = null;
        Date yesderday = null;
        List<String> list = new ArrayList<>();
//        list.add(sdf.format(today));
        if(num>=0){
            int delay = 1;
            while(delay <= num){
                tomorrow = getTomorrow(today);
                //当前日期+1,判断是否是非工作日,
                int holiday = isHoliday(sdf.format(tomorrow), holidayList, overTimeList);
                if(holiday==1){
                    delay++;
                    list.add(sdf.format(tomorrow));
                    today = tomorrow;
                }else if(holiday==0){
                    today = tomorrow;
                }else{
                    if (isWeekend(sdf.format(tomorrow))){
                        today = tomorrow;
                    }else{
                        delay++;
                        list.add(sdf.format(tomorrow));
                        today = tomorrow;
                    }
                }
            }
            return list;
        }else{
            int delay = 1;
            num = -num;
            while(delay <= num){
                yesderday = getYesterday(today);
                //当前日期+1,判断是否是非工作日,
                int holiday = isHoliday(sdf.format(yesderday), holidayList, overTimeList);
                if(holiday==1){
                    delay++;
                    list.add(sdf.format(yesderday));
                    today = yesderday;
                }else if(holiday==0){
                    today = yesderday;
                }else{
                    if (isWeekend(sdf.format(yesderday))){
                        today = yesderday;
                    }else{
                        delay++;
                        list.add(sdf.format(yesderday));
                        today = yesderday;
                    }
                }
            }
            Collections.reverse(list);
            return list;
        }
    }

    public static boolean isWorkDay(Date today, List<String> holidayList, List<String> overTimeList) throws ParseException {
        int holiday = isHoliday(sdf.format(today), holidayList, overTimeList);
        if(holiday==1){
            return false;
        }else if(holiday==0){
            return true;
        }else{
            if (isWeekend(sdf.format(today))){
                return true;
            }else{
                return false;
            }
        }
    }

        /**
         * 获取tomorrow的日期
         *
         * @param date
         * @return
         */
        public static Date getTomorrow(Date date){
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DATE, +1);
            date = calendar.getTime();
            return date;
        }

    /**
     * 获取tomorrow的日期
     *
     * @param date
     * @return
     */
    public static Date getYesterday(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -1);
        date = calendar.getTime();
        return date;
    }

        /**
         * 判断是否是weekend
         *
         * @param sdate
         * @return
         * @throws ParseException
         */
        public static boolean isWeekend(String sdate) throws ParseException {
            Date date = sdf.parse(sdate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
                return true;
            } else{
                return false;
            }

        }

        /**
         * 判断是否是holiday
         * 0 表示非工作日 1表示工作日 2表示不再表中
         * @param sdate
         * @param holidayList
         * @param overList
         * @return
         * @throws ParseException
         */
        public static int isHoliday(String sdate, List<String> holidayList,List<String> overList) throws ParseException {
            if(overList.size() > 0){
                for(int i = 0; i < overList.size(); i++){
                    if(sdate.equals(overList.get(i))){
                        return 1;
                    }
                }
            }
            if(holidayList.size() > 0){
                for(int i = 0; i < holidayList.size(); i++){
                    if(sdate.equals(holidayList.get(i))){
                        return 0;
                    }
                }
            }
            return 2;
        }
}
