package com.common.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author: young
 * @description: 日期格式转换 java 2016-09-03T00:00:00.000+08:00
 */
public class DealDateFormatUtil {

    public static String dealDate(String oldDateStr) throws ParseException {
        //此格式只有  jdk 1.7才支持  yyyy-MM-dd‘T‘HH:mm:ss.SSSXXX
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date date = df.parse(oldDateStr);
        SimpleDateFormat df1 = new SimpleDateFormat ("EEE MMM dd HH:mm:ss Z yyyy", Locale.UK);
        Date date1 =  df1.parse(date.toString());
        DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//  Date date3 =  df2.parse(date1.toString());
        return df2.format(date1);
    }
}
