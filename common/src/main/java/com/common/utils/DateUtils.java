package com.common.utils;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateUtils {

    public static String dateToString(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        return formatter.format(date);
    }

    public static String dateToString1(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        return formatter.format(date);
    }

    public static Date stringToDate(String time) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");//yyyy-mm-dd, 会出现时间不对, 因为小写的mm是代表: 秒
        Date utilDate = sdf.parse(time);
        Date date = new java.sql.Date(utilDate.getTime());
        return date;
    }

    public static Date stringToDates(String time) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//yyyy-mm-dd, 会出现时间不对, 因为小写的mm是代表: 秒
        Date utilDate = sdf.parse(time);
        Date date = new java.sql.Date(utilDate.getTime());
        return date;
    }
    public static String datesToString(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return formatter.format(date);
    }

    public static Date getNowDateTime() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        ParsePosition pos = new ParsePosition(0);
        Date currentTime_2 = formatter.parse(dateString, pos);
        return currentTime_2;

    }

    /**
     * 获取当年的第一天
     *
     * @return
     */
    public static Date getCurrYearFirst() {
        Calendar currCal = Calendar.getInstance();
        int currentYear = currCal.get(Calendar.YEAR);
        return getYearFirst(currentYear);
    }

    /**
     * 获取当年的最后一天
     *
     * @return
     */
    public static Date getCurrYearLast() {
        Calendar currCal = Calendar.getInstance();
        int currentYear = currCal.get(Calendar.YEAR);
        return getYearLast(currentYear);
    }

    /**
     * 获取某年第一天日期
     *
     * @param year 年份
     * @return Date
     */
    public static Date getYearFirst(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        Date currYearFirst = calendar.getTime();
        return currYearFirst;
    }

    /**
     * 获取某年最后一天日期
     *
     * @param year 年份
     * @return Date
     */
    public static Date getYearLast(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.roll(Calendar.DAY_OF_YEAR, -1);
        Date currYearLast = calendar.getTime();

        return currYearLast;
    }

    /**
     * 比较时间的日期部分的大小（前<后 返回 -1 ；前 = 后 返回 0 ；前>/后 返回1）
     */
    public static Integer compareData(Date date1,Date date2) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        if(date1==null||date2==null){
            return  null;
        }
        String date1String = formatter.format(date1);
        String date2String = formatter.format(date2);
        try {
            date1 = formatter.parse(date1String);
            date2 = formatter.parse(date2String);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(date1.getTime()>date2.getTime()){
            return 1;
        }else if(date1String.equals(date2String)){
            return 0;
        }else{
            return -1;
        }
    }

    /**
     * 去除时间的时分秒部分
     */
    public static Date reMoveExceptDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        if(date==null){
            return  null;
        }
        String dateString = formatter.format(date);
        try {
            date = formatter.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  date;
    }

    public static Date setMonth(Date nowDate,int month){
        Calendar c = Calendar.getInstance();
        c.setTime(nowDate);
        c.add(Calendar.MONTH, month);
        return c.getTime();
    }


    /**
     * 取当前时间的前N天或后N天的零点
     * @param day
     * @return
     */
    public static Date getDateOfAfterOrBefore(Integer day){
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.DATE, day);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        now = calendar.getTime();
        return now;
    }


    public static void main(String[] args) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.DATE,1);
        String startDate = sdf.format(calendar.getTime());
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
        String endDate = sdf.format(calendar.getTime());

        System.out.println("startDate:"+startDate);
        System.out.println("endDate:"+endDate);
    }
}
