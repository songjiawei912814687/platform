package com.selfservice.config;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;


/**
 * leeoohoo@gmail.com
 */
@Component
public class RedisMessageReceiver {



    /**
     * 接收redis消息，并处理
     *
     * @param message 过期的redis key
     */
    public void message(String message) throws IntrospectionException, MethodArgumentNotValidException, IllegalAccessException, InvocationTargetException {
//        System.out.println("通知的key是：" + message);
//        if(message.indexOf("HZFT_MESSAGE") > 0){
//            var strs = message.split(":");
//        }
    }

}
