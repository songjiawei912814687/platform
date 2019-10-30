package com.common.utils;

public class NumberUtil {
    public static String toChinese(String string) {
        String[] s1 = { "零", "一", "二", "三", "四", "五", "六", "七", "八", "九" };
        String[] s2 = { "十", "百", "千", "万", "十", "百", "千", "亿", "十", "百", "千" };

        String result = "";

        int n = string.length();
        for (int i = 0; i < n; i++) {

            int num = string.charAt(i) - '0';

            if (i != n - 1 && num != 0) {
                result += s1[num] + s2[n - 2 - i];
            } else {
                result += s1[num];
            }
        }
        if(result.length()>1){
            int count = result.length();
            if(result.charAt(0)=='一' && result.indexOf("百")<0 && result.indexOf("千")<0 && result.indexOf("万")<0){
                result = result.substring(1);
            }
            boolean flag = true;
            while (flag){
                if(result.charAt(result.length()-1)=='零'){
                    result = result.substring(0,result.length()-1);
                }else {
                    flag = false;
                }
            }
        }
        if(result.indexOf("千")>=0 && result.indexOf("零零")>=0){
            result = result.replace("零零","零");
        }
        if(result.indexOf("万")>=0 && result.indexOf("零零零")>=0){
            result = result.replace("零零零","零");
        }
        if(result.indexOf("万")>=0 && result.indexOf("零零")>=0){
            result = result.replace("零零","零");
        }

        return result;

    }


    public static void main(String[] args) {
        int i = 11;
        String a = String.valueOf(20010);
        String str = NumberUtil.toChinese(a);
        System.out.println(str);
    }


}
