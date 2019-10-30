package com.wechatsug.config;

import com.wechatsug.service.WechatsugService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;


/**
 * leeoohoo@gmail.com
 */
@Component
public class RedisMessageReceiver {

    @Autowired
    private WechatsugService wechatsugService;


    /**
     * 接收redis消息，并处理
     *
     * @param message 过期的redis key
     */
    public void message(String message) throws IntrospectionException, MethodArgumentNotValidException, IllegalAccessException, InvocationTargetException {
        System.out.println("通知的key是：" + message);
        if(message.indexOf("HZFT_MESSAGE") > 0){
            var strs = message.split(":");
            wechatsugService.send(Integer.parseInt(strs[1]));
        }
    }

}
