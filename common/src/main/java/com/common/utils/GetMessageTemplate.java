package com.common.utils;

import java.util.*;

public class GetMessageTemplate {

    public static Map<String,String> getKey(String template){
        if(template == null || "".equals(template)){
            return null;
        }
        Map<String,String> map = new HashMap<>();
        var strs = template.split("\\u007B");
        for(var str : strs){
            if(str.indexOf("}") >= 0){
                var s = str.split("}");
                map.put(s[0],"");
            }
        }
        return map;
    }

    public static String getContent(String template,Map<String,String> map){
        if(map.size() <= 0 || template == null || "".equals(template)){
            return null;
        }
        for(Map.Entry<String,String> entry : map.entrySet()){
            var index = template.indexOf(entry.getKey());
            if(index > 0){
                template = template.replaceAll("\\u007B"+entry.getKey()+"}",entry.getValue() == null ? "" : entry.getValue());
            }
            System.out.println(entry.getKey());
        }
        return template;
    }





}
